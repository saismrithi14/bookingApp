package com.project.bookingApp.runner;

import com.project.bookingApp.entity.Fare;
import com.project.bookingApp.enums.VehicleType;
import com.project.bookingApp.repos.FareRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareLoader implements CommandLineRunner {
    private final FareRepository fareRepository;
    public FareLoader(FareRepository fareRepository)
    {
        this.fareRepository = fareRepository;
    }
    @Override
    public void run(String...args)
    {
        if(fareRepository.count() == 0) {
            fareRepository.saveAll(List.of(
                    Fare.builder()
                            .vehicleType(VehicleType.CAB)
                            .baseFare(40.0)
                            .perKmFare(20.0)
                            .build(),
                    Fare.builder()
                            .vehicleType(VehicleType.AUTO)
                            .baseFare(30.0)
                            .perKmFare(10.0)
                            .build(),
                    Fare.builder()
                            .vehicleType(VehicleType.BIKE)
                            .baseFare(25.0)
                            .perKmFare(10.0)
                            .build()
            ));
        }
    }
}
