package com.example.miniuber.app.features.employeeFeatures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniuber.R;
import com.example.miniuber.entities.Driver;

import java.util.ArrayList;

public class DriversWithoutCarsAdapter extends RecyclerView.Adapter<DriversWithoutCarsAdapter.DriverWithoutCarViewHolder>  {

    ArrayList<Driver> driversWithoutCar;

    public DriversWithoutCarsAdapter(ArrayList<Driver> driversWithoutCar) {
        this.driversWithoutCar = driversWithoutCar;
    }

    @NonNull
    @Override
    public DriverWithoutCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_without_car_custom_item,parent,false);
        return new DriverWithoutCarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverWithoutCarViewHolder holder, int position) {

        holder.driverPhoneNo.setText(driversWithoutCar.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return driversWithoutCar.size();
    }

   static class DriverWithoutCarViewHolder extends RecyclerView.ViewHolder  {

        TextView driverPhoneNo;

        public DriverWithoutCarViewHolder(@NonNull View itemView) {
            super(itemView);
            driverPhoneNo = itemView.findViewById(R.id.driver_without_car_phoneNo);



        }

    }


}
