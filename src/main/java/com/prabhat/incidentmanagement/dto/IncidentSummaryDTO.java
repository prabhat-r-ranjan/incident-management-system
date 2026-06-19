package com.prabhat.incidentmanagement.dto;

import com.prabhat.incidentmanagement.entity.Incident;
import java.time.LocalDateTime;

public class IncidentSummaryDTO {
    private Long id;
    private String title;
    private String severity;
    private String status;
    private LocalDateTime createdAt;
    
    public static IncidentSummaryDTO fromEntity(Incident incident) {
        IncidentSummaryDTO dto = new IncidentSummaryDTO();
        dto.setId(incident.getId());
        dto.setTitle(incident.getTitle());
        dto.setSeverity(incident.getSeverity());
        dto.setStatus(incident.getStatus());
        dto.setCreatedAt(incident.getCreatedAt());
        return dto;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}