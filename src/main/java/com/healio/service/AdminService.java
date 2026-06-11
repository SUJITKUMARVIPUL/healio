package com.healio.service;
import com.healio.dto.AdminDashboardStatsDto;
import com.healio.dto.ApprovalRequest;
import com.healio.entity.DoctorProfile;
import com.healio.repository.AppointmentRepository;
import com.healio.repository.DoctorProfileRepository;
import com.healio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public String reviewDoctorProfile(ApprovalRequest request) {
        DoctorProfile profile = doctorProfileRepository.findById(request.getDoctorProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile profile not found"));

        profile.setApproved(request.isApprove());
        doctorProfileRepository.save(profile);

        return request.isApprove() ? "Doctor has been successfully approved!" : "Doctor approval was rejected.";
    }

    public List<DoctorProfile> getPendingDoctors() {
        return doctorProfileRepository.findAll().stream()
                .filter(profile -> !profile.isApproved())
                .collect(Collectors.toList());
    }

    public AdminDashboardStatsDto getPlatformStats() {
        long totalUsers = userRepository.count();
        long totalAppointments = appointmentRepository.count();
        long totalDoctors = doctorProfileRepository.count();

        return AdminDashboardStatsDto.builder()
                .totalDoctors(totalDoctors)
                .totalAppointments(totalAppointments)
                .totalPatients(totalUsers - totalDoctors) // Rough estimation for dashboard overview
                .build();
    }
}
