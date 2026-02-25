package com.pueblahealth.back.service;

import com.pueblahealth.back.model.SecurityLog;
import com.pueblahealth.back.repository.SecurityLogRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityLogService {

    private final SecurityLogRepository repository;

    public SecurityLogService(SecurityLogRepository repository) {
        this.repository = repository;
    }

    public void log(String email, String eventType, String ip, String description) {
        SecurityLog log = new SecurityLog(email, eventType, ip, description);
        repository.save(log);
    }
}