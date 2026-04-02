package com.project.bookingApp.dto;

import com.project.bookingApp.entity.Location;
import lombok.*;
import java.util.UUID;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverRideRequestDTO {
    private Long rideId;
    private UUID driverId;
    private Location pickUpLocation;
    private Location droppOffLocation;
    private Double distance;
    private Double fare;

}
