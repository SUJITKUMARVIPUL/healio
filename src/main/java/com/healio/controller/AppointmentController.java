package com.healio.controller;

import com.healio.dto.AppointmentResponseDto;
import com.healio.dto.BookingRequest;
import com.healio.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Secure endpoint: Only logged-in patients can trigger a booking sequence
    @PostMapping("/book")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BookingRequest request) {

        AppointmentResponseDto response = appointmentService.bookAppointment(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    // Patient views their personal itinerary of upcoming medical visits
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(appointmentService.getPatientAppointments(userDetails.getUsername()));
    }
}
