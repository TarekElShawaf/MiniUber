package com.example.miniuber.app.features.employeeFeatures.employeeFragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.miniuber.R;
import com.example.miniuber.domain.fireBase.database.FireBaseChecker;
import com.example.miniuber.entities.Car;
import com.example.miniuber.entities.FormatChecker;
import com.example.miniuber.entities.ModuleOption;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class AddCarFragment extends Fragment {

    private AppCompatEditText etModel, etYear, etColor, etPlateNo;
    private AppCompatButton addCar;
    private String model, year, color, plateNo;

    private ProgressBar progressBar;


    public AddCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_car, container, false);
        initialize(v);

        addCar.setOnClickListener(view -> {
            convertEditTextsToStrings();
            if (model.isEmpty() || year.isEmpty() || color.isEmpty() || plateNo.isEmpty()) {
                Toast.makeText(v.getContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();
            } else if (!FormatChecker.isValidCarYear(year)) {
                Toast.makeText(v.getContext(), "car year is invalid", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                Car car = new Car(model, Integer.parseInt(year), color, plateNo);
                FireBaseChecker checker = new FireBaseChecker(ModuleOption.CAR, v.getContext(), progressBar);
                checker.checkIfCarExists(car, () -> addCarDataToDatabase(car,v));

            }
        });


        return v;
    }

    private void initialize(View v) {
        etModel = v.findViewById(R.id.car_model);
        etYear = v.findViewById(R.id.car_year);
        etColor = v.findViewById(R.id.car_color);
        etPlateNo = v.findViewById(R.id.car_plate_no);
        addCar = v.findViewById(R.id.btn_add_car);
        progressBar = v.findViewById(R.id.progress_bar_add_car);
    }

    private void convertEditTextsToStrings() {
        model = Objects.requireNonNull(etModel.getText()).toString();
        year = Objects.requireNonNull(etYear.getText()).toString();
        color = Objects.requireNonNull(etColor.getText()).toString();
        plateNo = Objects.requireNonNull(etPlateNo.getText()).toString();
    }

    private void clearInput() {
        Objects.requireNonNull(etModel.getText()).clear();
        Objects.requireNonNull(etColor.getText()).clear();
        Objects.requireNonNull(etPlateNo.getText()).clear();
        Objects.requireNonNull(etYear.getText()).clear();
    }

    private void addCarDataToDatabase(Car car,View v) {
        DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference("Cars");
        carsRef.push().setValue(car).addOnCompleteListener(task -> {
            clearInput();
            Toast.makeText(v.getContext(), "Car is added Successfully",
                    Toast.LENGTH_SHORT).show();
        });
    }
}