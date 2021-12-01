package com.example.springproject.controller;

import com.example.springproject.data.User;
import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")
public class UserController {

    final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    //Returns all users in XML format
    @GetMapping(value = "/users/getAll/xml", produces = {"application/xml"})
    public List<UserDto> getAllUsersXML() {
        return userService.getAll();
    }

    @GetMapping("/users/getAll")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/user/getById/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        Optional<UserDto> userOptional = userService.getUserById(id);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id doesn't exist"));
    }

    @GetMapping("user/getByEmail/{email}")
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> userOptional = userService.getUserByEmail(email);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist"));
    }

    @GetMapping("/user/getByUserName")
    public ResponseEntity<?> getUserByName(@RequestParam(name = "user", required = false) String username) {
        Optional<UserDto> user = userService.getUserByName(username);

        return user.map(userDto -> ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(userDto)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist"));
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        Optional<UserDto> userOptional = userService.createUser(userDto);

        return userOptional.map(user -> new ResponseEntity<>(UserMapper.map(userDto), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(UserMapper.map(userDto), HttpStatus.FORBIDDEN));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        Optional<UserDto> userOptional = userService.deleteById(id);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User removed successfully: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id doesn't exist"));
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable("id") Long id, @RequestBody UserDto user) {
        return userService.updateUserById(id, user);
    }
}
