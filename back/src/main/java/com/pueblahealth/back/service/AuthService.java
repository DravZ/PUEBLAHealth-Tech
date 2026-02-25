package com.pueblahealth.back.service;

import com.pueblahealth.back.dto.AuthResponse;
import com.pueblahealth.back.dto.UserResponse;
import com.pueblahealth.back.exception.AccountLockedException;
import com.pueblahealth.back.exception.InvalidCredentialsException;
import com.pueblahealth.back.exception.UserAlreadyExistsException;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogService securityLogService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SecurityLogService securityLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityLogService = securityLogService;
    }
    public UserResponse register(String email, String password, HttpServletRequest request) {

        logger.info("Intento de registro con email: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Intento de registro con email ya existente: {}", email);
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole("MEDICO");
        user.setFailedAttempts(0);
        user.setAccountLocked(false);

        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado correctamente: {}", email);

        securityLogService.log(
                email,
                "USER_REGISTER",
                request.getRemoteAddr(),
                "Cuenta bloqueada por múltiples intentos fallidos"
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public UserResponse login(String email, String password, HttpServletRequest request) {
        logger.info("Intento de login para el usuario: {}", email);
        String ip = request.getRemoteAddr();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Intento de login con usuario inexistente: {}", email);
                    return new InvalidCredentialsException("Credenciales inválidas");
                });

        if (user.isAccountLocked()) {
            logger.error("Cuenta bloqueada intentó iniciar sesión: {}", email);
            throw new AccountLockedException("La cuenta está bloqueada por múltiples intentos fallidos");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);
            logger.warn("Intento fallido #{} para el usuario {}",
                    user.getFailedAttempts(), email);


            securityLogService.log(
                    email,
                    "LOGIN_FAILED",
                    ip,
                    "Intento de login fallido"
            );

            if (user.getFailedAttempts() >= 5) {
                user.setAccountLocked(true);
                logger.error("Cuenta bloqueada por múltiples intentos fallidos: {}", email);

                securityLogService.log(
                        email,
                        "ACCOUNT_LOCKED",
                        ip,
                        "Cuenta bloqueada por múltiples intentos fallidos"
                );
            }

            userRepository.save(user);

            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        user.setFailedAttempts(0);
        userRepository.save(user);

        logger.info("Login exitoso del usuario: {}", email);
        securityLogService.log(
                email,
                "LOGIN_SUCCESS",
                ip,
                "Inicio de sesión exitoso"
        );


        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}