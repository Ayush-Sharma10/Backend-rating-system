package com.rating.rating_system;

import com.rating.rating_system.dto.LoginRequest;
import com.rating.rating_system.model.User;
import com.rating.rating_system.repository.UserRepository;
import com.rating.rating_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
// we set a base url for all endpoints in this controller
@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository; // db operations k liye user repository inject krre h

    @Autowired
    private JwtUtil jwtUtil; // tokens k liye user jwtUtil inject krre h

    // passwordEncoder bna rhe h taaki pass ko hash kr ske db mai store krne s phle
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 🚀 Register New User or Admin
    @PostMapping("/register")
    // ResponseEntity ek Spring object hai jo use hoti h entire HTTP response k liye
    public ResponseEntity<String> register(@RequestBody User user) { //@RequestBody automatically maps the incoming JSON to a User object
        // check kro email already toh exist n krt, krti h toh error dedo
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("❌ Email already registered!");
        }

        // Validate role
        String role = user.getRole();
        if (role == null || (!role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("ADMIN"))) {
            return ResponseEntity.badRequest().body("❌ Invalid role! Use 'USER' or 'ADMIN'");
        }

        // Hash krdo pass ko
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Role ko Upper case krdo
        user.setRole(role.toUpperCase());

        // Save user
        userRepository.save(user);
        // ResponseEntity is a special object provided by Spring that helps you control the entire HTTP response
        // — not just the body, but also the status code, headers
        return ResponseEntity.ok("✅ " + user.getRole() + " registered successfully! Welcome to the team");
    }

    // 🔐 Login and return JWT tokennnn
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Login request received for email: " + request.getEmail());

        //Optional is a Java feature that helps avoid NullPointerException.
        // optional acts as a container
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(404).body("❌ User not found!");
        }

        User user = optionalUser.get();

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("❌ Invalid password!");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());
        System.out.println("JWT generated for user: " + user.getEmail());

        // Return token
        return ResponseEntity.ok("✅ Login successful! Your token: " + token);
    }

    // ✅ Secured profile endpoint
    @GetMapping("/secure/profile")
    public ResponseEntity<String> getProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok("🔐 Welcome " + email + "! This is your secure profile.");
    }
}
