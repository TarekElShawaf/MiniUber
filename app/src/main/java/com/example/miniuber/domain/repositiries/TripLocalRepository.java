package com.example.miniuber.domain.repositiries;

import androidx.lifecycle.LiveData;

import com.example.miniuber.entities.Trip;

import java.util.List;

public interface TripLocalRepository {
    void insertTrip(Trip trip);

    void deleteTrip(Trip trip);

    LiveData<List<Trip>> getAllTrips();
    Trip getTripById(int id);


}
