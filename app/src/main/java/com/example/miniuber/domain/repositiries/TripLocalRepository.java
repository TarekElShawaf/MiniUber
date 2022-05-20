package com.example.miniuber.domain.repositiries;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.miniuber.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public interface TripLocalRepository {
    void insertTrip(Trip trip);

    void deleteTrip(Trip trip);

    LiveData<List<Trip>> getAllTrips();

    List<Trip> getAllTripsData();
    List<Trip> getTripByRiderId(String phoneNumber);
    List<Trip> getTripByDriverId(String phoneNumber);


}
