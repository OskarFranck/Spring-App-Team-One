package com.example.springproject.service;

import com.example.springproject.entity.UserDto;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userRepository.findByUserName(username);
        if (userDto == null) {
            throw new UsernameNotFoundException(username);
        } else {
            System.out.println("user found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (userDto.getAccess()) {
            authorities.add(new SimpleGrantedAuthority("admin"));
            authorities.add(new SimpleGrantedAuthority("user"));
        } else {
            authorities.add(new SimpleGrantedAuthority("user"));
        }
        return new User(userDto.getUserName(), userDto.getPassword(), authorities);
    }

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addUser(UserDto userDto) {
        if (emailExists(userDto)) {
            return ResponseEntity.badRequest().body("There is an account with that email address: "
                    + userDto.getEmail());
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userDto);
            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }

    }

    private boolean emailExists(UserDto userDto) {
        List<UserDto> userList = userRepository.findAll();

        UserDto user = null;
        if (!userList.isEmpty()) {
            List<UserDto> users = userList.stream().filter(currentUser -> currentUser.getEmail()
                    .equals(userDto.getEmail())).collect(Collectors.toList());
            if (!users.isEmpty()) {
                user = users.get(0);
            }
        }
        return user != null;
    }

    public ResponseEntity<String> getUserByEmail(String email) {
        List<UserDto> userList = userRepository.findAll();
        UserDto user = null;

        if (!userList.isEmpty()) {
            List<UserDto> users = userList.stream().filter(userDto -> userDto.getEmail()
                    .equals(email)).collect(Collectors.toList());

            if (!users.isEmpty()) {
                user = users.get(0);
            }
        }
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist");
        }
    }

    public List<UserDto> getAll() {
        return userRepository.findAll();
    }


    public UserResponse getUserById(Long id) {
        Optional<UserDto> userDtoOptional = userRepository.findById(id);
        if (userDtoOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return new UserResponse(userDtoOptional.get());
    }

    public ResponseEntity<String> deleteById(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.badRequest().body(
                    "Student with id" + id + "does not exists");
        } else {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Student with id" + id + "removed successfully!");
        }
    }

    public UserDto findUserByName(String userName) {
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
