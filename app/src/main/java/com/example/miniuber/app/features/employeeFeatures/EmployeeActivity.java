package com.example.miniuber.app.features.employeeFeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.employeeFeatures.employeeFragments.AddCarFragment;
import com.example.miniuber.app.features.employeeFeatures.employeeFragments.AssignCarToDriverFragment;
import com.example.miniuber.app.features.employeeFeatures.employeeFragments.EmployeeInformationFragment;
import com.example.miniuber.app.features.employeeFeatures.employeeFragments.SignUpDriverFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class EmployeeActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.defaultBackground));
        initialize();

        navigationView.setNavigationItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nav_account_info){
                replaceFragment(new EmployeeInformationFragment());
            }
            else if(item.getItemId() == R.id.nav_sign_up_driver){
                replaceFragment(new SignUpDriverFragment());
            }
            else if(item.getItemId() == R.id.nav_add_car){

                replaceFragment(new AddCarFragment());
            }
            else if(item.getItemId() == R.id.nav_assign_car_to_driver){

                replaceFragment(new AssignCarToDriverFragment());
            }
            else if(item.getItemId() == R.id.nav_logout){
                //Take an action to sign out user, won't need a fragment
                Toast.makeText(this, "Sign employee out", Toast.LENGTH_SHORT).show();

            }
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });


    }
    private void initialize(){
        navigationView = findViewById(R.id.nav_view_employee);
        drawerLayout = findViewById(R.id.drawer_layout_employee);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container_employee,fragment);
        transaction.commit();
    }
}