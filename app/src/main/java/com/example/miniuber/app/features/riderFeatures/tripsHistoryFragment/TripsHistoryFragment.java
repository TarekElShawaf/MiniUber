package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.employeeFeatures.ComplaintsRecyclerViewAdapter;
import com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.TripInfo.TripInfoActivity;
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


public class TripsHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Trip trip;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<Trip> trips = new ArrayList<>();
    TripsViewModel tripViewModel;

    String userPhoneNumber;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tripViewModel = new TripsViewModel(getActivity().getApplication());
        View v = inflater.inflate(R.layout.fragment_trips_history_fragment, container, false);
        Bundle data = getArguments();
        if (data != null) {
            userPhoneNumber = data.getString("userPhoneNumber");

        }


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
        //FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    trip = postSnapshot.getValue(Trip.class);
                    Log.d("Nouran : " ,trip.getTripDate());
                    Log.d("Nouran", userPhoneNumber);
                    Log.d("Nouran", trip.getRiderPhoneNo());


                    if (trip.getRiderPhoneNo().equals(userPhoneNumber)) {
                        Log.d("Nouran : Dest", trip.getDestination());
                        trips.add(new Trip(trip.getPickupPoint(), trip.getTime(), trip.getDestination(), trip.getFare(), trip.getTripDate(), trip.getDriverPhoneNo(),trip.getRate(),trip.getRiderPhoneNo()));
                        //tripViewModel.insertTrip(trip);
                   }
                }
               //adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // = new RecyclerViewAdapter(trips, adapter.listener);
       //adapter = new RecyclerViewAdapter(trips, adapter.listener);
        RecyclerView rv =  v.findViewById(R.id.pt_fragment);
        Trip testTrip = new Trip("home","15:4","city stars",2.3f,"22/5","011515",2.8f,userPhoneNumber);
        trips.add(testTrip);
       // tripViewModel.insertTrip(testTrip);

       Log.d("Nouran Trips size  : ", +tripViewModel.getAllTripsData().size()+"");


        adapter = new RecyclerViewAdapter(trips, new TripsInfoRecyclerViewListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), TripInfoActivity.class);
                intent.putExtra("trip", trips.get(position));
                startActivity(intent);
            }

        }, getContext());
        adapter.setTrips((ArrayList<Trip>) tripViewModel.getAllTripsData());
        rv.setAdapter(adapter);

        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);




        // Inflate the layout for this fragment
        return v;
    }
}

