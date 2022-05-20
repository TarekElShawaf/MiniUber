package com.example.miniuber.app.features.riderFeatures.tripsHistoryFragment.complaints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.miniuber.R;
import com.example.miniuber.entities.Complaint;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportProblem extends AppCompatActivity {
    RadioButton radioButtonOther;
    EditText editText;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    Complaint complaint;
    int trip_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        radioButtonOther =findViewById(R.id.otherproblem);
        editText=findViewById(R.id.problem_msg);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        trip_id=(Integer) getIntent().getSerializableExtra("tripID");
    }

    public void writeOtherProblem(View view) {
        if(radioButtonOther.isChecked()){
            editText.setVisibility(view.GONE);
        }
        else
        {
            editText.setVisibility(view.VISIBLE);
        }

    }

    public void submitProblem(View view) {
        String problem;
        if(radioButtonOther.isChecked()) {
            problem = editText.getText().toString();

        }
        else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton) findViewById(selectedId);
            problem=selectedRadioButton.getText().toString();

        }
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