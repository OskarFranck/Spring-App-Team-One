package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            role = userOptional.get().getAccess() ? "ADMIN" : "USER";

            authorities.add(new SimpleGrantedAuthority(role));

            return new User(userOptional.get().getUserName(), userOptional.get().getPassword(), authorities);
        }
    }

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

    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll();
    }


    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDto> deleteById(Long id) {
        Optional<UserDto> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) userRepository.deleteById(id);
        return userOptional;
    }

    public Optional<UserDto> getUserByName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public ResponseEntity<String> updateUserById(Long id, UserDto user) {

        if (userRepository.findById(id).isPresent()) {
            UserDto newUser = userRepository.findById(id).orElseThrow();
            //Ugly  code I know
            if (user.getUserName() != null) newUser.setUserName(user.getUserName());
            if (user.getPassword() != null) newUser.setPassword(user.getPassword());
            if (user.getEmail() != null) newUser.setEmail(user.getEmail());
            if (user.getAccess() != null) newUser.setAccess(user.getAccess());

            if (Stream.of(newUser.getAccess(), newUser.getEmail(), newUser.getPassword(), newUser.getUserName()).anyMatch(Objects::isNull)) {
                return ResponseEntity.badRequest().body("One or more fields are not filled. Please enter a value for all attributes.");

            } else {
                userRepository.save(newUser);
            }

            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with id " + id + " was found.");
        }
    }
}
