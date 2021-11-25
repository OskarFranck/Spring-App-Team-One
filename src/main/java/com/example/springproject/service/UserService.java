package com.example.springproject.service;

import com.example.springproject.entity.UserDto;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> addUser(UserDto userDto) {
        if (emailExists(userDto)) {
            return ResponseEntity.badRequest().body("There is an account with that email address: "
                    + userDto.getEmail());
        } else {
            userRepository.save(userDto);
            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }

    }

    private boolean emailExists(UserDto userDto) {
        List<UserDto> userList = userRepository.findAll();

        UserDto user = null;
        if (!userList.isEmpty()){
            List<UserDto> users = userList.stream().filter(currentUser -> currentUser.getEmail()
                    .equals(userDto.getEmail())).collect(Collectors.toList());
            if(!users.isEmpty()){
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

            if(!users.isEmpty()) {
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
        }
        else {
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
            newUser.setUserName(user.getUserName());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            newUser.setAccess(user.getAccess());
            if(Stream.of(user.getAccess(), user.getEmail(), user.getPassword(), user.getUserName()).anyMatch(Objects::isNull))
            {
                return ResponseEntity.badRequest().body("One or more fields are not filled. Please enter a value for all attributes.");

            }
            else{
                userRepository.save(newUser);
            }

            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated ");
        } else {
            return ResponseEntity.badRequest().body("No user with id " + id + " was found.");
        }
    }
}
