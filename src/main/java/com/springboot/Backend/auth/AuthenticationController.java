package com.springboot.Backend.auth;

import com.springboot.Backend.user.User;
import com.springboot.Backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final UserRepository userRepository;

  @GetMapping("/")
  public String home() {
    return "home";
  }


  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    try {
      if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
      }
      AuthenticationResponse response = service.register(request);
      System.out.println("Successful");
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create("/api/v1/auth/register/"));
      headers.add("Message", "Created");
      return new ResponseEntity<>("Successful", headers, HttpStatus.CREATED);
    } catch (EmailAlreadyExistsException e) {
      HttpHeaders headers = new HttpHeaders();
      headers.setLocation(URI.create("/api/v1/auth/register/"));
      headers.add("Message", "Email already exists");
      return new ResponseEntity<>("Email already exists", headers, HttpStatus.CONFLICT);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }
}
