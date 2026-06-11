package com.prabhat.incidentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IncidentManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(IncidentManagementSystemApplication.class, args);
        System.out.println("✅ Incident Management System Started!");
        System.out.println("📝 API URL: http://localhost:8080/api/incidents");
    }
}