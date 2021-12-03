package com.example.springproject.controller;

import com.example.springproject.data.mapper.UserMapper;
import com.example.springproject.entity.UserDto;
import com.example.springproject.exception.NotFoundGlobalException;
import com.example.springproject.response.UserResponse;
import com.example.springproject.service.MessageService;
import com.example.springproject.request_body.EditUserRequestBody;
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

    @GetMapping(value = "user/getByEmail/{email}", produces = {"application/json"})
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> userOptional = userService.getUserByEmail(email);

        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email doesn't exist");

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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User with that email or username already exists");

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.map(userDto));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        Optional<UserDto> userOptional = userService.deleteById(id);

        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with that ID not found");

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.map(userOptional.get()));
    }

    @PutMapping("/user/edit/{userName}")
    public ResponseEntity<?> updateProfile(@PathVariable("userName") String userName, @RequestBody EditUserRequestBody editUserRequestBody) {
        if (editUserRequestBody.getUserNameFromToken().equals("empty") && !editUserRequestBody.getUser().getAccess())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");

        Optional<UserDto> userOptional = userService.updateUserByUserName(userName, editUserRequestBody.getChoice(), editUserRequestBody.getUser());

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with username " + editUserRequestBody.getUser().getUserName() + " was found.");

        else if (userOptional.stream().anyMatch(Objects::isNull))
            return ResponseEntity.badRequest().body("One or more fields are not filled. Please enter a value for all attributes.");

        else return ResponseEntity.status(HttpStatus.OK).body("Successfully updated ");
    }
}
