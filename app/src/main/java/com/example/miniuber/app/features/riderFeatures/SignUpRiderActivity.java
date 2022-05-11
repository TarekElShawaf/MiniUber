package com.example.miniuber.app.features.riderFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniuber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpRiderActivity extends AppCompatActivity {


    private AppCompatEditText etName,etEmail,etPassword,etPhoneNo;
    private AppCompatButton signUp;
    private FirebaseAuth auth;
    private DatabaseReference riderRef;
    private ProgressBar progressBar;
    private String name,email,password,phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_rider);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        initialize();
        setSignUpButton();
    }

    private void setSignUpButton() {

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                convertEditTextsToStrings();

                if(name .isEmpty() ||email .isEmpty() ||password .isEmpty() ||phoneNo .isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(),"Fill All Fields",Toast.LENGTH_LONG).show();
                }
                else{
                    createRiderAccount();
                }

            }
        });
    }

    private void convertEditTextsToStrings() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        phoneNo = etPhoneNo.getText().toString();
    }

//    private void sendEmailVerification() {
//        FirebaseUser user = auth.getCurrentUser();
//
//        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    //Email is sent successfully
//                    Rider rider = new Rider (name,email,password ,phoneNo,auth.getCurrentUser().getUid(),0);
//                    riderRef.push().setValue(rider);
//                    Toast.makeText(getBaseContext(), "Email is sent please verify your account", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getBaseContext(), SignInActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getBaseContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
    private void createRiderAccount() {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                       //     sendEmailVerification();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getBaseContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initialize(){
        etName = findViewById(R.id.signUpNameRider);
        etEmail = findViewById(R.id.signUpEmailRider);
        etPassword = findViewById(R.id.signUpPasswordRider);
        etPhoneNo = findViewById(R.id.signUpPhoneNumberRider);
        signUp = findViewById(R.id.btn_signupRider);
        auth = FirebaseAuth.getInstance();
        riderRef = FirebaseDatabase.getInstance().getReference("Users").child("Riders");
        progressBar = findViewById(R.id.progress_bar_signup_rider);
    }
}