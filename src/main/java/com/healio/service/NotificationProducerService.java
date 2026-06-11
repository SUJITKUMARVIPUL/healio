package com.healio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healio.dto.AppointmentNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "appointment-notifications";

    public void sendNotificationEvent(AppointmentNotificationEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            log.info("Publishing booking event to Kafka topic [{}]: {}", TOPIC, message);
            kafkaTemplate.send(TOPIC, message);
        } catch (Exception e) {
            log.error("Failed to publish event to Kafka", e);
        }
    }
}
