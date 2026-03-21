package com.pueblahealth.back.service;

import com.pueblahealth.back.dto.UserDetailsResponse;
import com.pueblahealth.back.exception.InvalidCredentialsException;
import com.pueblahealth.back.model.User;
import com.pueblahealth.back.repository.UserRepository;
import com.pueblahealth.back.utils.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Value("${AES_SECRET_KEY}")
    private String secretKey;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsResponse getUserById(Long id){
        User user = userRepository.findById(id) .orElseThrow(() -> {
            return new RuntimeException("Usuario con id " + id + "no encontrado.");
        });

        String decryptedCurp;
        try {
            decryptedCurp = AesUtil.decrypt(user.getCurp(), secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error descifrando CURP");
        }

        return new UserDetailsResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getNombre(),
                user.getApellidoPaterno(),
                user.getApellidoMaterno(),
                decryptedCurp
        );
    }
}
