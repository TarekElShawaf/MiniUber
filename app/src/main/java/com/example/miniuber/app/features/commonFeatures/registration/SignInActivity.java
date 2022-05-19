package com.example.miniuber.app.features.commonFeatures.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.miniuber.app.features.commonFeatures.PhoneVerificationActivity;
import com.example.miniuber.domain.fireBase.database.FireBaseChecker;
import com.example.miniuber.entities.FormatChecker;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.R;

import com.example.miniuber.entities.Rider;
import com.example.miniuber.entities.SignOption;
import com.example.miniuber.entities.UserFactory;
import com.hbb20.CountryCodePicker;

import java.util.Objects;


public class SignInActivity extends AppCompatActivity {


    private AppCompatButton button;

    private int moduleOption;
    private int signOption = SignOption.SIGN_IN_MODE;
    private ProgressBar progressBar;
    private AppCompatEditText etPhoneNo, etEmail, etName;

    private CountryCodePicker countryCodePicker;
    private TextView loginMessage, tvSignUp, tvSignIn;
    FireBaseChecker checker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        moduleOption = getIntent().getIntExtra("Module Choice", 0);
        initialize();
        checker = new FireBaseChecker(moduleOption,getBaseContext(),progressBar);

        if (moduleOption == ModuleOption.RIDER) {

            signOption = SignOption.SIGN_UP_MODE;
            setSignUpButton();
            setSignUpViewForRider();
            setSignInTextViewClick();
            setSignUpTextViewClick();

        }

        else{
            setSignInButton();
        }

    }

    private void initialize() {
        button = findViewById(R.id.btn_signIn);
        progressBar = findViewById(R.id.loginProgressBar);
        etPhoneNo = findViewById(R.id.et_phoneNo);
        countryCodePicker = findViewById(R.id.country_code_picker);
        loginMessage = findViewById(R.id.tv_loginMessage);
        tvSignUp = findViewById(R.id.tv_signUp);
        tvSignIn = findViewById(R.id.tv_signIn);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        etPhoneNo.getText().clear();
    }

    //Toggles Visibilities for different components
    private void setSignUpViewForRider() {
        tvSignUp.setVisibility(View.VISIBLE);
        tvSignUp.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
        tvSignIn.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.grey));
        etEmail.setVisibility(View.VISIBLE);
        etName.setVisibility(View.VISIBLE);
        loginMessage.setVisibility(View.GONE);
        etPhoneNo.getText().clear();
        button.setText("Sign Up");

    }

    private void setSignInViewForRider() {

        tvSignIn.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
        tvSignUp.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.grey));
        etEmail.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        loginMessage.setVisibility(View.VISIBLE);
        etPhoneNo.getText().clear();
        button.setText("Sign In");

    }


    private void setSignInTextViewClick() {

        tvSignIn.setOnClickListener(view -> {
            setSignInViewForRider();
            signOption = SignOption.SIGN_IN_MODE;
            setSignInButton();

        });
    }

    private void setSignUpTextViewClick() {

        tvSignUp.setOnClickListener(view -> {
            setSignUpViewForRider();
            signOption = SignOption.SIGN_UP_MODE;
            setSignUpButton();
        });
    }



    private void setSignInButton(){
        button.setOnClickListener(view -> {
            String phoneNoWithoutCountryCode = etPhoneNo.getText().toString();

            if (phoneNoWithoutCountryCode.isEmpty() || !FormatChecker.isValidPhoneNo(phoneNoWithoutCountryCode)) {
                Toast.makeText(getBaseContext(), "enter your phone number in the correct format"
                        , Toast.LENGTH_SHORT).show();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                String fullNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNoWithoutCountryCode;
                checker.checkIfUserHasAnExistingAccountUponSigningIn(fullNumber, () -> {
                    Intent intent = new Intent(getBaseContext(), PhoneVerificationActivity.class);
                    intent.putExtra("phoneNo", fullNumber);
                    intent.putExtra("moduleOption", moduleOption);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    startActivity(intent);
                });
            }
        });
    }

    private void setSignUpButton(){
        button.setOnClickListener(view -> {

            String phoneNoWithoutCountryCode = etPhoneNo.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();

            if (phoneNoWithoutCountryCode.isEmpty() || name.isEmpty() || email.isEmpty() ) {

                Toast.makeText(getBaseContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();

            } else if (!FormatChecker.isValidPhoneNo(phoneNoWithoutCountryCode) ||
                    !FormatChecker.isValidEmail(email)) {

                Toast.makeText(getBaseContext(), "Incorrect phone number format or email format", Toast.LENGTH_SHORT).show();
            }
            else {
                progressBar.setVisibility(View.INVISIBLE);

                String fullNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNoWithoutCountryCode;
                //Rider rider = new Rider(name,email,fullNumber,"",0);
                Rider rider = (Rider) UserFactory.getUser("Rider",name,email,fullNumber);
                checker.checkIfUserHasAnExistingAccountUponSignUp(rider, () -> {
                    Intent intent = new Intent(getBaseContext(), PhoneVerificationActivity.class);
                    intent.putExtra("rider", rider);
                    intent.putExtra("moduleOption", moduleOption);
                    intent.putExtra("signOption", SignOption.SIGN_UP_MODE);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    startActivity(intent);
                });


            }


        });
    }


}