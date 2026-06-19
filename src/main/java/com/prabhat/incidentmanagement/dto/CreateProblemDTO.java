package com.prabhat.incidentmanagement.dto;

import java.util.List;

public class CreateProblemDTO {
    private String title;
    private String description;
    private String severity;
    private String rootCause;
    private String workaround;
    private String assignedTo;
    private List<Long> relatedIncidentIds;
    
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    
    public String getWorkaround() { return workaround; }
    public void setWorkaround(String workaround) { this.workaround = workaround; }
    
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    
    public List<Long> getRelatedIncidentIds() { return relatedIncidentIds; }
    public void setRelatedIncidentIds(List<Long> relatedIncidentIds) { this.relatedIncidentIds = relatedIncidentIds; }
}