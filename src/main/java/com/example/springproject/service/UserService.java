package com.example.springproject.service;

import com.example.springproject.data.UserDto;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    final UserRepository userRepository;

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

}
