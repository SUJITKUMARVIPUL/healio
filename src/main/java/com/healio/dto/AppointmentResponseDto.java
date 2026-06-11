package com.healio.dto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AppointmentResponseDto {
    private Long appointmentId;
    private String doctorName;
    private String patientName;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private AppointmentStatus status;
    private String symptoms;
}
