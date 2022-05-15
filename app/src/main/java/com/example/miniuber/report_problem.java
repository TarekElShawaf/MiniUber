package com.example.miniuber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class report_problem extends AppCompatActivity {
    RadioButton radioButtonOther;
    EditText editText;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        radioButtonOther =findViewById(R.id.otherproblem);
        editText=findViewById(R.id.problem_msg);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
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
        int trip_id ;
        String problem;
        if(radioButtonOther.isChecked()) {
             problem = editText.getText().toString();

        }
        else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            selectedRadioButton = (RadioButton) findViewById(selectedId);
            problem=selectedRadioButton.getText().toString();

        }

    }
}