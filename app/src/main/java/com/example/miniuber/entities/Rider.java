package com.example.miniuber.entities;

import java.io.Serializable;

public class Rider extends  User implements Serializable {


    public Rider(String name, String email, String phoneNumber, String userId, float rate) {
        super(name, email, phoneNumber, userId, rate);
    }




}
