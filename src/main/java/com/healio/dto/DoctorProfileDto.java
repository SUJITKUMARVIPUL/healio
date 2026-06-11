package com.healio.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DoctorProfileDto {
    private String specialty;
    private String biography;
    private BigDecimal consultationFee;
}
