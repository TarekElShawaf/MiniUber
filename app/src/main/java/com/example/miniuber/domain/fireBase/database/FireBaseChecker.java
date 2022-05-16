package com.example.miniuber.domain.fireBase.database;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.miniuber.app.features.commonFeatures.PhoneVerificationActivity;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.entities.Rider;
import com.example.miniuber.entities.SignOption;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseChecker {

    private final int moduleOption;
    private final Context context;
    private final DatabaseReference myRef;

    public FireBaseChecker(int moduleOption, Context context) {
        this.moduleOption = moduleOption;
        this.context = context;
        this.myRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(ModuleOption.getReferenceName(moduleOption));
    }

    public void checkIfUserHasAnExistingAccountUponSigningIn(String fullNumber) {

        //DO NOT CHANGE THE "phoneNumber" spelling passed to Order BY CHILD
        myRef.orderByChild("phoneNumber").equalTo(fullNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    //Phone number already exists in database
                    handleIntentUponSigningIn(fullNumber);

                } else {
                    //Phone number does not exist in database
                    Toast.makeText(context, "Account does not exist, please create an account", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void checkIfRiderHasAnExistingAccountUponSignUp(Rider rider) {
        //if rider has an account , the function makes sure the rider cannot create another account
        //with the same phone number


        myRef.orderByChild("phoneNumber").equalTo(rider.getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    Toast.makeText(context, "Account already exists with the same phone number, try signing in",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //new User
                    handleIntentSigningUpRider(rider);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void handleIntentUponSigningIn(String fullNumber){
        Intent intent = new Intent(context, PhoneVerificationActivity.class);
        intent.putExtra("phoneNo", fullNumber);
        intent.putExtra("moduleOption", moduleOption);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        context.startActivity(intent);
    }
    private void handleIntentSigningUpRider(Rider rider){
        Intent intent = new Intent(context, PhoneVerificationActivity.class);
        intent.putExtra("rider", rider);
        intent.putExtra("moduleOption",moduleOption);
        intent.putExtra("signOption", SignOption.SIGN_UP_MODE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
