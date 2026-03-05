package com.pueblahealth.back.repository;

import com.pueblahealth.back.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findByEmail(String email);

    void deleteByEmail(String email);
}