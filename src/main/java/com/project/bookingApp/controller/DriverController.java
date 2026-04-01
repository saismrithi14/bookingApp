package com.project.bookingApp.controller;

import com.project.bookingApp.dto.DriverRequestDTO;
import com.project.bookingApp.dto.getDriverDTO;
import com.project.bookingApp.entity.Driver;
import com.project.bookingApp.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {
    private final DriverService driverService;


    public DriverController(DriverService driverService)
    {
        this.driverService = driverService;
    }
    @GetMapping("/nearest-driver")
    public Driver findNearestDriver(@Valid @RequestBody DriverRequestDTO dto)
    {
        return driverService.findNearestDriver(dto, new HashSet<>());
    }

    @GetMapping("/all-drivers")
    public List<getDriverDTO> getAllMembers()
    {
        return driverService.getAllDrivers();
    }

    @GetMapping("/all-available-drivers")
    public List<getDriverDTO> getAllAvailableDrivers()
    {
        return driverService.getAllAvailableDrivers();
    }



}
