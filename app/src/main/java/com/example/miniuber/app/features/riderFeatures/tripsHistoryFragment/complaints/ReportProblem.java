package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.complaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.riderFeatures.riderMapsActivity.RiderMapsActivity;
import com.example.miniuber.entities.Complaint;
import com.example.miniuber.entities.Rider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportProblem extends AppCompatActivity {
    RadioButton radioButton;
    RadioButton radio;
    EditText editText;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    AppCompatButton button ;
    Complaint complaint;
    int trip_id ;
    int min = 200;
    int max = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        getSupportActionBar().hide();
        complaint=new Complaint();

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        //writeOtherProblem();
        trip_id=(Integer) getIntent().getSerializableExtra("tripID");
        button= findViewById(R.id.submitproblem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportProblem.this, RiderMapsActivity.class);
                startActivity(intent);
            }
        });


        Toast.makeText(this, "Trip ID: "+trip_id, Toast.LENGTH_SHORT).show();
    }
    String problem = new String();
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.accidentproblem:
                if (checked)
                    // Ninjas rule
                    problem="My driver was not wearing a face mask";
                complaint.setProblem("My driver was not wearing a face mask");
                    break;

            case R.id.maskproblem:
                if (checked)
                    // Ninjas rule
                    problem="I lost an item";
                complaint.setProblem("I lost an item");
                    break;

            case R.id.lostitemproblem:
                if (checked)
                    // Ninjas rule
                    problem="I was envolved in an accident";
                complaint.setProblem("I was envolved in an accident");
                    break;

            case R.id.driverproblem:
                if (checked)
                    // Ninjas rule
                    problem="My driver was rude or unprofessional";
                complaint.setProblem("My driver was rude or unprofessional");
                    break;
            case R.id.overchargeproblem:
                if (checked)
                    // Ninjas rule
                    problem="I was overcharged on my cash trip";
                complaint.setProblem("I was overcharged on my cash trip");
                    break;
            case R.id.differentplateproblem:
                if (checked)
                    // Ninjas rule

                    problem="The car had a different plate number";
                complaint.setProblem("The car had a different plate number");

                break;

        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");

        complaint.setTripId(trip_id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTime());
        complaint.setDate(date);
        int b = (int)(Math.random()*(max-min+1)+min);
        complaint.setComplaintID(b);
        myRef.push().setValue(complaint);
        Toast.makeText(this, "Complaint Submitted", Toast.LENGTH_SHORT).show();
    }

}