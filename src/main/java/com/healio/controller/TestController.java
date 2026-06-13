package com.healio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping
    public String test(){
        System.out.println("test");
        log.info(">>>> TEST ENDPOINT WAS HIT SUCCESFULLY! <<<<");
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
