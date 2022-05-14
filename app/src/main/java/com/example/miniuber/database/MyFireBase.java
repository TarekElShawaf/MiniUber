package com.example.miniuber.database;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.example.miniuber.app.features.riderFeatures.RiderMapsActivity;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFireBase {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final PhoneAuthCredential credential;
    private final Context context;

    public MyFireBase(PhoneAuthCredential credential, Context context) {
        this.credential = credential;
        this.context = context;
    }


    public void signInUser() {

        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                handleIntent();

            } else {
                //verification unsuccessful.. display an error message
                handleFailedVerification(task);
            }

        });
    }

    public void createUserAccount(User user, int moduleOption) {

        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                addUserDataToDatabase(user, moduleOption);
                handleIntent();

            } else {
                //verification unsuccessful.. display an error message

                handleFailedVerification(task);

            }

        });

    }

    private void addUserDataToDatabase(User user, int moduleOption) {

        DatabaseReference myRef =
                FirebaseDatabase.getInstance().getReference("Users").child(ModuleOption.getReferenceName(moduleOption));
        String userID = auth.getCurrentUser().getUid();
        user.setUserId(userID);
        myRef.push().setValue(user);
    }

    private void handleIntent() {
        Intent intent = new Intent(context, RiderMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void handleFailedVerification(Task<AuthResult> task) {
        String message = "Something is wrong, we will fix it soon...";

        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            message = "Invalid code entered...";
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
