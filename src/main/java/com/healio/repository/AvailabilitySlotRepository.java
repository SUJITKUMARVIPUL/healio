package com.healio.repository;

import com.healio.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    // Fetch slots for a specific doctor that aren't booked yet
    List<AvailabilitySlot> findByDoctorProfileIdAndDateAndBookedFalse(Long doctorProfileId, LocalDate date);
}
