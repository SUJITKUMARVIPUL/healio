package com.healio.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalRequest {
    @NotNull private Long doctorProfileId;
    @NotNull private boolean approve;
}
