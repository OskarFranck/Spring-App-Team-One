package com.example.springproject.repo;

import com.example.springproject.data.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Long> {
    boolean findByEmail(String email);
    UserDto findByUserName(String userName);
}
