package com.project.bookingApp.dto;

import com.project.bookingApp.entity.Location;
import com.project.bookingApp.enums.VehicleType;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {
    private Location pickUpLocation;
    private Location dropOffLocation;
    private VehicleType vehicleType;

}
