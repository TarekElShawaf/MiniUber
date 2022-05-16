package com.example.miniuber.domain.room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.miniuber.entities.Trip;

import java.util.List;
@Dao
public interface TripDoa {
    @Insert
    void  insertTrip(Trip trip);
    @Delete
    void deleteTrip(Trip trip);
    @Query("SELECT * FROM Trips ")
    LiveData<List<Trip>> getAllTrips();
 //get trip by id
    @Query("SELECT * FROM Trips WHERE id = :id")
    Trip getTripById(int id);
}
