package com.example.miniuber.entities;

public class Driver extends User{

    private  String  drivingLicense ;


    public Driver(String name, String email, String password, String phoneNumber, String userId, float rate, String drivingLicense) {
        super(name, email, password, phoneNumber, userId, rate);
        this.drivingLicense = drivingLicense;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
