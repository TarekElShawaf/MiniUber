package com.example.miniuber.domain.repositiries;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.miniuber.domain.room.TripsDatabase;
import com.example.miniuber.entities.Trip;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TripLocalImplementation implements TripLocalRepository {
    private static TripLocalImplementation instance;
    private TripsDatabase tripsDatabase;
    private Executor executor = Executors.newSingleThreadExecutor();
    public static TripLocalImplementation getInstance(Context context) {
        if (instance == null) {
            instance = new TripLocalImplementation(context);
        }
        return instance;
    }
    public TripLocalImplementation(Context context) {
        tripsDatabase = TripsDatabase.getInstance(context);
    }
    @Override
    public void insertTrip(Trip trip) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                tripsDatabase.tripsDao().insertTrip(trip);

            }
        });
    }

    @Override
    public void deleteTrip(Trip trip) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                tripsDatabase.tripsDao().deleteTrip(trip);

            }
        });
    }

    @Override
    public LiveData<List<Trip>> getAllTrips() {
        return tripsDatabase.tripsDao().getAllTrips();
    }

    @Override
    public Trip getTripById(int id) {
        return tripsDatabase.tripsDao().getTripById(id);

    }
}
