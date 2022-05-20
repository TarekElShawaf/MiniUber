package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.miniuber.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripsHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Trip trip;
    Rider rider_;
    Driver driver_;
    String userPhoneNumber;

    public TripsHistoryFragment() {

    }


    public static TripsHistoryFragment newInstance(String param1, String param2) {
        TripsHistoryFragment fragment = new TripsHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trips_history_fragment, container, false);
        Bundle data = getArguments();
        if (data != null) {
            userPhoneNumber = data.getString("userPhoneNumber");
        }


        RecyclerView rv = v.findViewById(R.id.pt_fragment);

        ArrayList<Trip> trips = new ArrayList<Trip>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    trip = postSnapshot.getValue(Trip.class);
                    if (trip.getRiderPhoneNo().equals(userPhoneNumber)) {

                        trips.add(trip);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(trips, new TripsInfoRecyclerViewListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), TripInfoActivity.class);
                intent.putExtra("trip", trips.get(position));
                startActivity(intent);

            }
        });

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }
}

