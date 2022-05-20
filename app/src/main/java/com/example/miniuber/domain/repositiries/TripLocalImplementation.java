package com.example.miniuber.domain.repositiries;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.miniuber.domain.room.TripsDatabase;
import com.example.miniuber.entities.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TripLocalImplementation implements TripLocalRepository {
    private static final String TAG = "maskdm";
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
                Log.d(TAG, "run: trip dest is  "+trip.getDestination());

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
    public List<Trip> getAllTripsData() {
        return tripsDatabase.tripsDao().getAllTripsData();
    }

    @Override
    public Trip getTripByRiderId(String phoneNumber) {
        return tripsDatabase.tripsDao().getTripByRiderId(phoneNumber);
    }

    @Override
    public Trip getTripByDriverId(String phoneNumber) {
        return tripsDatabase.tripsDao().getTripByDriverId(phoneNumber);
    }
}
