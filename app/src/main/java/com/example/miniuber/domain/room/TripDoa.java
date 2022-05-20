package com.example.miniuber.domain.room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniuber.entities.Trip;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface TripDoa {
    @Insert
    void  insertTrip(Trip trip);
    @Delete
    void deleteTrip(Trip trip);
    @Query("SELECT * FROM Trips ")
    LiveData<List<Trip>> getAllTrips();
    @Query("SELECT * FROM Trips ")
    List<Trip> getAllTripsData();
    //get trip by id
    @Query("SELECT * FROM Trips WHERE driverPhoneNo = :phoneNumber")
    Trip getTripByDriverId(String phoneNumber);
    @Query("SELECT * FROM Trips WHERE riderPhoneNo = :phoneNumber")
    Trip getTripByRiderId(String phoneNumber);

}
