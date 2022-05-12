package com.example.miniuber.entities;

import java.sql.Time;
import java.util.Date;

public class Trip {
    private Location pickupPoint ;
    private Float fare ;
    private Date tripDate ;
    private Location Destination ;
    private Driver driver ;
    private Time time;
    private float rate;
    private Rider rider ;
   // private Car car;


    public Trip(Location pickupPoint, Float fare, Date tripDate, Location destination, Driver driver, Time time, float rate, Rider rider
    ,String driverLicense) {
        this.pickupPoint = pickupPoint;
        this.fare = fare;
        this.tripDate = tripDate;
        this.Destination = destination;
        this.driver = driver;
        this.time = time;
        this.rate = rate;
        this.rider = rider;
        //this.car=car;
    }

   /* public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
    */

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

    public Driver getDriver() {
        return driver;
    }

    public void setDiver(Driver diver) {
        this.driver = diver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }
}
