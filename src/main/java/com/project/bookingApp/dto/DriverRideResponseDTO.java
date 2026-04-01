package com.project.bookingApp.dto;

import com.project.bookingApp.enums.DriverRideRequestStatus;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DriverRideResponseDTO {
    private Long rideId;
    private UUID driverId;
    private DriverRideRequestStatus driverRideRequestStatus;
}
