package com.example.miniuber.entities;

import java.io.Serializable;

public  abstract  class User implements Serializable {

    private String name ;
    private String email ;
    private String phoneNumber ;
    private float rate ;


    public User(String name, String email, String phoneNumber, float rate) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.rate = rate;
    }
    public User(String name , String email ,String phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public User(){

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


}
