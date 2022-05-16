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

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
