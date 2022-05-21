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
    String userType;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tripViewModel = new TripsViewModel(getActivity().getApplication());
        Trip tip = new Trip("home","1:4","college",2.3f,"25/30","+201111111111",3.5f,"+201142434195");
        //tripViewModel.insertTrip(tip);
        View v = inflater.inflate(R.layout.fragment_trips_history_fragment, container, false);
        Bundle data = getArguments();
        if (data != null) {
            userPhoneNumber = data.getString("userPhoneNumber");
            userType = data.getString("userType");

        }


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");

        RecyclerView rv =  v.findViewById(R.id.pt_fragment);

        adapter = new RecyclerViewAdapter(trips, new TripsInfoRecyclerViewListener() {
            @Override
            public void click(View view, int position) {

                Intent intent = new Intent(getContext(), TripInfoActivity.class);
                intent.putExtra("trip", adapter.getTrips().get(position));
                intent.putExtra("userType", userType);

                startActivity(intent);
            }

        }, getContext());
        if(userType.equals("rider")){
            adapter.setTrips(((ArrayList<Trip>) tripViewModel.getTripByRiderId(userPhoneNumber)));
        }
        else{
            adapter.setTrips(((ArrayList<Trip>) tripViewModel.getTripByDriverId(userPhoneNumber)));

        }

        rv.setAdapter(adapter);

        rv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);




        // Inflate the layout for this fragment
        return v;
    }
}

