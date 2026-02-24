package com.pueblahealth.back.service;

import com.pueblahealth.back.dto.UserResponse;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole("MEDICO");
        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public UserResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.isAccountLocked()) {
            throw new RuntimeException("Cuenta bloqueada");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);

            if (user.getFailedAttempts() >= 5) {
                user.setAccountLocked(true);
                user.setLockTime(System.currentTimeMillis());
            }

            userRepository.save(user);
            throw new RuntimeException("Credenciales incorrectas");
        }

        user.setFailedAttempts(0);
        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}