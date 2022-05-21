package com.example.miniuber.entities;

public class Car {

    private String model;
    private int year;
    private String color;
    private String plateNumber;
    private String driverPhoneNo;
    private Driver driver;
    private Location location ;


    public Car(String model, int year, String color, String plateNumber, String driverPhoneNo) {
        this.model = model;
        this.year = year;
        this.color = color;
        this.plateNumber = plateNumber;
        this.driverPhoneNo = driverPhoneNo;
    }
    public Car(String model, int year, String color, String plateNumber) {
        this.model = model;
        this.year = year;
        this.color = color;
        this.plateNumber = plateNumber;

    }

    public Car() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverPhoneNo() {
        return driverPhoneNo;
    }

    public void setDriverPhoneNo(String driverPhoneNo) {
        this.driverPhoneNo = driverPhoneNo;
    }
}
