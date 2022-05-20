package com.example.miniuber.app.features.employeeFeatures.employeeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniuber.R;
import com.example.miniuber.app.features.employeeFeatures.AvailableCarsAdapter;
import com.example.miniuber.app.features.employeeFeatures.DriversWithoutCarsAdapter;
import com.example.miniuber.entities.Car;
import com.example.miniuber.entities.Driver;
import com.example.miniuber.entities.FormatChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;


public class AssignCarToDriverFragment extends Fragment {


    private EditText etCarPlateNo, etDriverPhoneNo;
    private CountryCodePicker picker;
    private AppCompatButton assignCar;
    private ProgressBar progressBar;
    private String carRowID;
    private String oldCarRowID;
    private String driverRowID;
    private final DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference("Cars");
    private final DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference("Users")
            .child("Drivers");

    RecyclerView rvCars;
    RecyclerView rvDrivers;

    private ArrayList<Car>  availableCars = new ArrayList<>();
    private ArrayList<Driver> driversWithoutCars = new ArrayList<>();


    private AvailableCarsAdapter availableCarsAdapter;

    private DriversWithoutCarsAdapter driversWithoutCarsAdapter;


    public AssignCarToDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_assign_car_to_driver, container, false);
        initialize(v);

        getAllFreeCars();
        getAllDriversWithoutCar();


        availableCarsAdapter = new AvailableCarsAdapter(availableCars);
        driversWithoutCarsAdapter = new DriversWithoutCarsAdapter(driversWithoutCars);

        rvCars.setAdapter(availableCarsAdapter);
        rvDrivers.setAdapter(driversWithoutCarsAdapter);


        rvCars.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDrivers.setLayoutManager(new LinearLayoutManager(getActivity()));



        assignCar.setOnClickListener(new View.OnClickListener() {
            String carPlateNo, driverPhoneNo;

            @Override
            public void onClick(View view) {
                carPlateNo = etCarPlateNo.getText().toString();
                driverPhoneNo = etDriverPhoneNo.getText().toString();

                if (carPlateNo.isEmpty() || driverPhoneNo.isEmpty()) {
                    Toast.makeText(v.getContext(), "Fill All Fields", Toast.LENGTH_SHORT).show();
                } else if (!FormatChecker.isValidPhoneNo(driverPhoneNo)) {
                    Toast.makeText(v.getContext(), "invalid number", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    String fullNumber = "+" + picker.getSelectedCountryCode() + driverPhoneNo;
                    //Does the car exist ?
                    carsRef.orderByChild("plateNumber").equalTo(carPlateNo)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.getValue() == null) {
                                        //car does not exist
                                        Toast.makeText(v.getContext(), "car not found", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);


                                    } else {

                                        //car exists
                                        // check if car is already assigned to another driver\
                                        Car car = getCarFromFireBase(snapshot, carPlateNo);

                                        if (car.getDriverPhoneNo().equals("")) {


                                            driversRef.orderByChild("phoneNumber").equalTo(fullNumber)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.getValue() == null) {
                                                                Toast.makeText(v.getContext(), "Driver does not exist", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                            } else {
                                                                //driver exists

                                                                Driver driver = getDriverFromFireBase(snapshot, fullNumber);

                                                                if (driver.getCarPlateNumber().equals("")) {
                                                                    //driver not assigned to another car!

                                                                    driversRef.child(driverRowID).child("carPlateNumber").setValue(carPlateNo);
                                                                    carsRef.child(carRowID).child("driverPhoneNo").setValue(fullNumber);

                                                                    etCarPlateNo.getText().clear();
                                                                    etDriverPhoneNo.getText().clear();

                                                                    Toast.makeText(v.getContext(), "driver assigned to car successfully"
                                                                            , Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.INVISIBLE);


                                                                } else {
                                                                    //driver is already assigned to another car

                                                                    //Ahmed drives an Accent
                                                                    // No we Want Ahmed to Drive an Astra

                                                                    //Go to Ahmed and set the carPlateNo

                                                                    addCarToAvailableCars(driver);
                                                                    driversRef.child(driverRowID).child("carPlateNumber").setValue(carPlateNo);

                                                                    //Go to Astra and Set the driver's PhoneNo
                                                                    carsRef.child(carRowID).child("driverPhoneNo").setValue(fullNumber);


                                                                    etCarPlateNo.getText().clear();
                                                                    etDriverPhoneNo.getText().clear();

                                                                    //Go to Accent and set the driver's Phone No to ""
                                                                    //how?



                                                                    Toast.makeText(v.getContext(), "driver assigned to car successfully",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.INVISIBLE);

                                                                }



                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });


                                        } else {

                                            //car is already assigned to another driver
                                            Toast.makeText(v.getContext(), "car is assigned to another driver",
                                                    Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }


                                    }


                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                            });


                }

            }
        });


        return v;
    }

    private void initialize(View v) {
        etCarPlateNo = v.findViewById(R.id.et_assign_car_plateNo);
        etDriverPhoneNo = v.findViewById(R.id.et_assign_driver_phoneNo);
        assignCar = v.findViewById(R.id.btn_assign_car_to_driver);
        progressBar = v.findViewById(R.id.progress_bar_assign_car);
        picker = v.findViewById(R.id.code_picker_assign_car_to_driver);
        rvCars = v.findViewById(R.id.rv_available_cars);
        rvDrivers = v.findViewById(R.id.rv_drivers_without_cars);
    }


    private void getAllFreeCars() {

        carsRef.addValueEventListener(new ValueEventListener() {
            Car car;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableCars.clear();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    car = postSnapShot.getValue(Car.class);
                    Log.d("Zizo", car.getPlateNumber());

                    if (car.getDriverPhoneNo().equalsIgnoreCase("")) {
                        Log.d("Zizo", car.getModel());
                        availableCars.add(car);
                    }
                }
                availableCarsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getAllDriversWithoutCar() {

        driversRef.addValueEventListener(new ValueEventListener() {
            Driver driver;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                driversWithoutCars.clear();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    driver = postSnapShot.getValue(Driver.class);

                    Log.d("Zizo", driver.getDrivingLicense());

                    if (driver.getCarPlateNumber().equals("")) {
                        Log.d("Zizo", driver.getName());
                        driversWithoutCars.add(driver);
                    }
                }
                driversWithoutCarsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




    private Car getCarFromFireBase(DataSnapshot snapshot, String carPlateNo) {
        Car car = null;
        for (DataSnapshot postSnapShot : snapshot.getChildren()) {

            car = postSnapShot.getValue(Car.class);

            assert car != null;
            if (car.getPlateNumber().equalsIgnoreCase(carPlateNo)) {
                carRowID = postSnapShot.getKey();
                break;
            }
        }

        return car;
    }

    private Driver getDriverFromFireBase(DataSnapshot snapshot, String fullNumber) {
        Driver driver = null;

        for (DataSnapshot postSnapShot : snapshot.getChildren()) {

            driver = postSnapShot.getValue(Driver.class);

            assert driver != null;
            if (driver.getPhoneNumber().equals(fullNumber)) {
                driverRowID = postSnapShot.getKey();
                break;
            }
        }


        return driver;
    }

    private void addCarToAvailableCars(Driver driver){


        carsRef.addValueEventListener(new ValueEventListener() {
            Car car = null;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren() ){

                    Car car = postSnapshot.getValue(Car.class);
                    if(car.getPlateNumber().equalsIgnoreCase(driver.getCarPlateNumber())){
                        car.setDriverPhoneNo("");
                        oldCarRowID = postSnapshot.getKey();
                        Log.d("Zoz", "key is " + oldCarRowID);
                        break;
                    }
                }

                carsRef.child(oldCarRowID).child("driverPhoneNo").setValue("");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}