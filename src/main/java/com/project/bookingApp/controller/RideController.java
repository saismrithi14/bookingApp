package com.project.bookingApp.controller;

import com.project.bookingApp.dto.DriverRideResponseDTO;
import com.project.bookingApp.dto.RideRequestDTO;
import com.project.bookingApp.entity.Ride;
import com.project.bookingApp.service.RideService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rides")
public class RideController {
    private final RideService rideService;
    public RideController(RideService rideService)
    {
        this.rideService = rideService;
    }
    @PostMapping("/request-ride")
    public Ride requestRide(@RequestBody RideRequestDTO dto)
    {
        return rideService.requestRide(dto);
    }

    @PostMapping("/complete/{rideId}")
    public void completeRide(@PathVariable Long runId)
    {
        rideService.completeRide(runId);
    }

    @PostMapping("/cancel/{rideId}")
    public void cancelRide(@PathVariable Long runId)
    {
        rideService.cancelRide(runId);
    }

    @PostMapping("/response-to-ride")
    public Ride respondToRide(@RequestBody DriverRideResponseDTO dto)
    {
        return rideService.respondToRide(dto);
    }

    @PostMapping("/start/{rideId}")
    public void startRide(@PathVariable Long rideId)
    {
        rideService.startRide(rideId);
    }
}
