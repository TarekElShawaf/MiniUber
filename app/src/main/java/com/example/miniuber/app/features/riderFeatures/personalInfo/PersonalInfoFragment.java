package com.example.miniuber.app.features.riderFeatures.personalInfo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.commonFeatures.PhoneVerificationActivity;
import com.example.miniuber.entities.FormatChecker;
import com.example.miniuber.entities.Rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PersonalInfoFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String email ;
    String name ;
    String phone ;
    float rate;
    AppCompatEditText nameView,phoneView,emailView;
    private AppCompatButton saveButton;
    TextView ratingTxt;
    RatingBar rating ;
    String userPhoneNumber;
    Rider rider;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        Bundle data = getArguments();
        if(data!=null)
        {
            userPhoneNumber=data.getString("userPhoneNumber");
        }
        
        DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users").child("Riders");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    rider = snap.getValue(Rider.class);
                    if(rider.getPhoneNumber().equals(userPhoneNumber))
                    {
                        Toast.makeText(getContext(),rider.getPhoneNumber(),Toast.LENGTH_LONG).show();
                        //Get Values
                        String riderID = snap.getKey().toString();
                        name=rider.getName();
                        email=rider.getEmail().toString();
                        rate=rider.getRate();
                        phone=rider.getPhoneNumber();
                        //Assign IDs
                        nameView = view.findViewById(R.id.nameView);
                        phoneView = view.findViewById(R.id.phoneView);
                        emailView = view.findViewById(R.id.emailView);
                        rating = view.findViewById(R.id.ratingBar);
                        ratingTxt = view.findViewById(R.id.ratingTxt);
                        saveButton = view.findViewById(R.id.btn_save_info);
                        //Set Values
                        nameView.setText(name);
                        phoneView.setText(phone);
                        emailView.setText(email);
                        rating.setRating(rate);
                        ratingTxt.setText("("+ rate+")");
                        //Save Edited Values
                        saveButton.setOnClickListener(view -> {
                            String editedPhone = phoneView.getText().toString();
                            String editedName = nameView.getText().toString();
                            String editedEmail = emailView.getText().toString();
                            if (editedPhone.isEmpty() || editedName.isEmpty() || editedEmail.isEmpty() ) {

                                Toast.makeText(getContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();

                            }
                            else if(editedPhone.equals(phone)&&editedName.equals(name)&&editedEmail.equals(email))
                            {
                                Toast.makeText(getContext(), "No data changed", Toast.LENGTH_SHORT).show();
                            }
                            else if (!FormatChecker.isValidPhoneNo(editedPhone) || !FormatChecker.isValidEmail(editedEmail)) {

                                Toast.makeText(getContext(), "Incorrect phone number format or email format", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                DatabaseReference currentRiderRef = users.child(riderID);
                                Map<String,Object> riderMap = new HashMap<String,Object>();
                                riderMap.put("name", editedName);
                                riderMap.put("email", editedEmail);
                                riderMap.put("phoneNumber", editedPhone);
                                currentRiderRef.updateChildren(riderMap);
                                name=editedName;
                                email=editedEmail;
                                phone=editedPhone;
                                Toast.makeText(getContext(), "Data updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}
