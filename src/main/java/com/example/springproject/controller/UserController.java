package com.example.springproject.controller;

import com.example.springproject.data.User;
import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.data.mapper.UsersMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.AlreadyExistsGlobalException;
import com.example.springproject.exception.NotFoundGlobalException;
import com.example.springproject.exception.UnAuthorizedGlobalException;
import com.example.springproject.request_body.EditUserRequestBody;
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
            throw new AlreadyExistsGlobalException(messageService.getLocalMessage(MessageUtil.ALREADY_EXISTS));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.map(userDto));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        Optional<UserDto> userOptional = userService.deleteById(id);

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with that ID not found");
    }
        @DeleteMapping("/users/delete/{id}")
    public UserResponse deleteUserById(@PathVariable("id") Long id) {
        Optional<UserDto> user = userService.deleteById(id);
        if (user.isEmpty()) throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_ID_NOT_FOUND));
        return new UserResponse(user.get());
    }


    @PutMapping("/user/edit/{userName}")
    public ResponseEntity<?> updateUser(@PathVariable("userName") String userName, @RequestBody EditUserRequestBody editUserRequestBody) {
        Optional<UserDto> user = userService.updateUserByUserName(userName, editUserRequestBody.getChoice());

        if (user.isEmpty())
            throw new NotFoundGlobalException(messageService.getLocalMessage(MessageUtil.USER_NAME_NOT_FOUND));

        else if (editUserRequestBody.getUserNameFromToken().equals("empty") && !user.get().getAccess())
            throw new UnAuthorizedGlobalException(messageService.getLocalMessage(MessageUtil.UNAUTHORIZED));

        return ResponseEntity.status(HttpStatus.OK).body("Successfully updated: " + UserMapper.map(user.get()));
    }
}
