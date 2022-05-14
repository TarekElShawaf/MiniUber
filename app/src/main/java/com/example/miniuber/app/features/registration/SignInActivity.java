package com.example.miniuber.app.features.registration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.example.miniuber.entities.FormatChecker;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.app.features.commonFeatures.PhoneVerificationActivity;
import com.example.miniuber.R;

import com.example.miniuber.entities.Rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;


public class SignInActivity extends AppCompatActivity {


    public static final int SIGN_UP_MODE = 1;
    public static final int SIGN_IN_MODE = 2;
    private AppCompatButton signInAndSignUpBtn;

    private int moduleOption;
    private ProgressBar progressBar;
    private AppCompatEditText etPhoneNo, etEmail, etName;

    private CountryCodePicker countryCodePicker;
    private TextView loginMessage, tvSignUp, tvSignIn;
    private int signOption = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        moduleOption = getIntent().getIntExtra("Module Choice", 0);
        initialize();


        if (moduleOption == ModuleOption.RIDER) {

            signOption = 1;
            setSignUpViewForRider();

            setSignInTextViewClick();
            setSignUpTextViewClick();
        }


        setSignInAndSignUpButton();
    }

    private void initialize() {
        signInAndSignUpBtn = findViewById(R.id.btn_signIn);
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
        signInAndSignUpBtn.setText("Sign Up");

    }

    private void setSignInViewForRider() {

        tvSignIn.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
        tvSignUp.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.grey));
        etEmail.setVisibility(View.GONE);
        etName.setVisibility(View.GONE);
        loginMessage.setVisibility(View.VISIBLE);
        etPhoneNo.getText().clear();
        signInAndSignUpBtn.setText("Sign In");

    }


    private void setSignInTextViewClick() {

        tvSignIn.setOnClickListener(view -> {
            setSignInViewForRider();
            signOption = SignInActivity.SIGN_IN_MODE;

        });
    }

    private void setSignUpTextViewClick() {

        tvSignUp.setOnClickListener(view -> {
            setSignUpViewForRider();
            signOption = SignInActivity.SIGN_UP_MODE;
        });
    }


    private void setSignInAndSignUpButton() {

        signInAndSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String phoneNoWithoutCountryCode = etPhoneNo.getText().toString();

                if (phoneNoWithoutCountryCode.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (!FormatChecker.isValidPhoneNo(phoneNoWithoutCountryCode)) {

                    Toast.makeText(getBaseContext(), "Incorrect phone number format", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (signOption == SignInActivity.SIGN_UP_MODE) {
                    handleSigningUpRider(phoneNoWithoutCountryCode);
                    progressBar.setVisibility(View.INVISIBLE);

                } else {

                    String fullNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNoWithoutCountryCode;
                    checkIfUserHasAnExistingAccountUponSigningIn(moduleOption, fullNumber);
                    progressBar.setVisibility(View.INVISIBLE);


                }


            }
        });
    }

    private void handleSigningUpRider(String phoneNoWithoutCountryCode) {
        //Rider is trying to sign up  , only rider
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        if (email.isEmpty() || name.isEmpty()) {
            Toast.makeText(getBaseContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();
        } else if (!FormatChecker.isValidEmail(email)) {
            Toast.makeText(getBaseContext(), "Incorrect email format", Toast.LENGTH_LONG).show();
        } else {
            //sign up rider
            String fullNumber = "+" + countryCodePicker.getSelectedCountryCode() + phoneNoWithoutCountryCode;
            checkIfRiderHasAnExistingAccountUponSignUp(name,email,fullNumber);

        }
    }

    private void checkIfUserHasAnExistingAccountUponSigningIn(int moduleOption, String fullNumber) {
        //function checks if user has an existing account in order to sign in

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(ModuleOption.getReferenceName(moduleOption));


        myRef.orderByChild("phoneNumber").equalTo(fullNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    //Phone number already exists in database
                    Intent intent = new Intent(getBaseContext(), PhoneVerificationActivity.class);
                    intent.putExtra("phoneNo", fullNumber);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent);
                } else {
                    //Phone number does not exist in database
                    Log.d("ZOZ", "Sa7by account does not exist");
                    Toast.makeText(getBaseContext(), "Account does not exist, please create an account", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void checkIfRiderHasAnExistingAccountUponSignUp(String name,String email,String fullNumber) {
        //if rider has an account , the function makes sure the rider cannot create another account
        //with the same phone number

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users")
                .child("Riders");


        myRef.orderByChild("phoneNumber").equalTo(fullNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    Toast.makeText(getBaseContext(), "Account already exists with the same phone number, try signing in",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //new User
                    Toast.makeText(getBaseContext(), "You are a new user",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), PhoneVerificationActivity.class);

                    Rider rider = new Rider(name,email,fullNumber,"",0);
                    intent.putExtra("rider", rider);
                    intent.putExtra("moduleOption",moduleOption);
                    intent.putExtra("signOption", SignInActivity.SIGN_UP_MODE);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }


}