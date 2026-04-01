package com.project.bookingApp.repos;

import com.project.bookingApp.entity.Fare;
import com.project.bookingApp.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareRepository extends JpaRepository<Fare, VehicleType> {
}
