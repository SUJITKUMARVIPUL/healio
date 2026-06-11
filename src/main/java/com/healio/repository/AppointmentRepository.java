package com.healio.repository;

import com.healio.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // Find appointments booked by a specific patient
    List<Appointment> findByPatientId(Long patientId);

    // Find appointments scheduled for a specific doctor
    List<Appointment> findByDoctorProfileId(Long doctorProfileId);
}
