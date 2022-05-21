package com.example.miniuber.entities;

import java.io.Serializable;

public class Driver extends User implements Serializable {

    private  String  drivingLicense ;
    private  String  carPlateNumber ;
    private  Car car ;
    private Location location ;


    public Driver() {
    }

    public Driver(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }


    public Driver(String name, String email, String phoneNumber, String drivingLicense, String carPlateNumber) {
        super(name, email, phoneNumber);
        this.drivingLicense = drivingLicense;
        this.carPlateNumber = carPlateNumber;
    }

    public Driver(String name, String email, String phoneNumber, float rate, String drivingLicense, String carPlateNumber) {
        super(name, email, phoneNumber, rate);
        this.drivingLicense = drivingLicense;
        this.carPlateNumber = carPlateNumber;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }
}
