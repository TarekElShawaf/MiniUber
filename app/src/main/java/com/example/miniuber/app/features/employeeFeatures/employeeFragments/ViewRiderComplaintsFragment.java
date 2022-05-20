package com.example.miniuber.app.features.employeeFeatures.employeeFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.employeeFeatures.ComplaintDetailsActivity;
import com.example.miniuber.app.features.employeeFeatures.ComplaintsRecyclerViewAdapter;
import com.example.miniuber.entities.Complaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewRiderComplaintsFragment extends Fragment {

    private ComplaintsRecyclerViewAdapter adapter;
    private final DatabaseReference complaintsRef = FirebaseDatabase.getInstance().getReference("Complaints");

    private final ArrayList<Complaint> complaints = new ArrayList<>();


    public ViewRiderComplaintsFragment(){
        //required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_rider_complaints, container, false);

        RecyclerView rv = v.findViewById(R.id.rv_complaints);
        TextView noAvailableComplaints = v.findViewById(R.id.tv_no_available_complaints);


        complaintsRef.addValueEventListener(new ValueEventListener() {
            Complaint complaint;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaints.clear();
                if (snapshot.getValue() == null) {
                    noAvailableComplaints.setVisibility(View.VISIBLE);
                } else {

                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {

                        complaint = postSnapShot.getValue(Complaint.class);
                        complaints.add(complaint);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        adapter = new ComplaintsRecyclerViewAdapter(complaints, (v1, position) -> {

            Intent intent = new Intent(v1.getContext(), ComplaintDetailsActivity.class);
            intent.putExtra("complaint", complaints.get(position));
            startActivity(intent);
        });


       new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(v.getContext()));


        return v;
    }



    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            complaints.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();

            //complaint removed from firebase not implemented yet

        }
    };





}

