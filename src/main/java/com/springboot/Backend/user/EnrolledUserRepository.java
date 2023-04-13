package com.springboot.Backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolledUserRepository extends JpaRepository<EnrollUser, Long> {
    List<EnrollUser> findByCreatedBy(User createdBy);
}


