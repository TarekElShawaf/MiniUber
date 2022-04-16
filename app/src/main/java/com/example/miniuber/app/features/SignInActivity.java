package com.example.miniuber.app.features;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.miniuber.R;
import com.example.miniuber.app.features.driverFeatures.SignUpDriverActivity;
import com.example.miniuber.app.features.riderFeatures.SignUpRiderActivity;
import com.example.miniuber.app.features.riderFeatures.MapsActivity;

public class SignInActivity extends AppCompatActivity {

    private  TextView register ;
    private AppCompatButton signIn ;
    private int moduleOption ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        moduleOption= getIntent().getIntExtra("Module Choice",0);
        setRegisterButton();
        setSignInButton();
    }
    private void setSignInButton(){
        signIn=findViewById(R.id.signInButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
    private void setRegisterButton(){
        register=findViewById(R.id.registerText);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moduleOption==1){
                    Intent intent = new Intent(SignInActivity.this, SignUpRiderActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                else{
                    Intent intent = new Intent(SignInActivity.this, SignUpDriverActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });
    }
}