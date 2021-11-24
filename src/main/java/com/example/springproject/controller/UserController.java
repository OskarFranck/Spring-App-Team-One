package com.example.springproject.controller;

import com.example.springproject.data.UserDto;
import com.example.springproject.service.UserService;
import org.apache.catalina.User;
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

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAll();
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
    public UserDto updateUserById(@PathVariable Long id, @RequestBody UserDto user){
//        if(id!=null) {
//            //insert custom exception
//            UserDto newUser = userService.findById(id).orElseThrow();
//            newUser.setUserName(user.getUserName());
//            newUser.setEmail(user.getEmail());
//            return newUser;
//        }
        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserByName(@RequestParam(name = "user", required = false) String username) {
        return ResponseEntity.ok().body(userService.findUserByName(username));
    }
}