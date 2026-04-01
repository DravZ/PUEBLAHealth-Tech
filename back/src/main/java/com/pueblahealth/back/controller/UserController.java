package com.pueblahealth.back.controller;

import com.pueblahealth.back.dto.CurpRequest;
import com.pueblahealth.back.dto.ProfileResponse;
import com.pueblahealth.back.dto.UserDetailsResponse;
import com.pueblahealth.back.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDetailsResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 🔥 NUEVO ENDPOINT
    @PostMapping("/profile")
    public ProfileResponse getProfile(@RequestBody CurpRequest request) {
        return userService.getProfile(request);
    }
}