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

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

//        @Bean
//    CommandLineRunner run(UserService userService) {
//        return args -> {
//            userService.addUser(new UserDto(null,"user1","mail1@mail.com", "pass", true));
//            userService.addUser(new UserDto(null,"user2","mail2@mail.com", "pass", true));
//            userService.addUser(new UserDto(null,"user3","mail3@mail.com", "pass", true));
//            userService.addUser(new UserDto(null,"user4","mail4@mail.com", "pass", true));
//            userService.addUser(new UserDto(null,"user5","mail5@mail.com", "pass", false));
//            userService.addUser(new UserDto(null,"user6","mail6@mail.com", "pass", false));
//        };
//    } q

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
