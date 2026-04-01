package com.project.bookingApp.service;

import com.project.bookingApp.dto.DriverRequestDTO;
import com.project.bookingApp.dto.DriverRideRequestDTO;
import com.project.bookingApp.dto.DriverRideResponseDTO;
import com.project.bookingApp.dto.RideRequestDTO;
import com.project.bookingApp.entity.Driver;
import com.project.bookingApp.entity.Location;
import com.project.bookingApp.entity.Ride;
import com.project.bookingApp.enums.DriverRideRequestStatus;
import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.enums.RideStatus;
import com.project.bookingApp.enums.VehicleType;
import com.project.bookingApp.exceptions.*;
import com.project.bookingApp.repos.DriverRepository;
import com.project.bookingApp.repos.RideRepository;
import com.project.bookingApp.utils.HaversineDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

@Service
public class RideService {
    private final DriverService driverService;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public RideService(DriverService driverService,RideRepository rideRepository,DriverRepository driverRepository)
    {
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public Ride requestRide(RideRequestDTO dto)
    {
        Location pickUpLocation = dto.getPickUpLocation();
        VehicleType vehicleType = dto.getVehicleType();

        DriverRequestDTO driverdto = new DriverRequestDTO();
        driverdto.setLocation(pickUpLocation);
        driverdto.setVehicleType(vehicleType);

        Driver driver = driverService.findNearestDriver(driverdto, new HashSet<>());

        Ride ride = new Ride();

        ride.setUserId(UUID.randomUUID());
        ride.setPickupLocation(dto.getPickUpLocation());
        ride.setDropOffLocation(dto.getDropOffLocation());
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.REQUESTED);
        ride.setDistance(HaversineDistance.calculateDistanceKm(pickUpLocation, dto.getDropOffLocation()));
        ride.setFare(calculateFare(ride.getDistance(),vehicleType));
        rideRepository.save(ride);
        return ride;

    }

    @Transactional
    public Ride respondToRide(DriverRideResponseDTO dto)
    {
        Ride ride = rideRepository.findById(dto.getRideId()).orElseThrow(()->new RideNotFoundException("Ride with id: " + dto.getRideId() + " doesn't exist"));
        if(dto.getDriverRideRequestStatus() == DriverRideRequestStatus.ACCEPT)
        {
            ride.setRideStatus(RideStatus.ACCEPTED);
            Driver driver = driverRepository.findById(dto.getDriverId()).orElseThrow(()->new DriverNotFoundException("Driver with id: " + dto.getDriverId() + " doesn't exist"));
            driver.setDriverStatus(DriverStatus.ON_RIDE);
            driverRepository.save(driver);
            return rideRepository.save(ride);
        }
        else {
            ride.getRejectedDriverIds().add(dto.getDriverId());
            DriverRequestDTO requestDTO = new DriverRequestDTO(ride.getPickupLocation(), ride.getDriver().getVehicleType());
            Driver nextDriver = driverService.findNearestDriver(requestDTO,ride.getRejectedDriverIds());
            ride.setDriver(nextDriver);
            return rideRepository.save(ride);

        }
    }

    @Transactional
    public void completeRide(Long rideId)
    {
        Ride ride = rideRepository.findById(rideId).orElseThrow(()->new RideNotFoundException("Ride with id: " + rideId + " doesn't exist"));

        if(ride.getRideStatus()!= RideStatus.IN_PROGRESS){ throw new CompletionNotPossibleException("The ride is not ongoing so it cannot be completed");}
        ride.setRideStatus(RideStatus.COMPLETED);
        Driver driver = ride.getDriver();
        driver.setDriverStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver);
        ride.setDriver(driver);
        rideRepository.save(ride);
    }

    @Transactional
    public void cancelRide(Long rideId)
    {
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RideNotFoundException("Ride with id: " + rideId + " doesn't exist"));
        if(ride.getRideStatus()!= RideStatus.IN_PROGRESS && ride.getRideStatus()!= RideStatus.REQUESTED && ride.getRideStatus()!= RideStatus.ACCEPTED) throw new CancellationNotPossibleException("The ride is not ongoing so it cannot be cancelled");
        ride.setRideStatus(RideStatus.CANCELLED);
        Driver driver = ride.getDriver();
        driver.setDriverStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver);
        ride.setDriver(driver);
        rideRepository.save(ride);

    }

    @Transactional
    public void startRide(Long rideId)
    {
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RideNotFoundException("Ride with id: " + rideId + " doesn't exist"));
        if(ride.getRideStatus() != RideStatus.ACCEPTED){
            throw new StartNotPossibleException("Ride is not accepted by any driver so it cannot be started");
        }
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        rideRepository.save(ride);
    }

    private double calculateFare(double distance, VehicleType vehicleType)
    {
        double basefare = 0.0;
        double perKmFare = 0.0;
        switch(vehicleType)
        {
            case VehicleType.AUTO:
                basefare = 30.0;
                perKmFare = 10.0;
                break;
            case VehicleType.CAB:
                basefare = 40.0;
                perKmFare = 20.0;
                break;
            case VehicleType.BIKE:
                basefare = 25.0;
                perKmFare = 10.0;
        }

        return basefare + (distance * perKmFare);
    }

}
