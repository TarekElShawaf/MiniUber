package com.example.miniuber.entities;

import java.util.Date;

public class Trip {
    private String pickupPoint ;
    private Float fare ;
    private Date tripDate ;
    private String Destination ;
    private Driver diver ;
    private Rider rider ;

    public Trip(String pickupPoint, Float fare, Date tripDate, String destination, Driver diver, Rider rider) {
        this.pickupPoint = pickupPoint;
        this.fare = fare;
        this.tripDate = tripDate;
        Destination = destination;
        this.diver = diver;
        this.rider = rider;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
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

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
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
