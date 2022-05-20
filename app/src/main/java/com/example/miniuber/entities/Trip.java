package com.example.miniuber.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity(tableName = "Trips")
public class Trip implements Serializable {
    private String pickupPoint ;
    private String time;
    private String destination;
    private Float fare ;
    private String tripDate ;
    private String driverPhoneNo ;
    private float rate;
    private String  riderPhoneNo;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int tripId =0;



    public Trip(String pickupPoint, String time, String destination, Float fare, String tripDate, String driverPhoneNumber, float rate, String riderPhoneNumber) {
        this.pickupPoint = pickupPoint;
        this.time = time;
        this.destination = destination;
        this.fare = fare;
        this.tripDate = tripDate;
        this.driverPhoneNo = driverPhoneNumber;
        this.rate = rate;
        this.riderPhoneNo = riderPhoneNumber;

    }
    public Trip() {

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


    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getDriverPhoneNo() {
        return driverPhoneNo;
    }

    public void setDriverPhoneNo(String driverPhoneNo) {
        this.driverPhoneNo = driverPhoneNo;
    }

    public String getRiderPhoneNo() {
        return riderPhoneNo;
    }

    public void setRiderPhoneNo(String riderPhoneNo) {
        this.riderPhoneNo = riderPhoneNo;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
}
