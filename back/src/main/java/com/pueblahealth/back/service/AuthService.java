package com.pueblahealth.back.service;

import com.pueblahealth.back.dto.AuthResponse;
import com.pueblahealth.back.dto.UserResponse;
import com.pueblahealth.back.exception.AccountLockedException;
import com.pueblahealth.back.exception.InvalidCredentialsException;
import com.pueblahealth.back.exception.UserAlreadyExistsException;
import com.pueblahealth.back.model.OtpCode;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.repository.OtpCodeRepository;
import com.pueblahealth.back.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityLogService securityLogService;
    private final EmailService emailService;
    private final OtpCodeRepository otpCodeRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SecurityLogService securityLogService, OtpCodeRepository otpCodeRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityLogService = securityLogService;
        this.otpCodeRepository = otpCodeRepository;
        this.emailService = emailService;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public UserResponse register(String email, String password, HttpServletRequest request) {

        logger.info("[INFO]Intento de registro con email: {}", email);

        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("[WARN]Intento de registro con email ya existente: {}", email);
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
        logger.info("[SUCCESS]Usuario registrado correctamente: {}", email);

        securityLogService.log(
                email,
                "CRITICAL",
                request.getRemoteAddr(),
                "Cuenta bloqueada por múltiples intentos fallidos"
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    @Transactional
    public UserResponse login(String email, String password, HttpServletRequest request) {
        logger.info("[INFO]Intento de login para el usuario: {}", email);
        String ip = request.getRemoteAddr();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("[WARN]Intento de login con usuario inexistente: {}", email);
                    return new InvalidCredentialsException("Credenciales inválidas");
                });

        if (user.isAccountLocked()) {
            logger.error("[CRITICAL]Cuenta bloqueada intentó iniciar sesión: {}", email);
            throw new AccountLockedException("La cuenta está bloqueada por múltiples intentos fallidos");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            user.setFailedAttempts(user.getFailedAttempts() + 1);
            logger.warn("[WARN]Intento fallido #{} para el usuario {}",
                    user.getFailedAttempts(), email);


            securityLogService.log(
                    email,
                    "ERROR",
                    ip,
                    "Intento de login fallido"
            );

            if (user.getFailedAttempts() >= 5) {
                user.setAccountLocked(true);
                logger.error("[CRITICAL]Cuenta bloqueada por múltiples intentos fallidos: {}", email);

                securityLogService.log(
                        email,
                        "CRITICAL",
                        ip,
                        "Cuenta bloqueada por múltiples intentos fallidos"
                );
            }

            userRepository.save(user);

            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        user.setFailedAttempts(0);
        userRepository.save(user);

        logger.info("[SUCCESS]Login exitoso del usuario: {}", email);
        securityLogService.log(
                email,
                "SUCCESS",
                ip,
                "Inicio de sesión exitoso"
        );

        String otp = generateOtp();

        String hashedOtp = passwordEncoder.encode(otp);

        OtpCode otpCode = new OtpCode(
                email,
                hashedOtp,
                LocalDateTime.now().plusMinutes(2)
        );

        deleteOldOtp(email);

        otpCodeRepository.save(otpCode);

        emailService.sendOtp(email, otp);



        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Transactional
    public void deleteOldOtp(String email){
        otpCodeRepository.deleteByEmail(email);
    }

    public String verifyOtp(String email, String otp) {

        OtpCode otpCode = otpCodeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP inválido"));

        if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expirado");
        }

        if(!passwordEncoder.matches(otp, otpCode.getCode())){
            throw new RuntimeException("OTP incorrecto");
        }

        return "Autenticación exitosa";
    }
}