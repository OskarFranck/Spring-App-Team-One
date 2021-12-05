package com.example.springproject.request_body;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class EditUserRequestBody {
    String username;
    String email;
    String password;
    String usernameFromToken;
    Integer choice;
    Boolean access;
}
