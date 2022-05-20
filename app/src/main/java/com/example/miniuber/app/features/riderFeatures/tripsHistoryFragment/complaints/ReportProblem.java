package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.complaints;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.entities.Complaint;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        getSupportActionBar().hide();
        radioButton =findViewById(R.id.otherproblem);
        editText=findViewById(R.id.problem_msg);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        //writeOtherProblem();
        trip_id=(Integer) getIntent().getSerializableExtra("tripID");
        button= findViewById(R.id.submitproblem);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProblem();
            }
        });

        Toast.makeText(this, "Trip ID: "+trip_id, Toast.LENGTH_SHORT).show();
    }

    public void writeOtherProblem( ) {
        if(radioButton.isChecked()){
            editText.setVisibility(View.VISIBLE);
        }
        else
        {
            editText.setVisibility(View.INVISIBLE);
        }

    }

    public void submitProblem( ) {
        String problem;
            radio= (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            problem=radio.getText().toString();


        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Trips");
        complaint.setProblem(problem);
        complaint.setTripId(trip_id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTime());
        complaint.setDate(date);
        complaint.setComplaintID(5);



        myRef.push().setValue(complaint);

    }
}