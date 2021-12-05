package com.example.springproject.service;

import com.example.springproject.entity.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAll();
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByName(String userName);
    Optional<UserDto> deleteByUsername(String userName);
    Optional<UserDto> updateUserByUserName(String userName, Integer choice);
    Optional<UserDto> createUser(UserDto userDto);
}
