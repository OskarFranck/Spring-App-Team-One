package com.example.springproject.controller;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.NotFoundGlobalException;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.MessageService;
import com.example.springproject.service.UserService;
import com.example.springproject.util.MessageUtil;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;


    //Returns all users in XML format
    @GetMapping(value = "/users/getAll/xml", produces = { "application/xml" })
    public List<UserDto> getAllUsersXML() {
        return userService.getAll();
    }

    @GetMapping(value="/users/getAll", produces = { "application/json" })
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value="/user/getById/{id}", produces = { "application/json" })
    public UserResponse getUserById(@PathVariable Long id) {

        Optional <UserDto> user = userService.getUserById(id);
        if (user.isEmpty()) {throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_ID_NOT_FOUND));}

        return new UserResponse(user.get());
    }

    @GetMapping(value="user/getByEmail/{email}", produces = { "application/json" })
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body("User: " + UserMapper.map(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist");
        }
    }

    @GetMapping(value="/user/getByUserName", produces = { "application/json" })
    public ResponseEntity<?> getUserByName(@RequestParam(name = "user", required = false) String username) {
        UserDto userDto = userService.findUserByName(username);
        if (userDto != null) {
            return ResponseEntity.ok().body(userDto);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping(value="/user/create", consumes={"application/json"}, produces = { "application/json" })
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