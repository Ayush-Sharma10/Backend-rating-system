//package com.rating.rating_system.controller;
//
//import com.rating.rating_system.model.User;
//import com.rating.rating_system.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users") // Changed base path from /api to /api/users
//public class UserController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/register") // Final path: /api/users/register
//    public String register(@RequestBody User user) {
//        System.out.println("Incoming request: " + user.getName() + ", " + user.getEmail());
//
//        // Check if email is already used
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return "Email already exists!";
//        }
//        // Set default role and save user
//        user.setRole("USER");
//        userRepository.save(user);
//
//        return "User registered successfully!";
//    }
//}
