package com.healio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull private Long doctorProfileId;
    @NotNull private Long slotId;
    private String symptoms;
}