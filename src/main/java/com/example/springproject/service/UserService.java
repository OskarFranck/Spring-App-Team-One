package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDto user = userRepository.findByUserName(username);
        if(user==null) {
            throw new NotFoundException("User with username: " + username + "not found");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String role = user.getAccess() ? "ADMIN" : "USER";
            authorities.add(new SimpleGrantedAuthority(role));
            return new User(user.getUserName(), user.getPassword(), authorities);
            }
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

    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public ResponseEntity<?> deleteById(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.badRequest().body(
                    "Student with id" + id + "does not exists");
        } else {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Student with id" + id + "removed successfully");
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
