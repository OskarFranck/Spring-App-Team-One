package com.example.springproject.service;

import com.example.springproject.entity.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByName(String userName);
    Optional<UserDto> deleteById(Long id);

}
