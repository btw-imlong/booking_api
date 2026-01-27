package com.booking.booking_api.Service;

import com.booking.booking_api.Enity.Availability;
import com.booking.booking_api.Enity.User;
import com.booking.booking_api.Repositories.AvailabilityRepository;
import com.booking.booking_api.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    // ------------------ CREATE AVAILABILITY ------------------
    public Availability createAvailability(UUID providerId, LocalDate date, 
                                           String startTime, String endTime) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        Availability availability = Availability.builder()
                .provider(provider)
                .date(date)
                .startTime(java.time.LocalTime.parse(startTime))
                .endTime(java.time.LocalTime.parse(endTime))
                .build();

        return availabilityRepository.save(availability);
    }

    // ------------------ GET AVAILABILITY ------------------
    public List<Availability> getAvailability(UUID providerId, LocalDate date) {
        return availabilityRepository.findByProviderIdAndDate(providerId, date);
    }
}
