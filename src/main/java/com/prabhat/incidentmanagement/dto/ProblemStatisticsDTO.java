package com.prabhat.incidentmanagement.dto;

import java.util.Map;

public class ProblemStatisticsDTO {
    private long totalProblems;
    private long openProblems;
    private long investigatingProblems;
    private long knownErrorProblems;
    private long resolvedProblems;
    private long closedProblems;
    private Map<String, Long> bySeverity;
    private Map<String, Long> openBySeverity;
    
    // Getters and Setters
    public long getTotalProblems() { return totalProblems; }
    public void setTotalProblems(long totalProblems) { this.totalProblems = totalProblems; }
    
    public long getOpenProblems() { return openProblems; }
    public void setOpenProblems(long openProblems) { this.openProblems = openProblems; }
    
    public long getInvestigatingProblems() { return investigatingProblems; }
    public void setInvestigatingProblems(long investigatingProblems) { this.investigatingProblems = investigatingProblems; }
    
    public long getKnownErrorProblems() { return knownErrorProblems; }
    public void setKnownErrorProblems(long knownErrorProblems) { this.knownErrorProblems = knownErrorProblems; }
    
    public long getResolvedProblems() { return resolvedProblems; }
    public void setResolvedProblems(long resolvedProblems) { this.resolvedProblems = resolvedProblems; }
    
    public long getClosedProblems() { return closedProblems; }
    public void setClosedProblems(long closedProblems) { this.closedProblems = closedProblems; }
    
    public Map<String, Long> getBySeverity() { return bySeverity; }
    public void setBySeverity(Map<String, Long> bySeverity) { this.bySeverity = bySeverity; }
    
    public Map<String, Long> getOpenBySeverity() { return openBySeverity; }
    public void setOpenBySeverity(Map<String, Long> openBySeverity) { this.openBySeverity = openBySeverity; }
}