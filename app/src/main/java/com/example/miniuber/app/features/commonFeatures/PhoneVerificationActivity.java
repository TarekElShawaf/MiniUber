package com.example.miniuber.app.features.commonFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.Toast;

import com.example.miniuber.database.MyFireBase;
import com.example.miniuber.R;
import com.example.miniuber.app.features.registration.SignInActivity;

import com.example.miniuber.entities.Rider;

import com.google.firebase.FirebaseException;


import com.goodiebag.pinview.Pinview;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class PhoneVerificationActivity extends AppCompatActivity {

    private Pinview pinView;
    private String phoneNo;
    public int signOption, moduleOption;
    private AppCompatButton resendOTP;
    private String mVerificationId;
    private Rider rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialize();

        if (signOption == SignInActivity.SIGN_UP_MODE) {
            rider = (Rider) getIntent().getSerializableExtra("rider");
            sendVerificationCode(rider.getPhoneNumber());
        } else {
            sendVerificationCode(phoneNo);
        }

        pinView.setPinViewEventListener(
                (pinView, fromUser) -> verifyVerificationCode(pinView.getValue()));

        setResendOTPButton();

    }

    private void initialize() {
        pinView = findViewById(R.id.pinview);
        phoneNo = getIntent().getStringExtra("phoneNo");
        signOption = getIntent().getIntExtra("signOption", 0);
        moduleOption = getIntent().getIntExtra("moduleOption", 0); //not working
        resendOTP = findViewById(R.id.btn_resendOTP);

    }

    private void setResendOTPButton() {

        resendOTP.setOnClickListener(view -> sendVerificationCode(phoneNo));

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
                pinView.setValue(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            resendOTP.setEnabled(false);
            resendOTP.setAlpha(0.50f);
            resendOTP.setBackgroundResource(R.drawable.disabled_resendotp_button);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            resendOTP.setAlpha(1);
            resendOTP.setEnabled(true);
            resendOTP.setBackgroundResource(R.drawable.roundedcorners);
        }
    };

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        MyFireBase myFireBase = new MyFireBase(credential, getBaseContext(),moduleOption);

        if (signOption != 1) {
            //signing the user
            myFireBase.signInUser();
        } else {
            //user is creating a new account
            myFireBase.createUserAccount(rider);

        }


    }

}