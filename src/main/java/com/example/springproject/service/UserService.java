package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> getUserByEmail(String email);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByName(String userName);
    Optional<UserDto> deleteById(Long id);

    List<UserDto> getAll();
    Optional<UserDto> updateUserByUserName(String userName, Integer choice, UserDto userDto);
    Optional<UserDto> createUser(UserDto userDto);
}
