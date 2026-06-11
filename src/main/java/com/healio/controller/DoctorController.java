package com.healio.controller;

import com.healio.dto.DoctorProfileDto;
import com.healio.dto.SlotCreationRequest;
import com.healio.entity.AvailabilitySlot;
import com.healio.entity.DoctorProfile;
import com.healio.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Doctor updates their own business card details
    @PostMapping("/profile")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<DoctorProfile> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DoctorProfileDto dto) {
        return ResponseEntity.ok(doctorService.createOrUpdateProfile(userDetails.getUsername(), dto));
    }

    // Doctor creates open appointment blocks
    @PostMapping("/slots")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<AvailabilitySlot> addSlot(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SlotCreationRequest request) {
        return ResponseEntity.ok(doctorService.addAvailabilitySlot(userDetails.getUsername(), request));
    }

    // Open endpoint: Public or Patients searching doctors
    @GetMapping
    public ResponseEntity<List<DoctorProfile>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Open endpoint: Get unbooked timeslots for a specific doctor
    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<AvailabilitySlot>> getSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(doctorService.getAvailableSlots(doctorId, date));
    }
}
