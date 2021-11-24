package com.example.springproject.response;

import com.example.springproject.data.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;
    private String email;

    public UserResponse(UserDto userDto) {

        this.username = userDto.getUserName();
        this.email = userDto.getEmail();

    }
}
