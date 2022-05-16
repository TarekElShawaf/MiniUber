package com.example.miniuber.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "Trips")
public class Trip {
    private String pickupPoint ;
    private String time;
    private String destination;
    private Float fare ;
    private String tripDate ;
    private int driverId ;
    private float rate;
    private int  riderId ;
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    private int tripId =0;

    public Trip(String pickupPoint, String time, String destination, Float fare, String tripDate, int driverId, float rate, int riderId, int tripId) {
        this.pickupPoint = pickupPoint;
        this.time = time;
        this.destination = destination;
        this.fare = fare;
        this.tripDate = tripDate;
        this.driverId = driverId;
        this.rate = rate;
        this.riderId = riderId;
        this.tripId = tripId;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Float getFare() {
        return fare;
    }

    public void setFare(Float fare) {
        this.fare = fare;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
