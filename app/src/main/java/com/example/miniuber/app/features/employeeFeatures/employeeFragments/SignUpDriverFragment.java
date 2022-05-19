package com.example.miniuber.app.features.employeeFeatures.employeeFragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.domain.fireBase.database.FireBaseChecker;
import com.example.miniuber.entities.Driver;
import com.example.miniuber.entities.FormatChecker;
import com.example.miniuber.entities.ModuleOption;

import com.example.miniuber.entities.UserFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class SignUpDriverFragment extends Fragment {

    private AppCompatEditText etName, etEmail, etDrivingLicense, etPhoneNo;
    private CountryCodePicker countryCode;
    private AppCompatButton signUp;
    private ProgressBar progressBar;
    private String name, email, drivingLicense, phoneNoWithoutCode;


    public SignUpDriverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_sign_up_driver, container, false);
        initialize(v);


        signUp.setOnClickListener(
                view -> {
                    convertEditTextsToStrings();

                    if (name.isEmpty() || email.isEmpty() || phoneNoWithoutCode.isEmpty() ||
                            drivingLicense.isEmpty()) {
                        Toast.makeText(v.getContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();
                    } else if (!FormatChecker.isValidEmail(email) || !FormatChecker.isValidPhoneNo(phoneNoWithoutCode)) {
                        Toast.makeText(v.getContext(), "your phone number or email has incorrect format"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        String fullPhoneNo = "+" + countryCode.getSelectedCountryCode() + phoneNoWithoutCode;
                        FireBaseChecker checker = new FireBaseChecker(ModuleOption.DRIVER, v.getContext(), progressBar);
//                        Driver driver = new Driver(name, email, fullPhoneNo, "", 0, drivingLicense);
                        Driver driver = (Driver) UserFactory.getUser("Driver",name,email,fullPhoneNo);
                        driver.setDrivingLicense(drivingLicense);
                        checker.checkIfUserHasAnExistingAccountUponSignUp(driver, () ->  addDriverDataToDatabase(driver,v));

                    }
                });
        return v;

    }

    private void initialize(View v) {
        etName = v.findViewById(R.id.signUpNameDriver);
        etEmail = v.findViewById(R.id.signUpEmailDriver);
        etDrivingLicense = v.findViewById(R.id.signUpDriverLicense);
        etPhoneNo = v.findViewById(R.id.signUpPhoneNumberDriver);
        countryCode = v.findViewById(R.id.country_code_picker_driver);
        signUp = v.findViewById(R.id.btn_signupDriver);
        progressBar = v.findViewById(R.id.progress_bar_signup_driver);
    }

    private void convertEditTextsToStrings() {
        name = Objects.requireNonNull(etName.getText()).toString();
        email = Objects.requireNonNull(etEmail.getText()).toString();
        drivingLicense = Objects.requireNonNull(etDrivingLicense.getText()).toString();
        phoneNoWithoutCode = Objects.requireNonNull(etPhoneNo.getText()).toString();

    }

    private void clearInput() {
        Objects.requireNonNull(etName.getText()).clear();
        Objects.requireNonNull(etEmail.getText()).clear();
        Objects.requireNonNull(etPhoneNo.getText()).clear();
        Objects.requireNonNull(etDrivingLicense.getText()).clear();
    }
    public  void addDriverDataToDatabase(Driver driver,View v) {
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference("Users").child("Drivers");

        driverRef.push().setValue(driver).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(v.getContext(), "Account Added Successfully", Toast.LENGTH_SHORT).show();
                clearInput();
            }
    });


}
}