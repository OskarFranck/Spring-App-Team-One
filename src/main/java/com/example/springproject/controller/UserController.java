package com.example.springproject.controller;

import com.example.springproject.data.User;
import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.data.mapper.UsersMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.AlreadyExistsGlobalException;
import com.example.springproject.exception.NotFoundGlobalException;
import com.example.springproject.request_body.EditUserRequestBody;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.MessageService;
import com.example.springproject.service.UserService;
import com.example.springproject.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    //Returns all users in XML format
    @GetMapping(value = "/users/getAll/xml", produces = {"application/xml"})
    public List<User> getAllUsersXML() {
        List<UserDto> users = userService.getAll();
        if (users.isEmpty()) throw new NotFoundGlobalException(MessageUtil.USERS_NOT_FOUND);
        return UsersMapper.map(users);
    }

    @GetMapping(value = "/users/getAll", produces = {"application/json"})
    public List<User> getAllUsers() {
        List<UserDto> users = userService.getAll();
        if (users.isEmpty()) throw new NotFoundGlobalException(MessageUtil.USERS_NOT_FOUND);
        return UsersMapper.map(users);
    }

    @GetMapping(value = "/user/getById/{id}", produces = {"application/json"})
    public UserResponse getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        if (user.isEmpty()) throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_ID_NOT_FOUND));
        return new UserResponse(user.get());
    }

    @GetMapping(value = "user/getByEmail/{email}", produces = {"application/json"})
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty())
            throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_EMAIL_NOT_FOUND));

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.map(userOptional.get()));
    }

    @GetMapping(value = "/user/getByUserName/{username}", produces = {"application/json"})
    public ResponseEntity<?> getUserByName(@PathVariable String username) {
        Optional<UserDto> userOptional = userService.getUserByName(username);

        if (userOptional.isEmpty())
            throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_NAME_NOT_FOUND));

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.map(userOptional.get()));
    }

    @PostMapping(value = "/user/create", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        boolean userExist = userService.createUser(userDto);

        if (userExist)
            throw new AlreadyExistsGlobalException(messageService.getLocalMessage(MessageUtil.ALREADY_EXISTS));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.map(userDto));
    }

    @DeleteMapping("/users/delete/{username}")
    public UserResponse deleteUserByUsername(@PathVariable("username") String username) {
        Optional<UserDto> user = userService.deleteByUsername(username);
        if (user.isEmpty()) throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_ID_NOT_FOUND));
        return new UserResponse(user.get());
    }


    @PutMapping("/user/edit/{userName}")
    public ResponseEntity<?> updateUser(@PathVariable("userName") String userName, @RequestBody EditUserRequestBody editUserRequestBody) {
        Optional<UserDto> user = userService.updateUserByUserName(userName, editUserRequestBody, messageService);

        if (user.isEmpty())
            throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_NAME_NOT_FOUND));

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.map(user.get()));
    }
}
