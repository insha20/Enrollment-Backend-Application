package com.springboot.Backend.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.springboot.Backend.Models.*;
import com.springboot.Backend.auth.AuthenticationRequest;
import com.springboot.Backend.auth.AuthenticationResponse;
import com.springboot.Backend.auth.AuthenticationService;
import com.springboot.Backend.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.springboot.Backend.Models.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AppControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AppController appController;

    @Mock
    private UserRepository userRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private EnrolledUserRepository enrolledUserRepository;

    @Mock
    private AuthenticationService authenticationService;


    @BeforeEach
    public void setup() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(USER);

        when(userDetailsService.loadUserByUsername("test@example.com"))
                .thenReturn(new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), user.getAuthorities()));

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // Test for welcome endpoint
    @Test
    public void testWelcome() throws Exception {
        mockMvc.perform(get("/api/v1/auth/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Welcome to my application"));
    }

    // Test for register endpoint
    @Test
    public void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest("test@email.com", "password");
        AuthenticationResponse response = new AuthenticationResponse("token");

        when(authenticationService.register(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@email.com\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token"));
    }

    // Test for authenticate endpoint
    @Test
    public void testAuthenticate() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password");
        AuthenticationResponse response = new AuthenticationResponse("token");

        when(authenticationService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@email.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testGetUsers() throws Exception {
        // Create sample data
        User user = new User();
        user.setEmail("test@example.com");

        EnrollUser enrollUser1 = new EnrollUser(1L, "John Doe", "123456", "test_photo1", user);
        EnrollUser enrollUser2 = new EnrollUser(2L, "Jane Doe", "234567", "test_photo2", user);
        List<EnrollUser> enrollUserList = Arrays.asList(enrollUser1, enrollUser2);

        // Set up mock repository behavior
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(enrolledUserRepository.findByCreatedBy(user)).thenReturn(enrollUserList);

        // Perform request and validate response
        mockMvc.perform(get("/api/v1/auth/enroll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("John Doe"))
                .andExpect(jsonPath("$[0].idNumber").value("123456"))
                .andExpect(jsonPath("$[1].username").value("Jane Doe"))
                .andExpect(jsonPath("$[1].idNumber").value("234567"));
    }

    @Test
    public void testAddUser() throws IOException {
        // Prepare test data
        User testUser = new User("test@example.com", "password", Role.USER);
        testUser.setId(1);
        EnrollUser testEnrollUser = new EnrollUser(1L, "testUser", "ID12345", null, testUser);
        MultipartFile testFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test-image".getBytes());

        // Mock dependencies
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail(authentication.getName())).thenReturn(Optional.of(testUser));
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(enrolledUserRepository.save(any(EnrollUser.class))).thenReturn(testEnrollUser);

        // Call the addUser method
        ResponseEntity<Object> response = appController.addUser(testEnrollUser, testFile, userDetails);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEnrollUser, response.getBody());
    }
}
