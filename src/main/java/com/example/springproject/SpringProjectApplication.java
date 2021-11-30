package com.example.springproject;

import com.example.springproject.entity.UserDto;
import com.example.springproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringProjectApplication {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.addUser(new UserDto(null, "User1", "user@gmail.com", "pass", true));
//            userService.addUser(new UserDto(null, "User2", "user@gmail.com", "pass", true));
//            userService.addUser(new UserDto(null, "User3", "user@gmail.com", "pass", true));
//
//        };
//    }

}
