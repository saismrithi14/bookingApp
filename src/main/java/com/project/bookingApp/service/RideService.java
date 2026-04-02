package com.project.bookingApp.service;

import com.project.bookingApp.dto.*;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class RideService {
    private final DriverService driverService;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final FareService fareService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RideService(DriverService driverService,RideRepository rideRepository,DriverRepository driverRepository, FareService fareService,SimpMessagingTemplate simpMessagingTemplate)
    {
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.fareService = fareService;
        this.simpMessagingTemplate = simpMessagingTemplate;
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
        ride.setFare(fareService.calculateFare(vehicleType,ride.getDistance()));
        rideRepository.saveAndFlush(ride);
        simpMessagingTemplate.convertAndSend("/topic/ride-status/"+ ride.getRideId(),"You've successfully requested you're ride. Please wait for a driver to be assigned to you");
        DriverRideRequestDTO rideRequestDTO = new DriverRideRequestDTO();
        rideRequestDTO.setRideId(ride.getRideId());
        rideRequestDTO.setDriverId(ride.getDriver().getDriverId());
        rideRequestDTO.setPickUpLocation(ride.getPickupLocation());
        rideRequestDTO.setDroppOffLocation(ride.getDropOffLocation());
        rideRequestDTO.setDistance(ride.getDistance());
        rideRequestDTO.setFare(ride.getFare());
        simpMessagingTemplate.convertAndSend("/topic/driver-offers/"+ driver.getDriverId(), rideRequestDTO);
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
            driverRepository.saveAndFlush(driver);
            simpMessagingTemplate.convertAndSend("/topic/ride-status/" + ride.getRideId(), driver.getDriverName() + " has accepted your request!!");
            return rideRepository.save(ride);
        }
        else {
            ride = rideRepository.findById(dto.getRideId()).orElseThrow(()->new RideNotFoundException("Ride with id: " + dto.getRideId() + " doesn't exist"));
            Set<UUID> rejected = new HashSet<>(ride.getRejectedDriverIds());
            rejected.add(dto.getDriverId());
            ride.setRejectedDriverIds(rejected);
            ride.getRejectedDriverIds().add(dto.getDriverId());
            DriverRequestDTO requestDTO = new DriverRequestDTO(ride.getPickupLocation(), ride.getDriver().getVehicleType());
            Driver nextDriver = driverService.findNearestDriver(requestDTO,ride.getRejectedDriverIds());
            if(nextDriver != null)
            {
                ride.setDriver(nextDriver);
                rideRepository.saveAndFlush(ride);
                simpMessagingTemplate.convertAndSend("/topic/ride-status/"+ride.getRideId(),"Driver was busy, matching you with new driver......");

                DriverRideRequestDTO rideRequestDTO = new DriverRideRequestDTO();
                rideRequestDTO.setRideId(ride.getRideId());
                rideRequestDTO.setDriverId(ride.getDriver().getDriverId());
                rideRequestDTO.setPickUpLocation(ride.getPickupLocation());
                rideRequestDTO.setDroppOffLocation(ride.getDropOffLocation());
                rideRequestDTO.setDistance(ride.getDistance());
                rideRequestDTO.setFare(ride.getFare());
                simpMessagingTemplate.convertAndSend("/topic/driver-offers/"+ nextDriver.getDriverId(), rideRequestDTO);

            }
            else {
                simpMessagingTemplate.convertAndSend("/topic/ride-status/"+ride.getRideId(), "Sorry, no drivers available. Please try again later");
                ride.setRideStatus(RideStatus.CANCELLED);
                simpMessagingTemplate.convertAndSend("/topic/ride-status/" + ride.getRideId(), "Ride has been cancelled. Please try again in sometime.");
            }

            return rideRepository.saveAndFlush(ride);

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
        rideRepository.saveAndFlush(ride);
        simpMessagingTemplate.convertAndSend("/topic/ride-status/" + rideId,"You have reached your destination! Please pay " + ride.getDriver().getDriverName() + " " + ride.getFare() + " rupees");
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
        rideRepository.saveAndFlush(ride);
        simpMessagingTemplate.convertAndSend("/topic/ride-status/" + rideId,"Uh-Oh, your ride has been cancelled");

    }

    @Transactional
    public void startRide(Long rideId)
    {
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RideNotFoundException("Ride with id: " + rideId + " doesn't exist"));
        if(ride.getRideStatus() != RideStatus.ACCEPTED){
            throw new StartNotPossibleException("Ride is not accepted by any driver so it cannot be started");
        }
        ride.setRideStatus(RideStatus.IN_PROGRESS);
        rideRepository.saveAndFlush(ride);
        simpMessagingTemplate.convertAndSend("/topic/ride-status/" + rideId,ride.getDriver().getDriverName() + " has started your ride!");
    }

    @Transactional
    public void rateRide(RateDTO rateDTO)
    {
        Ride ride = rideRepository.findById(rateDTO.getRideId()).orElseThrow(()-> new RideNotFoundException("Ride with id: " + rateDTO.getRating() + " doesn't exist"));
        if(ride.getRideStatus()!= RideStatus.COMPLETED){
            throw new RatingNotPossibleException("Rating cannot be done as ride has not completed yet");
        }
        if(rateDTO.getRating() < 1 || rateDTO.getRating() > 10)
        {
            throw new IllegalRatingException("Invalid rating. Please rate again");
        }
        ride.setRating(rateDTO.getRating());
        rideRepository.save(ride);
    }

    public Set<UUID> getRejectedDriverIds(Long rideId)
    {
        Ride ride = rideRepository.findById(rideId).orElseThrow(()-> new RideNotFoundException("Ride with id: " + rideId + " doesn't exist"));
        return ride.getRejectedDriverIds();
    }

}
