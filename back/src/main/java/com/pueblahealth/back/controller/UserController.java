package com.pueblahealth.back.controller;

import com.pueblahealth.back.dto.*;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.service.AuthService;
import com.pueblahealth.back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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


}