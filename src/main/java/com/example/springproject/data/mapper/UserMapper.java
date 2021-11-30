package com.example.springproject.data.mapper;

import com.example.springproject.data.User;
import com.example.springproject.entity.UserDto;

public class UserMapper {
    public static User map(UserDto userDto) {
        return User.builder()
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .build();
    }
}
