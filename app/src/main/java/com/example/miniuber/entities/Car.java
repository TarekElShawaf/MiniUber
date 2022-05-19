package com.example.miniuber.entities;

public class Car {

    private String model;
    private int year;
    private String color;
    private String plateNumber;
    private String driverId;


    public Car() {
    }
    public Car(String model, int year, String color, String plateNumber) {
        this.model = model;
        this.year = year;
        this.color = color;
        this.plateNumber = plateNumber;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
