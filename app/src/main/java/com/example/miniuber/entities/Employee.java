package com.example.miniuber.entities;

public class Employee extends User{

        public Employee(String name, String email, String phoneNumber, String userId, float rate) {
            super(name, email, phoneNumber, userId, rate);
        }
    public Employee() {
    }



    public Employee(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

}
