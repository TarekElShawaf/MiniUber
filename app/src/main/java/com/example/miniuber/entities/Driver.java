package com.example.miniuber.entities;

public class Driver extends User{

    private  String  drivingLicense ;

    public Driver(String name, String email, String password, String phoneNumber, String userId,String  drivingLicense) {
        super(name, email, password, phoneNumber, userId);
        this.drivingLicense=drivingLicense;
    }
}