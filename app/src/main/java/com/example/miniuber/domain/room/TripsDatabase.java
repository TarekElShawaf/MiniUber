package com.example.miniuber.domain.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.miniuber.entities.converters.DestinationConverter;
import com.example.miniuber.entities.converters.PickupConverter;
import com.example.miniuber.entities.converters.TimeConverter;
import com.example.miniuber.entities.Trip;

@Database(entities = {Trip.class} ,version = 18, exportSchema = false)
public abstract class TripsDatabase extends RoomDatabase {

    private static TripsDatabase instance;

    public static synchronized TripsDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TripsDatabase.class, "TripsDatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract TripDoa tripsDao();
}


