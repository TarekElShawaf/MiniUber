package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.miniuber.domain.repositiries.TripLocalImplementation;
import com.example.miniuber.domain.repositiries.TripLocalRepository;
import com.example.miniuber.entities.Trip;

import java.util.List;

public class TripsViewModel extends ViewModel {
    private TripLocalRepository tripLocalRepository;
    public TripsViewModel(@NonNull Application application) {
        super();
        tripLocalRepository = new TripLocalImplementation(application);

    }

    public void insertTrip(Trip trip) {
        tripLocalRepository.insertTrip(trip);
    }

    public void deleteFavouriteMovie(Trip trip) {
        tripLocalRepository.deleteTrip(trip);
    }

    public LiveData<List<Trip>> getFavouriteMovies() {

        return tripLocalRepository.getAllTrips();
    }
    public Trip getTripById(int id) {
       return tripLocalRepository.getTripById(id);
    }



}
