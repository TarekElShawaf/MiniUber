package com.example.miniuber.entities;

public class Location {

    public String LocationName;
    public double Latitude , Longitude;
    public String userId;


    public Location(String LocationName , double latitude, double longitude , String ID_1 ){

        this.LocationName = LocationName;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.userId = ID_1;


    }
}
