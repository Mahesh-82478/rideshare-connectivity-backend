package com.rideshare.backend.controller;

import com.rideshare.backend.entity.RideRequest;
import com.rideshare.backend.service.RideRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ride-requests")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://rideshare-connectivity-frontend.vercel.app"
})
public class RideRequestController {

    private final RideRequestService rideRequestService;

    public RideRequestController(RideRequestService rideRequestService) {
        this.rideRequestService = rideRequestService;
    }

    @PostMapping
    public ResponseEntity<RideRequest> createRequest(
            @RequestBody RideRequest rideRequest) {

        RideRequest savedRequest =
                rideRequestService.createRequest(rideRequest);

        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RideRequest>> getAllRequests() {
        return ResponseEntity.ok(
                rideRequestService.getAllRequests()
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RideRequest> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        RideRequest updatedRequest =
                rideRequestService.updateRequestStatus(id, status);

        return ResponseEntity.ok(updatedRequest);
    }
}