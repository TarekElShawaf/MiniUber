package com.example.miniuber.app.features.riderFeatures.personalInfo;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    String userPhoneNumber;

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
            Toast.makeText(getContext(),userPhoneNumber,Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(),"Not Null",Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(getContext(),"PHONE nUMBER IS "+userPhoneNumber,Toast.LENGTH_LONG).show();
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users").child("Riders");
        Query query = users.orderByChild("phoneNumber").equalTo(userPhoneNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    name=dataSnapshot.child(userPhoneNumber).child("name").getValue(String.class);
                    email=dataSnapshot.child(userPhoneNumber).child("email").getValue(String.class);
                   // rate= dataSnapshot.child(userPhoneNumber).child("rate").getValue(float.class);
                    phone=userPhoneNumber;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });
        nameView = view.findViewById(R.id.nameView);
        nameView.setText("name is "+name);
        phoneView = view.findViewById(R.id.phoneView);
        phoneView.setText("Phone is : "+phone);
        emailView = view.findViewById(R.id.emailView);
        emailView.setText(email);

        rating = view.findViewById(R.id.ratingBar);
        rating.setRating(rate);
        ratingTxt = view.findViewById(R.id.ratingTxt);
        ratingTxt.setText(""+ rate);

        return view;
    }
}