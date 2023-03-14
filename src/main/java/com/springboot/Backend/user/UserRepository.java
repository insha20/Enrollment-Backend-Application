package com.springboot.Backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
}
