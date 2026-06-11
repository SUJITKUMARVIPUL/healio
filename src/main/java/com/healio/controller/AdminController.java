package com.healio.controller;

import com.healio.dto.AdminDashboardStatsDto;
import com.healio.dto.ApprovalRequest;
import com.healio.entity.DoctorProfile;
import com.healio.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") // Enforces security across every endpoint in this file
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Approve or reject pending specialist accounts
    @PostMapping("/verify-doctor")
    public ResponseEntity<String> verifyDoctor(@Valid @RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(adminService.reviewDoctorProfile(request));
    }

    // Pull list of doctors awaiting approval confirmation
    @GetMapping("/pending-doctors")
    public ResponseEntity<List<DoctorProfile>> getPendingDoctors() {
        return ResponseEntity.ok(adminService.getPendingDoctors());
    }

    // Fetch system data summaries
    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStatsDto> getPlatformStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }
}
