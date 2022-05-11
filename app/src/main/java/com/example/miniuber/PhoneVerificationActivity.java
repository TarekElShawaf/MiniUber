package com.example.miniuber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.miniuber.app.features.SignInActivity;
import com.example.miniuber.app.features.riderFeatures.MapsActivity;
import com.example.miniuber.entities.Rider;

import com.google.firebase.FirebaseException;



import com.goodiebag.pinview.Pinview;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.concurrent.TimeUnit;


public class PhoneVerificationActivity extends AppCompatActivity {

    Pinview pinview;
    String phoneNo;
    int signOption;
    AppCompatButton verify;
    FirebaseAuth auth;
    private String mVerificationId;
    Rider rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        getSupportActionBar().hide();
        initialize();

        if (signOption == SignInActivity.SIGN_UP_MODE){
            rider = (Rider) getIntent().getSerializableExtra("rider");
            sendVerificationCode(rider.getPhoneNumber());
        }
        else{
            sendVerificationCode(phoneNo);
        }
        setVerifyButton();




    }

    private void initialize() {
        pinview = findViewById(R.id.pinview);
        phoneNo = getIntent().getStringExtra("phoneNo");
        signOption = getIntent().getIntExtra("signOption",0);
        verify = findViewById(R.id.btn_verify);
        auth = FirebaseAuth.getInstance();



    }

    private void setVerifyButton() {

        verify.setOnClickListener(view -> {
            String code = pinview.getValue();
            if (code.isEmpty() || code.length() < 6) {
                Toast.makeText(getBaseContext(),"insert the code in the correct format",Toast.LENGTH_LONG).show();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code);
        });

    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                pinview.setValue(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneVerificationActivity.this, task -> {
                    if (task.isSuccessful()) {
                        //verification successful we will start the Maps activity
                        if(signOption == 1){

                            //Rider is signingUP

                            DatabaseReference ridersRef =
                                    FirebaseDatabase.getInstance().getReference("Users").child("Riders");
                            String riderID = auth.getCurrentUser().getUid();
                            rider.setUserId(riderID);
                            ridersRef.push().setValue(rider);


                        }
                        Intent intent = new Intent(PhoneVerificationActivity.this, MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {

                        //verification unsuccessful.. display an error message

                        String message = "Something is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }

                        Toast.makeText(getBaseContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
    }
}