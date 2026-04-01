package com.project.bookingApp.service;

import com.project.bookingApp.entity.Fare;
import com.project.bookingApp.enums.RideStatus;
import com.project.bookingApp.enums.VehicleType;
import com.project.bookingApp.exceptions.VehicleTypeNotFoundException;
import com.project.bookingApp.repos.FareRepository;
import com.project.bookingApp.repos.RideRepository;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    private final FareRepository fareRepository;
    private final RideRepository rideRepository;
    public FareService(FareRepository fareRepository, RideRepository rideRepository)
    {
        this.fareRepository = fareRepository;
        this.rideRepository = rideRepository;
    }

    public double calculateFare(VehicleType vehicleType, double distance)
    {
        double surgeMultiplier = 0.0;
        Fare fare = fareRepository.findById(vehicleType).orElseThrow(()->new VehicleTypeNotFoundException("Vehicle type: " + vehicleType + " doesn't exist"));
        long activeRides = rideRepository.countByRideStatus(RideStatus.IN_PROGRESS);
        if(activeRides < 2) surgeMultiplier = 1.0;
        else if(activeRides >= 2 && activeRides < 4) surgeMultiplier = 1.2;
        else if(activeRides >= 4 && activeRides < 6) surgeMultiplier = 1.5;
        else{
            surgeMultiplier = 2.0;
        }

        return (fare.getBaseFare() + (fare.getPerKmFare() * distance)) * surgeMultiplier;

    }
}
