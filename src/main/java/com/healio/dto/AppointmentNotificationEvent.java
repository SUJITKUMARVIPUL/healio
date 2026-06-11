package com.healio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentNotificationEvent {
    private Long appointmentId;
    private String patientEmail;
    private String doctorName;
    private String appointmentDate;
    private String startTime;
}
