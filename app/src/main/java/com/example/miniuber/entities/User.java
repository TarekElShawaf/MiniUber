package com.example.miniuber.entities;

import java.io.Serializable;

public  abstract  class User implements Serializable {

    private String name ;
    private String email ;
    private String phoneNumber ;
    private String userId ;
    private float rate ;


    public User(String name, String email, String phoneNumber, String userId, float rate) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.rate = rate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
