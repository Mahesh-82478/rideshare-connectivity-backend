
package com.rideshare.backend.controller;

import com.rideshare.backend.entity.Booking;
import com.rideshare.backend.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://rideshare-connectivity-frontend.vercel.app"
})
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody Booking booking) {

        Booking savedBooking =
                bookingService.createBooking(booking);

        return new ResponseEntity<>(
                savedBooking,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }
}
