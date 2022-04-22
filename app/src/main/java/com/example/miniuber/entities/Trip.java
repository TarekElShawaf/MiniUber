package com.example.miniuber.entities;

import java.sql.Time;
import java.util.Date;

public class Trip {
    private Location pickupPoint ;
    private Float fare ;
    private Date tripDate ;
    private Location Destination ;
    private Driver diver ;
    private Time time;
    private float rate;
    private Rider rider ;


    public Trip(Location pickupPoint, Float fare, Date tripDate, Location destination, Driver diver, Time time, float rate, Rider rider) {
        this.pickupPoint = pickupPoint;
        this.fare = fare;
        this.tripDate = tripDate;
        this.Destination = destination;
        this.diver = diver;
        this.time = time;
        this.rate = rate;
        this.rider = rider;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Location getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(Location pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public Float getFare() {
        return fare;
    }

    public void setFare(Float fare) {
        this.fare = fare;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public Location getDestination() {
        return Destination;
    }

    public void setDestination(Location destination) {
        Destination = destination;
    }

    public Driver getDiver() {
        return diver;
    }

    public void setDiver(Driver diver) {
        this.diver = diver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }
}
