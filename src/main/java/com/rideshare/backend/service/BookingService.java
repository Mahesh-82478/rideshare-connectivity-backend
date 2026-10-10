
package com.rideshare.backend.service;

import com.rideshare.backend.entity.Booking;
import com.rideshare.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {

        // Prevent duplicate bookings for the same passenger and ride
        List<Booking> existingBookings = bookingRepository.findAll();

        boolean alreadyBooked = existingBookings.stream()
                .anyMatch(existing ->
                        existing.getPassengerId().equals(booking.getPassengerId())
                        && existing.getRideId().equals(booking.getRideId())
                        && "CONFIRMED".equalsIgnoreCase(existing.getStatus())
                );

        if (alreadyBooked) {
            throw new IllegalArgumentException(
                    "You have already booked this ride."
            );
        }

        if (booking.getBookingTime() == null) {
            booking.setBookingTime(LocalDateTime.now());
        }

        if (booking.getStatus() == null) {
            booking.setStatus("CONFIRMED");
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
