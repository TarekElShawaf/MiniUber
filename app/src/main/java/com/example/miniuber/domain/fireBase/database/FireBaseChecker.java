package com.example.miniuber.domain.fireBase.database;


import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.miniuber.entities.Car;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseChecker {

    private final Context context;
    private final DatabaseReference myRef;
    private ProgressBar progressBar;

    public FireBaseChecker(int moduleOption, Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
        this.myRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(ModuleOption.getReferenceName(moduleOption));

    }

    public void checkIfUserHasAnExistingAccountUponSigningIn(String fullNumber, CheckerHandler checkerHandler) {

        //DO NOT CHANGE THE "phoneNumber" spelling passed to Order BY CHILD
        myRef.orderByChild("phoneNumber").equalTo(fullNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    //existing user trying to sign in
                    checkerHandler.handle();

                } else {
                    //non-existent user trying to sign in
                    Toast.makeText(context, "Account does not exist, please create an account",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        });

    }

    public void checkIfUserHasAnExistingAccountUponSignUp(User user, CheckerHandler checkerHandler) {


        myRef.orderByChild("phoneNumber").equalTo(user.getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getValue() != null) {
                            Toast.makeText(context, "Account already exists with the same phone number, try signing in",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            //new User signing up
                            checkerHandler.handle();
                            progressBar.setVisibility(View.INVISIBLE);


                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                });

    }
    public void checkIfCarExists(Car car,CheckerHandler checkerHandler) {

        DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference("Cars");

        carsRef.orderByChild("plateNumber").equalTo(car.getPlateNumber().toUpperCase())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    //cars plate numbers should be BLOCK capital in firebase
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.getValue() != null) {
                            //car already exists in firebase
                            Toast.makeText(context, "the car already exists",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            //add new car to firebase
                            checkerHandler.handle();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                });


    }



}

