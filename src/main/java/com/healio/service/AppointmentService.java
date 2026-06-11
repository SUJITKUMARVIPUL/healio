package com.healio.service;


import com.healio.dto.AppointmentNotificationEvent;
import com.healio.dto.AppointmentResponseDto;
import com.healio.dto.AppointmentStatus;
import com.healio.dto.BookingRequest;
import com.healio.entity.Appointment;
import com.healio.entity.AvailabilitySlot;
import com.healio.entity.DoctorProfile;
import com.healio.entity.User;
import com.healio.repository.AppointmentRepository;
import com.healio.repository.AvailabilitySlotRepository;
import com.healio.repository.DoctorProfileRepository;
import com.healio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final NotificationProducerService notificationProducerService;

    @Transactional
    public AppointmentResponseDto bookAppointment(String patientEmail, BookingRequest request) {
        // 1. Identify the Patient logged in
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Patient account not found"));

        // 2. Identify the Doctor profile
        DoctorProfile doctorProfile = doctorProfileRepository.findById(request.getDoctorProfileId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found"));

        // 3. Find and check the availability slot
        AvailabilitySlot slot = availabilitySlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Target availability slot not found"));

        if (slot.isBooked()) {
            throw new IllegalStateException("This time slot has already been booked by another patient!");
        }

        // 4. Update the slot status to claimed
        slot.setBooked(true);
        availabilitySlotRepository.save(slot);

        // 5. Create and persist the formal booking details
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctorProfile(doctorProfile)
                .availabilitySlot(slot)
                .status(AppointmentStatus.CONFIRMED) // Auto-confirmed for simplicity
                .symptoms(request.getSymptoms())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentResponseDto responseDto = mapToResponseDto(savedAppointment);

        // 🔥 NEW: Trigger Asynchronous Kafka Event
        AppointmentNotificationEvent notificationEvent = AppointmentNotificationEvent.builder()
                .appointmentId(savedAppointment.getId())
                .patientEmail(patient.getEmail())
                .doctorName("Dr. " + doctorProfile.getUser().getLastName())
                .appointmentDate(slot.getDate().toString())
                .startTime(slot.getStartTime().toString())
                .build();

        notificationProducerService.sendNotificationEvent(notificationEvent);

        return responseDto;
//        return mapToResponseDto(savedAppointment); //it without Kafka
    }

    public List<AppointmentResponseDto> getPatientAppointments(String email) {
        User patient = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return appointmentRepository.findByPatientId(patient.getId()).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private AppointmentResponseDto mapToResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getId())
                .doctorName("Dr. " + appointment.getDoctorProfile().getUser().getFirstName() + " " + appointment.getDoctorProfile().getUser().getLastName())
                .patientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName())
                .appointmentDate(appointment.getAvailabilitySlot().getDate())
                .startTime(appointment.getAvailabilitySlot().getStartTime())
                .status(appointment.getStatus())
                .symptoms(appointment.getSymptoms())
                .build();
    }
}
