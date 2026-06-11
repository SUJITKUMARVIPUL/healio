package com.healio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healio.dto.AppointmentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "appointment-notifications", groupId = "healio-notification-group")
    public void consumeNotificationEvent(String message) {
        try {
            AppointmentNotificationEvent event = objectMapper.readValue(message, AppointmentNotificationEvent.class);

            log.info("************ KAFKA NOTIFICATION WORKER ************");
            log.info("Sending confirmation email to patient: {}", event.getPatientEmail());
            log.info("Message: Hello, your appointment (ID: {}) with {} on {} at {} has been successfully confirmed!",
                    event.getAppointmentId(), event.getDoctorName(), event.getAppointmentDate(), event.getStartTime());
            log.info("****************************************************");

            // In a real-world app, you would inject JavaMailSender here to fire off an actual email.

        } catch (Exception e) {
            log.error("Error processing message from Kafka consumer", e);
        }
    }
}