package com.prabhat.incidentmanagement.dto;

import com.prabhat.incidentmanagement.entity.Problem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String severity;
    private String status;
    private String rootCause;
    private String workaround;
    private String resolution;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private List<IncidentSummaryDTO> relatedIncidents;
    private int incidentCount;
    
    public static ProblemResponseDTO fromEntity(Problem problem) {
        ProblemResponseDTO dto = new ProblemResponseDTO();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setSeverity(problem.getSeverity());
        dto.setStatus(problem.getStatus());
        dto.setRootCause(problem.getRootCause());
        dto.setWorkaround(problem.getWorkaround());
        dto.setResolution(problem.getResolution());
        dto.setAssignedTo(problem.getAssignedTo());
        dto.setCreatedAt(problem.getCreatedAt());
        dto.setUpdatedAt(problem.getUpdatedAt());
        dto.setResolvedAt(problem.getResolvedAt());
        
        if (problem.getRelatedIncidents() != null) {
            dto.setRelatedIncidents(
                problem.getRelatedIncidents().stream()
                    .map(IncidentSummaryDTO::fromEntity)
                    .collect(Collectors.toList())
            );
            dto.setIncidentCount(problem.getRelatedIncidents().size());
        }
        
        return dto;
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
    
    public List<IncidentSummaryDTO> getRelatedIncidents() { return relatedIncidents; }
    public void setRelatedIncidents(List<IncidentSummaryDTO> relatedIncidents) { this.relatedIncidents = relatedIncidents; }
    
    public int getIncidentCount() { return incidentCount; }
    public void setIncidentCount(int incidentCount) { this.incidentCount = incidentCount; }
}
