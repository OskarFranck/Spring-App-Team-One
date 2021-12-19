package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.request_body.EditUserRequestBody;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAll();
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> getUserById(Long id);
    boolean getCurrentUserAccess();
    String getCurrentUserName();
    Optional<UserDto> getUserByName(String userName);
    Optional<UserDto> deleteByUsername(String userName);
    Optional<UserDto> updateUserByUserName(String userName, EditUserRequestBody editUserRequestBody, MessageService messageService);
    boolean createUser(UserDto userDto);
}
