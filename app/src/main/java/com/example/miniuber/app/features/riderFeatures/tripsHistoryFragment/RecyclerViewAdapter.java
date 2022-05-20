package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.miniuber.R;
import com.example.miniuber.entities.Driver;
import com.example.miniuber.entities.Rider;
import com.example.miniuber.entities.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TripViewHolder> {
    ArrayList<Trip> trips;
    Driver driver_;

    public RecyclerViewAdapter(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_custom_item,null,false);
        TripViewHolder driverViewHolder = new TripViewHolder(view);
        return driverViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {

       /* String trip = trips.get(position);
        holder.pt_driver_name.setText(trip.toString());
*/
        Trip trip = trips.get(position);

        Driver driver = getDriverByPhoneNumber(trip.getDriverPhoneNo());

        holder.pt_driver_name.setText(driver.getName());
        holder. pt_arrival_time.setText(trip.getTime());
        holder.pt_trip_fare.setText(trip.getFare().toString());
        holder.pt_trip_date.setText(trip.getTripDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });



    }
     public Driver getDriverByPhoneNumber(String PhoneNumber){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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

  /*  public Rider getRiderByPhoneNumber(String PhoneNumber){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Riders");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()) {

                    rider_ = postSnapshot.getValue(Rider.class);
                    if(rider_.getPhoneNumber().equals( PhoneNumber))
                        break;


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return rider_;
    }*/

    @Override
    public int getItemCount() {
        return trips.size();
    }
    class TripViewHolder extends RecyclerView.ViewHolder {

     TextView pt_arrival_time;
        TextView pt_trip_fare;
        TextView pt_trip_date;
        TextView pt_driver_name;



        //holder class for recycler view
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            pt_arrival_time = itemView.findViewById(R.id.customitem_time);
            pt_trip_fare = itemView.findViewById(R.id.customitem_fare);
            pt_trip_date = itemView.findViewById(R.id.customitem_date);
            pt_driver_name = itemView.findViewById(R.id.customitem_driver_name);
            // pt_car_type=itemView.findViewById(R.id.pt_car_type);
            //pt_car_color=itemView.findViewById(R.id.pt_car_color);
        }
    }

}
