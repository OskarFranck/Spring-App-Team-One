package com.example.springproject.service;

import com.example.springproject.entity.UserDto;
import com.example.springproject.repo.UserRepository;
import com.example.springproject.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
