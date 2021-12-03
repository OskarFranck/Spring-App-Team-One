package com.example.springproject.request_body;

import com.example.springproject.entity.UserDto;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EditUserRequestBody {
    String userName;
    String userNameFromToken;
    Integer choice;
    UserDto user;
}
