package com.rideshare.backend.repository;

import com.rideshare.backend.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
}