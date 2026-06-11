package com.prabhat.incidentmanagement.repository;

import com.prabhat.incidentmanagement.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    // Basic counts
    long countByStatus(String status);
    
    long countBySeverity(String severity);
    
    // Find by status
    List<Incident> findByStatus(String status);
    
    // Find by severity
    List<Incident> findBySeverity(String severity);
    
    // Search by title containing text
    List<Incident> findByTitleContainingIgnoreCase(String title);
    
    // Statistics queries - FIXED VERSIONS
    
    @Query("SELECT i.severity, COUNT(i) FROM Incident i GROUP BY i.severity")
    List<Object[]> getSeverityStatistics();
    
    // FIXED: Use native PostgreSQL query for average resolution time
    @Query(value = "SELECT COALESCE(AVG(EXTRACT(EPOCH FROM (resolved_at - created_at))/60), 0) FROM incidents WHERE resolved_at IS NOT NULL", nativeQuery = true)
    Double getAverageResolutionTimeMinutes();
    
    // Get open incidents count by severity
    @Query("SELECT COUNT(i) FROM Incident i WHERE i.status = 'OPEN' AND i.severity = :severity")
    long countOpenBySeverity(@Param("severity") String severity);
}