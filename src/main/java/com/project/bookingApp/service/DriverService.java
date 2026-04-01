package com.project.bookingApp.service;

import com.project.bookingApp.dto.DriverRequestDTO;
import com.project.bookingApp.dto.getDriverDTO;
import com.project.bookingApp.entity.Driver;
import com.project.bookingApp.entity.Location;
import com.project.bookingApp.enums.VehicleType;
import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.exceptions.DriverNotFoundException;
import com.project.bookingApp.repos.DriverRepository;
import com.project.bookingApp.utils.HaversineDistance;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository)
    {
        this.driverRepository = driverRepository;
    }

    public Driver findNearestDriver(DriverRequestDTO dto, Set<UUID> rejectedDriverIds)
    {
        VehicleType vehicleType = dto.getVehicleType();
        Location currentLocation = dto.getLocation();

        List<Driver> available_drivers = driverRepository.findDriversByVehicleTypeAndDriverStatus(vehicleType,DriverStatus.AVAILABLE);

        return available_drivers.stream().filter(driver -> !rejectedDriverIds.contains(driver.getDriverId())).min(Comparator.comparing(driver ->{
            return HaversineDistance.calculateDistanceKm(currentLocation,driver.getLocation());
        })).orElseThrow(()->new DriverNotFoundException("No Drivers available"));

    }

    public List<getDriverDTO> getAllDrivers()
    {
        List<Driver> all_drivers = driverRepository.findAll();
        return all_drivers.stream().map(this::mapToDTO).toList();

    }

    public List<getDriverDTO> getAllAvailableDrivers()
    {
        return driverRepository.findByDriverStatus(DriverStatus.AVAILABLE).stream().map(this::mapToDTO).toList();
    }

    private getDriverDTO mapToDTO(Driver driver)
    {
        getDriverDTO dto = new getDriverDTO();
        dto.setDriverId(driver.getDriverId());
        dto.setDriverName(driver.getDriverName());
        dto.setPhoneNumber(driver.getPhoneNumber());
        dto.setVehicleType(driver.getVehicleType());
        dto.setDriverStatus(driver.getDriverStatus());
        return dto;
    }

}
