package com.example.miniuber.app.features.commonFeatures;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.miniuber.app.features.commonFeatures.registration.SignInActivity;
import com.example.miniuber.entities.ModuleOption;
import com.example.miniuber.R;

public class ModuleSelectorActivity extends AppCompatActivity {

    private AppCompatButton driverButton;
    private AppCompatButton riderButton;
    private AppCompatButton employeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      getSupportActionBar().hide();
        setDriverButton();
        setRiderButton();
        setEmployeeButton();

    }

    void setEmployeeButton() {
        employeeButton = findViewById(R.id.employeeButton);
        employeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModuleSelectorActivity.this, SignInActivity.class);
                intent.putExtra("Module Choice", ModuleOption.EMPLOYEE);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    void setRiderButton() {
        riderButton = findViewById(R.id.riderButton);
        riderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModuleSelectorActivity.this, SignInActivity.class);
                intent.putExtra("Module Choice",ModuleOption.RIDER);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    void setDriverButton() {
        driverButton = findViewById(R.id.driverButton);

        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModuleSelectorActivity.this, SignInActivity.class);
                intent.putExtra("Module Choice",ModuleOption.DRIVER);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}