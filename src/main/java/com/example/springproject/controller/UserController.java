package com.example.springproject.controller;

import com.example.springproject.data.UserDto;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<String> newUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUserById(@PathVariable Long id){
//        userService.deleteById(id);
        return "User with id: " + id + " has been deleted.";
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable Long id, @RequestBody UserDto user){

        return userService.updateUser(id, user);
    }

}
