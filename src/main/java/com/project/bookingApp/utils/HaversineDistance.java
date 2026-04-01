package com.project.bookingApp.utils;

import com.project.bookingApp.entity.Location;

public class HaversineDistance {
    public static  Double calculateDistanceKm(Location pickUpLocation, Location dropLocation)
    {
        double R = 6371;
        double dLat = Math.toRadians(dropLocation.getLatitude() - pickUpLocation.getLatitude());
        double dLon = Math.toRadians(dropLocation.getLongitude() - pickUpLocation.getLongitude());

        double lat1 = Math.toRadians(pickUpLocation.getLatitude());
        double lat2 = Math.toRadians(dropLocation.getLatitude());

        double a = Math.pow(Math.sin(dLat/2),2) + Math.cos(lat1) * Math.cos(lat2) *
                Math.pow(Math.sin(dLon/2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;

    }
}
