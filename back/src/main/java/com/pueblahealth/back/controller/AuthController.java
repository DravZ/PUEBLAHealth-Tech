package com.pueblahealth.back.controller;

import com.pueblahealth.back.dto.RegisterRequest;
import com.pueblahealth.back.dto.UserResponse;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request.getEmail(), request.getPassword());
    }
}