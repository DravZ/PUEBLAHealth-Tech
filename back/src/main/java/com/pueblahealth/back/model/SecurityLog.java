package com.pueblahealth.back.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_logs")
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String eventType;
    private String ipAddress;
    private String description;
    private LocalDateTime createdAt;

    public SecurityLog() {
        this.createdAt = LocalDateTime.now();
    }

    public SecurityLog(String email, String eventType, String ipAddress, String description) {
        this.email = email;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}

