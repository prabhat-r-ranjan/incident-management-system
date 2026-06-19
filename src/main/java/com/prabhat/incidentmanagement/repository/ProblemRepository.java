package com.prabhat.incidentmanagement.repository;

import com.prabhat.incidentmanagement.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    
    // Find by status
    List<Problem> findByStatus(String status);
    
    // Find by severity
    List<Problem> findBySeverity(String severity);
    
    // Find by assignedTo
    List<Problem> findByAssignedTo(String assignedTo);
    
    // Search by title containing text
    List<Problem> findByTitleContainingIgnoreCase(String title);
    
    // Count by status
    long countByStatus(String status);
    
    // Count by severity
    long countBySeverity(String severity);
    
    // Get open problems count by severity
    @Query("SELECT COUNT(p) FROM Problem p WHERE p.status = 'OPEN' AND p.severity = :severity")
    long countOpenBySeverity(@Param("severity") String severity);
    
    // Get problems with related incidents count
    @Query("SELECT p, SIZE(p.relatedIncidents) FROM Problem p")
    List<Object[]> findProblemsWithIncidentCount();
    
    // Find problems by related incident
    @Query("SELECT p FROM Problem p WHERE :incidentId MEMBER OF p.relatedIncidents")
    List<Problem> findByRelatedIncidentId(@Param("incidentId") Long incidentId);
}