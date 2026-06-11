package com.prabhat.incidentmanagement.controller;

import com.prabhat.incidentmanagement.entity.Incident;
import com.prabhat.incidentmanagement.repository.IncidentRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:3000")
public class IncidentController {

    private final IncidentRepository repository;

    public IncidentController(IncidentRepository repository) {
        this.repository = repository;
    }

    // ========== CREATE ==========
    @PostMapping
    public Incident create(@Valid @RequestBody Incident incident) {
        // Validate severity
        if (incident.getSeverity() == null ||
                !List.of("P0", "P1", "P2", "P3", "P4").contains(incident.getSeverity())) {
            throw new IllegalArgumentException("Invalid severity. Use P0, P1, P2, P3, P4");
        }
        
        incident.setStatus("OPEN");
        incident.setCreatedAt(LocalDateTime.now());
        return repository.save(incident);
    }

    // ========== READ - Get All ==========
    @GetMapping
    public List<Incident> getAll() {
        return repository.findAll();
    }

    // ========== READ - Get One by ID ==========
    @GetMapping("/{id}")
    public Incident getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
    }

    // ========== UPDATE - Resolve Incident ==========
    @PutMapping("/{id}/resolve")
    public Incident resolve(@PathVariable Long id) {
        Incident incident = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        incident.setStatus("RESOLVED");
        incident.setResolvedAt(LocalDateTime.now());
        return repository.save(incident);
    }
    
    // ========== UPDATE - Acknowledge Incident ==========
    @PutMapping("/{id}/acknowledge")
    public Incident acknowledge(@PathVariable Long id, @RequestParam String assignedTo) {
        Incident incident = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        incident.setStatus("ACKNOWLEDGED");
        incident.setAssignedTo(assignedTo);
        return repository.save(incident);
    }
    
    // ========== UPDATE - Full Update ==========
    @PutMapping("/{id}")
    public Incident update(@PathVariable Long id, @RequestBody Incident incidentDetails) {
        Incident incident = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        
        incident.setTitle(incidentDetails.getTitle());
        incident.setDescription(incidentDetails.getDescription());
        incident.setSeverity(incidentDetails.getSeverity());
        
        return repository.save(incident);
    }

    // ========== DELETE ==========
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        Incident incident = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + id));
        repository.delete(incident);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Incident deleted successfully");
        response.put("id", String.valueOf(id));
        return response;
    }

    // ========== STATISTICS ==========
    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("totalIncidents", repository.count());
        stats.put("openIncidents", repository.countByStatus("OPEN"));
        stats.put("acknowledgedIncidents", repository.countByStatus("ACKNOWLEDGED"));
        stats.put("resolvedIncidents", repository.countByStatus("RESOLVED"));
        
        // Severity breakdown
        Map<String, Long> severityStats = new HashMap<>();
        severityStats.put("P0", repository.countBySeverity("P0"));
        severityStats.put("P1", repository.countBySeverity("P1"));
        severityStats.put("P2", repository.countBySeverity("P2"));
        severityStats.put("P3", repository.countBySeverity("P3"));
        severityStats.put("P4", repository.countBySeverity("P4"));
        stats.put("bySeverity", severityStats);
        
        // Open incidents by severity
        Map<String, Long> openBySeverity = new HashMap<>();
        openBySeverity.put("P0", repository.countOpenBySeverity("P0"));
        openBySeverity.put("P1", repository.countOpenBySeverity("P1"));
        openBySeverity.put("P2", repository.countOpenBySeverity("P2"));
        openBySeverity.put("P3", repository.countOpenBySeverity("P3"));
        openBySeverity.put("P4", repository.countOpenBySeverity("P4"));
        stats.put("openBySeverity", openBySeverity);
        
        // Average resolution time
        Double avgTime = repository.getAverageResolutionTimeMinutes();
        stats.put("averageResolutionTimeMinutes", avgTime != null ? Math.round(avgTime) : 0);
        
        stats.put("timestamp", LocalDateTime.now());
        
        return stats;
    }

    // ========== SEARCH ==========
    @GetMapping("/search")
    public List<Incident> search(@RequestParam(required = false) String status,
                                  @RequestParam(required = false) String severity,
                                  @RequestParam(required = false) String title) {
        if (status != null && severity != null) {
            // Both status and severity
            return repository.findAll().stream()
                    .filter(i -> i.getStatus().equals(status) && i.getSeverity().equals(severity))
                    .toList();
        } else if (status != null) {
            // Only status
            return repository.findByStatus(status);
        } else if (severity != null) {
            // Only severity
            return repository.findBySeverity(severity);
        } else if (title != null) {
            // Search by title
            return repository.findByTitleContainingIgnoreCase(title);
        } else {
            // No filters - return all
            return repository.findAll();
        }
    }
    
    // Helper method
    private static List<String> List(String... strings) {
        return java.util.Arrays.asList(strings);
    }
}