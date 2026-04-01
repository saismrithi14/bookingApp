package com.project.bookingApp.dto;

import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class getDriverDTO {
    private UUID driverId;
    private String driverName;
    private String phoneNumber;
    private VehicleType vehicleType;
    private DriverStatus driverStatus;
}
