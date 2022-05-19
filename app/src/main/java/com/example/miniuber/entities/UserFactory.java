package com.example.miniuber.entities;

public class UserFactory {

    public static User getUser(String userType, String name , String email , String phoneNo){

        if (userType.equalsIgnoreCase("Rider")){
            return new Rider(name,email,phoneNo);
        }
        else if(userType.equalsIgnoreCase("Driver")){
            return new Driver(name,email,phoneNo);
        }
        else if (userType.equalsIgnoreCase("Employee")){

            return new Employee(name,email,phoneNo);
        }
        return null;
    }

}
