package com.example.springproject.data.mapper;


import com.example.springproject.data.User;
import com.example.springproject.entity.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UsersMapper {
    public static List<User> map(List<UserDto> users) {
        return users.stream().map(userDto -> User.builder()
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .build()).collect(Collectors.toList());
    }
}
