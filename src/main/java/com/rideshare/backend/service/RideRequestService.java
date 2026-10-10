
package com.rideshare.backend.service;

import com.rideshare.backend.entity.Booking;
import com.rideshare.backend.entity.Ride;
import com.rideshare.backend.entity.RideRequest;
import com.rideshare.backend.repository.BookingRepository;
import com.rideshare.backend.repository.RideRepository;
import com.rideshare.backend.repository.RideRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;

    public RideRequestService(
            RideRequestRepository rideRequestRepository,
            BookingRepository bookingRepository,
            RideRepository rideRepository) {
        this.rideRequestRepository = rideRequestRepository;
        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
    }

    public RideRequest createRequest(RideRequest rideRequest) {
        if (rideRequest.getStatus() == null ||
                rideRequest.getStatus().isBlank()) {
            rideRequest.setStatus("PENDING");
        }
        return rideRequestRepository.save(rideRequest);
    }

    public List<RideRequest> getAllRequests() {
        return rideRequestRepository.findAll();
    }

    @Transactional
    public RideRequest updateRequestStatus(Long id, String status) {
        RideRequest request = rideRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Ride request not found"));

        if (!"ACCEPTED".equalsIgnoreCase(status)) {
            request.setStatus(status.toUpperCase());
            return rideRequestRepository.save(request);
        }

        if ("ACCEPTED".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException(
                    "This request has already been processed");
        }

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException(
                    "Only pending requests can be accepted");
        }

        Ride ride = rideRepository.findById(request.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getAvailableSeats() == null ||
                ride.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        Booking booking = new Booking();
        booking.setRideId(request.getRideId());
        booking.setPassengerId(request.getPassengerId());
        booking.setStatus("CONFIRMED");
        booking.setBookingTime(LocalDateTime.now());

        bookingRepository.save(booking);

        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        rideRepository.save(ride);

        request.setStatus("ACCEPTED");
        return rideRequestRepository.save(request);
    }
}
