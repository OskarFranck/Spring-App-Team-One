package com.example.springproject.controller;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Returns all users in XML format
    @GetMapping(value = "/users/getAll/xml", produces = { "application/xml" })
    public List<UserDto> getAllUsersXML() {
        return userService.getAll();
    }

    @GetMapping("/users/getAll")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/user/getById/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("user/getByEmail/{email}")
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist");
        }
    }

    @GetMapping("/user/getByUserName")
    public ResponseEntity<?> getUserByName(@RequestParam(name = "user", required = false) String username) {
        UserDto userDto = userService.findUserByName(username);
        if (userDto != null) {
            return ResponseEntity.ok().body(userDto);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/user/create")
    public ResponseEntity<String> newUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        return userService.deleteById(id);
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Long id, @RequestBody UserDto user) {
        return userService.updateUserById(id, user);
    }
}
