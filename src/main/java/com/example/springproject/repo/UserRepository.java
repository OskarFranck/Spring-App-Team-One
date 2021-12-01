package com.example.springproject.repo;

import com.example.springproject.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Long> {
    Optional<UserDto> findByUserName(String userName);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> queryUserDtoById(Long id);
}
