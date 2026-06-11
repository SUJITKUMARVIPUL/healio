package com.healio.service;

import com.healio.dto.DoctorProfileDto;
import com.healio.dto.SlotCreationRequest;
import com.healio.entity.AvailabilitySlot;
import com.healio.entity.DoctorProfile;
import com.healio.entity.User;
import com.healio.repository.AvailabilitySlotRepository;
import com.healio.repository.DoctorProfileRepository;
import com.healio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final UserRepository userRepository;

    @Transactional
    public DoctorProfile createOrUpdateProfile(String email, DoctorProfileDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        DoctorProfile profile = doctorProfileRepository.findByUserId(user.getId())
                .orElse(new DoctorProfile());

        profile.setUser(user);
        profile.setSpecialty(dto.getSpecialty());
        profile.setBiography(dto.getBiography());
        profile.setConsultationFee(dto.getConsultationFee());

        return doctorProfileRepository.save(profile);
    }

    @Transactional
    public AvailabilitySlot addAvailabilitySlot(String email, SlotCreationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalStateException("Doctor profile must be created first."));

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .doctorProfile(doctorProfile)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .booked(false)
                .build();

        return availabilitySlotRepository.save(slot);
    }

    public List<DoctorProfile> getAllDoctors() {
        return doctorProfileRepository.findAll();
    }

    public List<AvailabilitySlot> getAvailableSlots(Long doctorId, LocalDate date) {
        return availabilitySlotRepository.findByDoctorProfileIdAndDateAndBookedFalse(doctorId, date);
    }
}
