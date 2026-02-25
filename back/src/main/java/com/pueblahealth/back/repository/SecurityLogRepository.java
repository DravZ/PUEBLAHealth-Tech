package com.pueblahealth.back.repository;

import com.pueblahealth.back.model.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {
}