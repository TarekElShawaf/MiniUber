package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.TripInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.miniuber.R;
import com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.complaints.ReportProblem;
import com.example.miniuber.entities.Driver;
import com.example.miniuber.entities.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class TripInfoActivity extends AppCompatActivity implements Serializable {
    TextView date;
    TextView time;
    TextView pickup_location;
    TextView destination_location;
    TextView fare;
    TextView driver_name;
    TextView rate;
    Trip trip;
    Driver driver_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        trip = (Trip) getIntent().getSerializableExtra("trip");

        date = findViewById(R.id.tripdate);
        time = findViewById(R.id.triptime);
        pickup_location = findViewById(R.id.trippickup);
        destination_location = findViewById(R.id.tripdestination);
        fare = findViewById(R.id.tripfare);
        rate = findViewById(R.id.triprate);
        driver_name = findViewById(R.id.tripdriver);


       date.setText(trip.getTripDate());
        time.setText(trip.getTime());
        pickup_location.setText(trip.getPickupPoint());
        destination_location.setText(trip.getDestination());
        fare.setText(trip.getFare().toString());
        rate.setText(Float.toString(trip.getRate()));
        driver_name.setText(trip.getDriverPhoneNo());





    }
    public Driver getDriverByPhoneNumber(String PhoneNumber){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()) {

                    driver_ = postSnapshot.getValue(Driver.class);
                    if(driver_.getPhoneNumber().equals( PhoneNumber))
                        break;


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return driver_;
    }


    public void makeComplaint(View view) {
        Intent intent=new Intent(getApplicationContext(), ReportProblem.class);
        intent.putExtra("tripID",trip.getTripId());
        startActivity(intent);
    }
}