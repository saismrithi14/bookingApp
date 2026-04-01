package com.project.bookingApp.runner;

import com.project.bookingApp.entity.Driver;
import com.project.bookingApp.entity.Location;
import com.project.bookingApp.enums.DriverStatus;
import com.project.bookingApp.enums.VehicleType;
import com.project.bookingApp.repos.DriverRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {
    private final DriverRepository driverRepository;
    public DataLoader(DriverRepository driverRepository)
    {
        this.driverRepository = driverRepository;
    }
    @Override
    public void run(String...args)
    {
        if(driverRepository.count()==0)
        {
            driverRepository.saveAll(List.of(
                    Driver.builder().driverName("Alice")
                            .phoneNumber("9876543210")
                            .vehicleType(VehicleType.CAB)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(13.00,77.60))
                            .build(),
                    Driver.builder()
                            .driverName("Bob")
                            .phoneNumber("9988776655")
                            .vehicleType(VehicleType.AUTO)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(12.950,77.65))
                            .build(),
                    Driver.builder()
                            .driverName("Charlie")
                            .phoneNumber("9944332211")
                            .vehicleType(VehicleType.BIKE)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(12.90,77.7))
                            .build(),
                    Driver.builder()
                            .driverName("Dean")
                            .phoneNumber("9879879876")
                            .vehicleType(VehicleType.BIKE)
                            .driverStatus(DriverStatus.ON_RIDE)
                            .location(new Location(13.050,77.550))
                            .build(),
                    Driver.builder()
                            .driverName("Emma")
                            .phoneNumber("9547658212")
                            .vehicleType(VehicleType.AUTO)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(12.850,77.500))
                            .build(),
                    Driver.builder()
                            .driverName("Fiona")
                            .phoneNumber("9132721540")
                            .vehicleType(VehicleType.CAB)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(13.100,77.8000))
                            .build(),
                    Driver.builder()
                            .driverName("Govind")
                            .phoneNumber("9000000001")
                            .vehicleType(VehicleType.BIKE)
                            .driverStatus(DriverStatus.ON_RIDE)
                            .location(new Location(12.920,77.580))
                            .build(),
                    Driver.builder()
                            .driverName("Horace")
                            .phoneNumber("9005647229")
                            .vehicleType(VehicleType.AUTO)
                            .driverStatus(DriverStatus.AVAILABLE)
                            .location(new Location(13.020,77.620))
                            .build(),
                    Driver.builder()
                            .driverName("Indu")
                            .phoneNumber("9012443355")
                            .vehicleType(VehicleType.CAB)
                            .driverStatus(DriverStatus.AWAY)
                            .location(new Location(12.880,77.660))
                            .build()
            ));
        }
    }
}
