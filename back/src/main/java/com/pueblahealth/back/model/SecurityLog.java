package com.pueblahealth.back.model;

import jakarta.persistence.*;

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

    public SecurityLog() {}
}
