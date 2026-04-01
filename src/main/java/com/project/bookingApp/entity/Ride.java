package com.project.bookingApp.entity;

import com.project.bookingApp.enums.PaymentStatus;
import com.project.bookingApp.enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long rideId;

    private UUID userId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "pickup_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "pickup_lon"))
    })
    private Location pickupLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "drop_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "drop_lon"))
    })
    private Location dropOffLocation;

    @ManyToOne
    @JoinColumn(name="driverId",referencedColumnName="driverId")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;


    private Double distance;

    private Double fare;

    @ElementCollection(fetch=FetchType.EAGER)
    private Set<UUID> rejectedDriverIds = new HashSet<>();

    private Integer rating;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;



}
