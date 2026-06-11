package com.healio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardStatsDto {
    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
}
