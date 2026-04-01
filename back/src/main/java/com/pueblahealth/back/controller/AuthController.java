package com.pueblahealth.back.controller;

import com.pueblahealth.back.dto.LoginRequest;
import com.pueblahealth.back.dto.OtpRequest;
import com.pueblahealth.back.dto.RegisterRequest;
import com.pueblahealth.back.dto.UserResponse;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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
    public UserResponse register(@RequestBody RegisterRequest request,HttpServletRequest httpRequest) {
        return authService.register(request.getEmail(), request.getPassword(), request.getNombre(),
                request.getApellidoPaterno(), request.getApellidoMaterno(), request.getCurp(), httpRequest);
    }
    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return authService.login(request.getEmail(), request.getPassword(), request.getEmailIv(),
                request.getPasswordIv(), httpRequest);
    }

    @PostMapping("/verify-otp")
    public UserResponse verifyOtp(@RequestBody OtpRequest request) {

        return authService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );
    }

}