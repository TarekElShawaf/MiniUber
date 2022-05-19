package com.example.miniuber.entities;

import java.io.Serializable;

public class Rider extends  User implements Serializable {


    public Rider(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    public Rider(String name, String email, String phoneNumber, float rate) {
        super(name, email, phoneNumber, rate);
    }

    public Rider() {
    }
}





