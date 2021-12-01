package com.example.springproject.controller;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Returns all users in XML format
    @GetMapping(value = "/users/getAll/xml", produces = {"application/xml"})
    public List<UserDto> getAllUsersXML() {
        return userService.getAll();
    }

    @GetMapping(value = "/users/getAll", produces = {"application/json"})
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/user/getById/{id}", produces = {"application/json"})
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        Optional<UserDto> userOptional = userService.getUserById(id);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id doesn't exist"));
    }

    @GetMapping(value = "user/getByEmail/{email}", produces = {"application/json"})
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> userOptional = userService.getUserByEmail(email);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist"));
    }

    @GetMapping(value = "/user/getByUserName", produces = {"application/json"})
    public ResponseEntity<?> getUserByName(@RequestParam(name = "user", required = false) String username) {
        Optional<UserDto> userOptional = userService.getUserByName(username);

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find user by that email");

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.map(userOptional.get()));
    }

    @PostMapping(value = "/user/create", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        Optional<UserDto> userOptional = userService.createUser(userDto);

        if (userOptional.isPresent())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User with that email or username already exists");

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.map(userDto));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        Optional<UserDto> userOptional = userService.deleteById(id);

        return userOptional.map(user -> ResponseEntity.status(HttpStatus.OK).body("User removed successfully: " + UserMapper.map(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id doesn't exist"));
    }

    @PutMapping("/user/edit/{userName}")
    public ResponseEntity<String> updateUserById(@PathVariable("userName") String userName, String userNameFromToken, Integer choice, @RequestBody UserDto user) {
        if (!userName.equals(userNameFromToken) && !user.getAccess()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");

        Optional<UserDto> userOptional = userService.updateUserByUserName(userName,choice,user);

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with username " + user.getUserName() + " was found.");

        else if (userOptional.stream().anyMatch(Objects::isNull))
            return ResponseEntity.badRequest().body("One or more fields are not filled. Please enter a value for all attributes.");

        else return ResponseEntity.status(HttpStatus.OK).body("Successfully updated ");

    }
}
