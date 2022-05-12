package com.example.miniuber.entities;

import java.io.Serializable;

public class Driver extends User implements Serializable {

    //Car car;
    String driverLicense;
    public Driver(String name, String email, String phoneNumber, String userId, float rate, String drivingLicense) {
        super(name, email, phoneNumber, userId, rate);
        this.driverLicense = driverLicense;
    }

}
