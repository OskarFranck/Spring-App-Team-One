package com.example.springproject.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users", schema = "spring_base")
public class UserDto {
    @Id
    @GeneratedValue
    int id;
    private String email;
    private String forename;
    private String surname;
    private String hashed_password;
}
