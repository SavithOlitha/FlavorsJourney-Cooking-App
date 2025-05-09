package com.example.cookingapp.controller;

import com.example.cookingapp.model.Role;
import com.example.cookingapp.model.User;
import com.example.cookingapp.payload.JwtResponse;
import com.example.cookingapp.payload.LoginRequest;
import com.example.cookingapp.payload.SignupRequest;
import com.example.cookingapp.repository.UserRepository;
import com.example.cookingapp.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // User Registration Endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        // Check if the email is already in use
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Create a new user with ROLE_USER
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(new HashSet<>(Collections.singleton(Role.ROLE_USER)));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(user);
    }

    // User Authentication Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Find the user by email
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Error: User not found!");
        }

        // Validate the password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Invalid password!");
        }

        // If authentication is successful, generate a JWT token
        String token = jwtUtils.generateJwtToken(user.getEmail());
        JwtResponse jwtResponse = new JwtResponse(token, user.getUsername(), user.getEmail());
        return ResponseEntity.ok(jwtResponse);
    }
}
