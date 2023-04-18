package com.springboot.Backend.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrolledUserService {
    @Autowired
    private EnrolledUserRepository enrolledUserRepository;

    public List<EnrollUser> getAllEnrolledUsers(){
        return enrolledUserRepository.findAll();
    }

    public Optional<EnrollUser> getEnrolledUserById(Long id){
        return enrolledUserRepository.findById(id);
    }

    public EnrollUser saveEnrolledUser(EnrollUser appUser){
        return enrolledUserRepository.save(appUser);
    }

    public void deleteEnrolledUserById(Long id){
        enrolledUserRepository.deleteById(id);
    }
}
