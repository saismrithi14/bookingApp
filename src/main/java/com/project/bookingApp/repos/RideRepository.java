package com.project.bookingApp.repos;

import com.project.bookingApp.entity.Ride;
import com.project.bookingApp.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride,Long> {
    long countByRideStatus(RideStatus rideStatus);
}
