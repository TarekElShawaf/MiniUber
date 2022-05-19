package com.example.miniuber.app.features.employeeFeatures.employeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.entities.Employee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class EmployeeInformationFragment extends Fragment {

    private TextView tvName,tvEmail,tvPhoneNo;




    public EmployeeInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_employee_information, container, false);
        initialize(v);
        DatabaseReference empRef = FirebaseDatabase.getInstance().getReference("Users").child("Employees");
        String correctPhoneNo = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();

        empRef.addListenerForSingleValueEvent(new ValueEventListener() {

            Employee employee;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    employee = postSnapshot.getValue(Employee.class);
                    assert employee != null;
                    if (employee.getPhoneNumber().equals(correctPhoneNo) ){
                        break;
                    }

                }
                tvName.setText(employee.getName());
                tvEmail.setText(employee.getEmail());
                tvPhoneNo.setText(employee.getPhoneNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });


        return v;


    }


    private void initialize(View v){
        tvName = v.findViewById(R.id.tv_display_employee_name);
        tvEmail = v.findViewById(R.id.tv_display_employee_email);
        tvPhoneNo = v.findViewById(R.id.tv_display_employee_phoneNo);
    }


}