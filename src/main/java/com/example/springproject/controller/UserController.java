package com.example.springproject.controller;

import com.example.springproject.entity.UserDto;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.UserService;
import jakarta.ws.rs.Produces;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Returns all users in XML format
    @GetMapping(value = "/users-xml", produces = { "application/xml" })
    public List<UserDto> getAllUsersXML() {
        return userService.getAll();
    }

    @GetMapping(value ="/users", produces = { "application/json" })
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value ="/users/{id}", produces = { "application/json" })
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value ="users/{email}", produces = { "application/json" })
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping(value ="/user", produces = { "application/json" })
    public ResponseEntity<?> getUserByName(@RequestParam(name = "user", required = false) String username) {
        UserDto userDto = userService.findUserByName(username);
        if (userDto != null) {
            return ResponseEntity.ok().body(userDto);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping(value ="/users",consumes={"application/json"}, produces = { "application/json" })
    public ResponseEntity<String> newUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @DeleteMapping(value ="/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        return userService.deleteById(id);
    }

    @PutMapping(value ="/users/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Long id, @RequestBody UserDto user) {
        return userService.updateUserById(id, user);
    }
}
