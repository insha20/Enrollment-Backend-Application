package com.springboot.Backend.controller;
import com.springboot.Backend.auth.AuthenticationRequest;
import com.springboot.Backend.auth.AuthenticationResponse;
import com.springboot.Backend.auth.AuthenticationService;
import com.springboot.Backend.auth.RegisterRequest;
import com.springboot.Backend.Models.EnrollUser;
import com.springboot.Backend.Models.EnrolledUserRepository;
import com.springboot.Backend.Models.User;
import com.springboot.Backend.Models.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AppController {

  private final AuthenticationService service;
  private final UserRepository userRepository;
  private final EnrolledUserRepository enrolledUserRepository;

  @CrossOrigin(origins = "http://127.0.0.1:5501")
  @GetMapping("/")
  public ResponseEntity<String> Welcome(){
    return ResponseEntity.ok("Welcome to my application");
  }

  @CrossOrigin(origins = "http://127.0.0.1:5501")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
  }



  @CrossOrigin(origins = "http://127.0.0.1:5501")
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(service.authenticate(request));
  }
  @CrossOrigin(origins = "http://127.0.0.1:5501")
  @GetMapping("/enroll")
  @ResponseBody
  public List<EnrollUser> getUsers() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
    return enrolledUserRepository.findByCreatedBy(user);
  }


  @CrossOrigin(origins = "http://127.0.0.1:5501")
  @PostMapping("/addUser")
  public ResponseEntity<Object> addUser(EnrollUser user, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
    if (!file.isEmpty()) {
      byte[] bytes = file.getBytes();
      String base64String = Base64.getEncoder().encodeToString(bytes);
      user.setPhoto(base64String);
    }

    User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
    user.setCreatedBy(currentUser);

    try {
      enrolledUserRepository.save(user);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      System.out.println("Error saving user: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving user");
    }
  }
}















