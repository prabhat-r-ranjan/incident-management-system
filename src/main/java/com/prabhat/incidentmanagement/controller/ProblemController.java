package com.prabhat.incidentmanagement.controller;

import com.prabhat.incidentmanagement.dto.CreateProblemDTO;
import com.prabhat.incidentmanagement.dto.ProblemResponseDTO;
import com.prabhat.incidentmanagement.dto.ProblemStatisticsDTO;
import com.prabhat.incidentmanagement.entity.Incident;
import com.prabhat.incidentmanagement.entity.Problem;
import com.prabhat.incidentmanagement.repository.IncidentRepository;
import com.prabhat.incidentmanagement.repository.ProblemRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "http://localhost:3000")
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final IncidentRepository incidentRepository;

    public ProblemController(ProblemRepository problemRepository, IncidentRepository incidentRepository) {
        this.problemRepository = problemRepository;
        this.incidentRepository = incidentRepository;
    }

    // ========== CREATE ==========
    @PostMapping
    public ResponseEntity<ProblemResponseDTO> create(@Valid @RequestBody CreateProblemDTO dto) {
        // Validate severity
        if (dto.getSeverity() == null || 
            !List.of("P0", "P1", "P2", "P3", "P4").contains(dto.getSeverity())) {
            throw new IllegalArgumentException("Invalid severity. Use P0, P1, P2, P3, P4");
        }
        
        Problem problem = new Problem();
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setSeverity(dto.getSeverity());
        problem.setStatus("OPEN");
        problem.setRootCause(dto.getRootCause());
        problem.setWorkaround(dto.getWorkaround());
        problem.setAssignedTo(dto.getAssignedTo());
        problem.setCreatedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());
        
        // Link related incidents if provided
        if (dto.getRelatedIncidentIds() != null && !dto.getRelatedIncidentIds().isEmpty()) {
            List<Incident> incidents = incidentRepository.findAllById(dto.getRelatedIncidentIds());
            problem.getRelatedIncidents().addAll(incidents);
        }
        
        Problem savedProblem = problemRepository.save(problem);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProblemResponseDTO.fromEntity(savedProblem));
    }

    // ========== CONVERT INCIDENT TO PROBLEM ==========
    @PostMapping("/convert/{incidentId}")
    public ResponseEntity<ProblemResponseDTO> convertFromIncident(@PathVariable Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + incidentId));
        
        Problem problem = new Problem();
        problem.setTitle("Root Cause: " + incident.getTitle());
        problem.setDescription(incident.getDescription());
        problem.setSeverity(incident.getSeverity());
        problem.setStatus("OPEN");
        problem.setAssignedTo(incident.getAssignedTo());
        problem.setCreatedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());
        problem.getRelatedIncidents().add(incident);
        
        Problem savedProblem = problemRepository.save(problem);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProblemResponseDTO.fromEntity(savedProblem));
    }

    // ========== READ - Get All ==========
    @GetMapping
    public List<ProblemResponseDTO> getAll() {
        return problemRepository.findAll().stream()
                .map(ProblemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ========== READ - Get One ==========
    @GetMapping("/{id}")
    public ProblemResponseDTO getById(@PathVariable Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        return ProblemResponseDTO.fromEntity(problem);
    }

    // ========== UPDATE ==========
    @PutMapping("/{id}")
    public ProblemResponseDTO update(@PathVariable Long id, @RequestBody CreateProblemDTO dto) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setSeverity(dto.getSeverity());
        problem.setRootCause(dto.getRootCause());
        problem.setWorkaround(dto.getWorkaround());
        problem.setAssignedTo(dto.getAssignedTo());
        problem.setUpdatedAt(LocalDateTime.now());
        
        // Update related incidents
        if (dto.getRelatedIncidentIds() != null) {
            List<Incident> incidents = incidentRepository.findAllById(dto.getRelatedIncidentIds());
            problem.getRelatedIncidents().clear();
            problem.getRelatedIncidents().addAll(incidents);
        }
        
        Problem updatedProblem = problemRepository.save(problem);
        return ProblemResponseDTO.fromEntity(updatedProblem);
    }

    // ========== UPDATE STATUS ==========
    @PatchMapping("/{id}/status")
    public ProblemResponseDTO updateStatus(@PathVariable Long id, @RequestParam String status) {
        if (!List.of("OPEN", "INVESTIGATING", "KNOWN_ERROR", "RESOLVED", "CLOSED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        
        problem.setStatus(status);
        problem.setUpdatedAt(LocalDateTime.now());
        
        if ("RESOLVED".equals(status) || "CLOSED".equals(status)) {
            problem.setResolvedAt(LocalDateTime.now());
        }
        
        Problem updatedProblem = problemRepository.save(problem);
        return ProblemResponseDTO.fromEntity(updatedProblem);
    }

    // ========== RESOLVE PROBLEM ==========
    @PatchMapping("/{id}/resolve")
    public ProblemResponseDTO resolve(@PathVariable Long id, @RequestParam String resolution) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        
        problem.setStatus("RESOLVED");
        problem.setResolution(resolution);
        problem.setResolvedAt(LocalDateTime.now());
        problem.setUpdatedAt(LocalDateTime.now());
        
        Problem updatedProblem = problemRepository.save(problem);
        return ProblemResponseDTO.fromEntity(updatedProblem);
    }

    // ========== LINK INCIDENT TO PROBLEM ==========
    @PostMapping("/{problemId}/link/{incidentId}")
    public ProblemResponseDTO linkIncident(@PathVariable Long problemId, @PathVariable Long incidentId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + problemId));
        
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + incidentId));
        
        problem.addRelatedIncident(incident);
        problem.setUpdatedAt(LocalDateTime.now());
        
        Problem updatedProblem = problemRepository.save(problem);
        return ProblemResponseDTO.fromEntity(updatedProblem);
    }

    // ========== UNLINK INCIDENT ==========
    @DeleteMapping("/{problemId}/unlink/{incidentId}")
    public ProblemResponseDTO unlinkIncident(@PathVariable Long problemId, @PathVariable Long incidentId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + problemId));
        
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found with id: " + incidentId));
        
        problem.removeRelatedIncident(incident);
        problem.setUpdatedAt(LocalDateTime.now());
        
        Problem updatedProblem = problemRepository.save(problem);
        return ProblemResponseDTO.fromEntity(updatedProblem);
    }

    // ========== GET RELATED INCIDENTS ==========
    @GetMapping("/{problemId}/incidents")
    public List<Incident> getRelatedIncidents(@PathVariable Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + problemId));
        return problem.getRelatedIncidents();
    }

    // ========== DELETE ==========
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + id));
        problemRepository.delete(problem);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Problem deleted successfully");
        response.put("id", String.valueOf(id));
        return response;
    }

    // ========== STATISTICS ==========
    @GetMapping("/stats")
    public ProblemStatisticsDTO getStatistics() {
        ProblemStatisticsDTO stats = new ProblemStatisticsDTO();
        
        // Basic counts
        stats.setTotalProblems(problemRepository.count());
        stats.setOpenProblems(problemRepository.countByStatus("OPEN"));
        stats.setInvestigatingProblems(problemRepository.countByStatus("INVESTIGATING"));
        stats.setKnownErrorProblems(problemRepository.countByStatus("KNOWN_ERROR"));
        stats.setResolvedProblems(problemRepository.countByStatus("RESOLVED"));
        stats.setClosedProblems(problemRepository.countByStatus("CLOSED"));
        
        // Severity breakdown
        Map<String, Long> severityStats = new HashMap<>();
        severityStats.put("P0", problemRepository.countBySeverity("P0"));
        severityStats.put("P1", problemRepository.countBySeverity("P1"));
        severityStats.put("P2", problemRepository.countBySeverity("P2"));
        severityStats.put("P3", problemRepository.countBySeverity("P3"));
        severityStats.put("P4", problemRepository.countBySeverity("P4"));
        stats.setBySeverity(severityStats);
        
        // Open by severity
        Map<String, Long> openBySeverity = new HashMap<>();
        openBySeverity.put("P0", problemRepository.countOpenBySeverity("P0"));
        openBySeverity.put("P1", problemRepository.countOpenBySeverity("P1"));
        openBySeverity.put("P2", problemRepository.countOpenBySeverity("P2"));
        openBySeverity.put("P3", problemRepository.countOpenBySeverity("P3"));
        openBySeverity.put("P4", problemRepository.countOpenBySeverity("P4"));
        stats.setOpenBySeverity(openBySeverity);
        
        return stats;
    }

    // ========== SEARCH ==========
    @GetMapping("/search")
    public List<ProblemResponseDTO> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String assignedTo) {
        
        List<Problem> problems;
        
        if (status != null && severity != null) {
            // Both filters
            problems = problemRepository.findAll().stream()
                    .filter(p -> p.getStatus().equals(status) && p.getSeverity().equals(severity))
                    .collect(Collectors.toList());
        } else if (status != null) {
            problems = problemRepository.findByStatus(status);
        } else if (severity != null) {
            problems = problemRepository.findBySeverity(severity);
        } else if (title != null) {
            problems = problemRepository.findByTitleContainingIgnoreCase(title);
        } else if (assignedTo != null) {
            problems = problemRepository.findByAssignedTo(assignedTo);
        } else {
            problems = problemRepository.findAll();
        }
        
        return problems.stream()
                .map(ProblemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private static List<String> List(String... strings) {
        return java.util.Arrays.asList(strings);
    }
}