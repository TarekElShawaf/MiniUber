package com.example.miniuber.app.features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.miniuber.R;
import com.example.miniuber.app.features.riderFeatures.SignUpRiderActivity;
import com.example.miniuber.app.features.riderFeatures.MapsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextView register;
    private AppCompatButton signIn;
    private AppCompatEditText etEmail, etPassword;
    private int moduleOption;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        moduleOption = getIntent().getIntExtra("Module Choice", 0);
        initialize();
        setRegisterButton();
        setSignInButton();
    }

    private void initialize() {
        signIn = findViewById(R.id.signInButton);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        register = findViewById(R.id.registerText);
        progressBar = findViewById(R.id.loginProgressBar);
    }

    private void setSignInButton() {

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), "Fill All Fields", Toast.LENGTH_LONG).show();
                } else {
                    login();
                }


            }
        });
    }


    private void login(){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    verifyEmailAddress();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void verifyEmailAddress(){
        FirebaseUser user = auth.getCurrentUser();

        if(user.isEmailVerified() == true ){
            Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            progressBar.setVisibility(View.INVISIBLE);
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext(),"Please verify your account",Toast.LENGTH_LONG).show();
        }
    }

    private void setRegisterButton() {

        Intent intent = new Intent(SignInActivity.this, SignUpRiderActivity.class);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moduleOption == 1) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });
    }
}