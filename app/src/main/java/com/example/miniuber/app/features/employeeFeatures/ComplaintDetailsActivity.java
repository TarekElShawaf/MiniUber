package com.example.miniuber.app.features.employeeFeatures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.entities.Complaint;
import com.example.miniuber.entities.Trip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ComplaintDetailsActivity extends AppCompatActivity {

    private TextView pickUpPoint,destination,rate,fare,problem,driverPhoneNo,riderPhoneNo,duration,date;
    private Complaint complaint;
    private final DatabaseReference tripRef = FirebaseDatabase.getInstance().getReference("Trips");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialize();
        problem.setMovementMethod(new ScrollingMovementMethod()); //makes textView scrollable

        complaint = (Complaint) getIntent().getSerializableExtra("complaint");


        tripRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Trip trip;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    trip = postSnapshot.getValue(Trip.class);
                    assert trip != null;
                    if (trip.getTripId() == complaint.getTripId() ){
                        break;
                    }

                }
                setTextViews(trip);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialize(){
        pickUpPoint = findViewById(R.id.tv_trip_pickup_point);
        destination = findViewById(R.id.tv_trip_destination);
        rate = findViewById(R.id.tv_trip_rate);
        fare = findViewById(R.id.tv_trip_fare);
        driverPhoneNo = findViewById(R.id.tv_trip_driver_phoneNo);
        problem = findViewById(R.id.tv_trip_problem);
        riderPhoneNo = findViewById(R.id.tv_trip_rider_phoneNo);
        duration = findViewById(R.id.tv_trip_arrival_time);
        date = findViewById(R.id.tv_trip_date);
    }
    private void setTextViews(Trip trip){
        pickUpPoint.setText(trip.getPickupPoint());
        destination.setText(trip.getDestination());
        rate.setText(String.valueOf(trip.getRate()));
        fare.setText(String.valueOf(trip.getFare()));
        driverPhoneNo.setText(String.valueOf(trip.getDriverPhoneNo()));
        problem.setText(String.valueOf(complaint.getProblem()));
        riderPhoneNo.setText(String.valueOf(trip.getRiderPhoneNo()));
        duration.setText(trip.getTime());
        date.setText(trip.getTripDate());
    }


}