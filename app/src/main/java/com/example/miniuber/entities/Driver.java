package com.example.miniuber.entities;

import java.io.Serializable;

public class Driver extends User implements Serializable {

    private  String  drivingLicense ;


    public Driver(String name, String email, String phoneNumber, String userId, float rate, String drivingLicense) {
        super(name, email, phoneNumber, userId, rate);
        this.drivingLicense = drivingLicense;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }
}
