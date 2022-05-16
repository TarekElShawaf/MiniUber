package com.example.miniuber.app.features.riderFeatures.personalInfo;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.miniuber.R;
import com.example.miniuber.entities.Rider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    TextView ratingTxt;
    RatingBar rating ;

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
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");
        Query query = users.orderByChild("name").equalTo("TarekElShawaf");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Rider currentUser = dataSnapshot.getValue(Rider.class);
                name= currentUser.getName();
                email=currentUser.getEmail();
                phone=currentUser.getPhoneNumber();
                rate= currentUser.getRate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        nameView = view.findViewById(R.id.nameView);
        nameView.setText(name);
        phoneView = view.findViewById(R.id.phoneView);
        phoneView.setText(phone);
        emailView = view.findViewById(R.id.emailView);
        emailView.setText(email);

        rating = view.findViewById(R.id.ratingBar);
        rating.setRating(rate);
        ratingTxt = view.findViewById(R.id.ratingTxt);
        ratingTxt.setText(""+ rate);

        return view;
    }
}