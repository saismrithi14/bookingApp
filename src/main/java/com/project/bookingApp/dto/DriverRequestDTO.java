package com.project.bookingApp.dto;

import com.project.bookingApp.entity.Location;
import com.project.bookingApp.enums.VehicleType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequestDTO {
    private Location location;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}
