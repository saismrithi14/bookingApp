package com.project.bookingApp.repos;

import com.project.bookingApp.entity.Driver;
import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    List<Driver> findDriversByVehicleTypeAndDriverStatus(
            VehicleType vehicleType,
            DriverStatus driverStatus);

    List<Driver> findByDriverStatus(DriverStatus driverStatus);
}
