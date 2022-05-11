package com.example.miniuber;

import android.util.Patterns;

public class FormatChecker {

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidPhoneNo(String phone){
        return phone.length() > 6 && phone.length() < 16;
    }

}
