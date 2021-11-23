package com.example.springproject.data.mapper;

import com.example.springproject.data.User;
import com.example.springproject.data.UserDto;

public class UserMapper {
    public static User map(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .forename(userDto.getForename())
                .surname(userDto.getSurname())
                .build();
    }
}
