package com.project.bookingApp.entity;

import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    @GeneratedValue
    private UUID driverId;

    private String driverName;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    @Embedded
    private Location location;
}
