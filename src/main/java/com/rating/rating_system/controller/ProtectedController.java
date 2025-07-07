package com.rating.rating_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    @GetMapping("/protected")
    public String secureAccess() {
        return "🔐 This is a protected endpoint!";
    }
}
