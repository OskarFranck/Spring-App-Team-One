package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.NotFoundGlobalException;
import com.example.springproject.exception.UnAuthorizedGlobalException;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.request_body.EditUserRequestBody;
import com.example.springproject.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> userOptional = userRepository.findByUserName(username);
        String role;
        if (userOptional.isEmpty()) {
            throw new NotFoundGlobalException(MessageUtil.USER_NAME_NOT_FOUND);
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            role = userOptional.get().getAccess() ? "ADMIN" : "USER";

            authorities.add(new SimpleGrantedAuthority(role));

            return new User(userOptional.get().getUserName(), userOptional.get().getPassword(), authorities);
        }
    }

    @Override
    public boolean createUser(UserDto userDto) {

        if (!userExists(userDto)) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userDto);
            return false;
        }
        return true;
    }

    private boolean userExists(UserDto userDto) {
        Optional<UserDto> userOptional = getUserByEmail(userDto.getEmail());
        Optional<UserDto> userOptional2 = getUserByName(userDto.getUserName());

        return userOptional.isPresent() || userOptional2.isPresent();
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll();
    }


    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserDto> deleteByUsername(String username) {
        Optional<UserDto> userOptional = userRepository.findByUserName(username);
        userOptional.ifPresent(userDto -> userRepository.deleteById(userDto.getId()));
        return userOptional;
    }

    @Override
    public Optional<UserDto> getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean getCurrentUserAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) return true;
        return !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) return null;
        return authentication.getName();
    }

    @Override
    public Optional<UserDto> updateUserByUserName(String userName, EditUserRequestBody editUserRequestBody, MessageService messageService) {
        Optional<UserDto> userOptional = userRepository.findByUserName(userName);
        Optional<UserDto> editingUser = userRepository.findByUserName(editUserRequestBody.getUsernameFromToken());

        if (userOptional.isEmpty()) return userOptional;

        else if (!editUserRequestBody.getUsernameFromToken().equals(userName) && (editingUser.isPresent() && !editingUser.get().getAccess()))
            throw new UnAuthorizedGlobalException(messageService.getLocalMessage(MessageUtil.UNAUTHORIZED));

        UserDto user = userOptional.get();

        switch (editUserRequestBody.getChoice()) {
            case 1:
                user.setUserName(editUserRequestBody.getUsername());
                break;
            case 2:
                user.setPassword(passwordEncoder.encode(editUserRequestBody.getPassword()));
                break;
            case 3:
                user.setEmail(editUserRequestBody.getEmail());
                break;
            case 4:
                user.setAccess(editUserRequestBody.getAccess());
            default:
                break;
        }
        userRepository.save(user);

        return userOptional;

    }
}
