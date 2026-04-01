package com.project.bookingApp.entity;

import com.project.bookingApp.enums.VehicleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Fare {
    @Id
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private double baseFare;
    private double perKmFare;
}
