package com.prabhat.incidentmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problems")
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String severity; // P0, P1, P2, P3, P4
    
    @Column(nullable = false)
    private String status; // OPEN, INVESTIGATING, KNOWN_ERROR, RESOLVED, CLOSED
    
    @Column(columnDefinition = "TEXT")
    private String rootCause;
    
    @Column(columnDefinition = "TEXT")
    private String workaround;
    
    @Column(columnDefinition = "TEXT")
    private String resolution;
    
    private String assignedTo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @ManyToMany
    @JoinTable(
        name = "problem_incidents",
        joinColumns = @JoinColumn(name = "problem_id"),
        inverseJoinColumns = @JoinColumn(name = "incident_id")
    )
    private List<Incident> relatedIncidents = new ArrayList<>();
    
    // Constructors
    public Problem() {}
    
    public Problem(String title, String description, String severity) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = "OPEN";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    
    public String getWorkaround() { return workaround; }
    public void setWorkaround(String workaround) { this.workaround = workaround; }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public List<Incident> getRelatedIncidents() { return relatedIncidents; }
    public void setRelatedIncidents(List<Incident> relatedIncidents) { this.relatedIncidents = relatedIncidents; }
    
    // Helper methods
    public void addRelatedIncident(Incident incident) {
        if (!this.relatedIncidents.contains(incident)) {
            this.relatedIncidents.add(incident);
        }
    }
    
    public void removeRelatedIncident(Incident incident) {
        this.relatedIncidents.remove(incident);
    }
    
    public boolean isValidSeverity() {
        return severity != null && List.of("P0", "P1", "P2", "P3", "P4").contains(severity);
    }
    
    public boolean isValidStatus() {
        return status != null && List.of("OPEN", "INVESTIGATING", "KNOWN_ERROR", "RESOLVED", "CLOSED").contains(status);
    }
}