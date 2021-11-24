package com.example.springproject.service;

import com.example.springproject.data.UserDto;

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
    try {
        UserDto user = userRepository.findAll()
                .stream().filter(currentUser -> currentUser.getEmail()
                        .equals(userDto.getEmail()))
                .collect(Collectors.toList()).get(0);
        return user != null;
    }
    catch(IndexOutOfBoundsException e){
        e.printStackTrace();
        return false;
    }

    }

    public List<UserDto> getAll() {
        return userRepository.findAll();
    }


    public UserResponse getUserById(Long id) {
        Optional<UserDto> userDtoOptional = userRepository.findById(id);
        if (!userDtoOptional.isPresent()) {
            throw new NotFoundException();
        }
        return new UserResponse(userDtoOptional.get());

    public ResponseEntity<String> updateUser(Long id, UserDto user) {
        if (userRepository.findById(id).isPresent()) {
            UserDto newUser = userRepository.findById(id).orElseThrow();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated ");
        } else {
            return ResponseEntity.badRequest().body("No user with id " + user.getId() + " was found.");
        }

    }
}