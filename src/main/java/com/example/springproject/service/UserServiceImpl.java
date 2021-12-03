package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.GlobalException;
import com.example.springproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto> userOptional = userRepository.findByUserName(username);
        String role;
        if (userOptional.isEmpty()) {
            throw new GlobalException(HttpStatus.FORBIDDEN,"User not found in the database");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            role = userOptional.get().getAccess() ? "ADMIN" : "USER";

            authorities.add(new SimpleGrantedAuthority(role));

            return new User(userOptional.get().getUserName(), userOptional.get().getPassword(), authorities);
        }
    }

    @Override
    public Optional<UserDto> createUser(UserDto userDto) {

        Optional<UserDto> userOptional = getUserByEmail(userDto.getEmail());

        if (!userExists(userDto)) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userDto);
        }
        return userOptional;
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
    public Optional<UserDto> deleteById(Long id) {
        Optional<UserDto> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) userRepository.deleteById(id);
        return userOptional;
    }

    @Override
    public Optional<UserDto> getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<UserDto> updateUserByUserName(String userName, Integer choice) {
        Optional<UserDto> userOptional = userRepository.findByUserName(userName);

        if (userOptional.isEmpty()) return userOptional;

        UserDto user = userOptional.get();

        switch (choice) {
            case 1:
                user.setUserName(userOptional.get().getUserName());
                break;
            case 2:
                user.setPassword(passwordEncoder.encode(userOptional.get().getPassword()));
                break;
            case 3:
                user.setEmail(userOptional.get().getEmail());
                break;
            default:
                break;
        }
        userRepository.save(user);

        return userOptional;

    }
}
