package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.miniuber.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TripViewHolder> {
    ArrayList<String> trips;

    public RecyclerViewAdapter(ArrayList<String> trips) {
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
      //  String trip = trips.get(position);
       // holder.pt_driver_name.setText(trip.toString());



    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
    class TripViewHolder extends RecyclerView.ViewHolder {

    /*    TextView pt_pickup;
        TextView pt_destination;
        TextView pt_trip_fare;
        TextView pt_trip_date;
        TextView pt_trip_rate;*/
        TextView pt_driver_name;



        //holder class for recycler view
        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
         /*   pt_pickup = itemView.findViewById(R.id.pt_pickup);
            pt_destination = itemView.findViewById(R.id.pt_destination);
            pt_trip_fare = itemView.findViewById(R.id.pt_trip_fare);
            pt_trip_date = itemView.findViewById(R.id.pt_trip_date);
            pt_trip_rate = itemView.findViewById(R.id.pt_trip_rate);*/
           // pt_driver_name = itemView.findViewById(R.id.customitem_driver_name);
            // pt_car_type=itemView.findViewById(R.id.pt_car_type);
            //pt_car_color=itemView.findViewById(R.id.pt_car_color);
        }
    }

}
