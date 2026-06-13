package com.healio.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public String test(){
        return "test";
    }

    @GetMapping("/patient-only")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public String patientEndpoint() {
        return "Access granted: You are a Patient!";
    }

    @GetMapping("/doctor-only")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public String doctorEndpoint() {
        return "Access granted: You are a Doctor!";
    }
}
