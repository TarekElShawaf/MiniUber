package com.example.miniuber.app.features.employeeFeatures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniuber.R;

import com.example.miniuber.entities.Car;


import java.util.ArrayList;

public class AvailableCarsAdapter  extends RecyclerView.Adapter<AvailableCarsAdapter.AvailableCarsViewHolder> {

    ArrayList<Car> cars;

    public AvailableCarsAdapter(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public AvailableCarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.available_car_custom_item,parent,false);
        return new AvailableCarsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableCarsViewHolder holder, int position) {
        holder.carPlateNo.setText(cars.get(position).getPlateNumber());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

   static class AvailableCarsViewHolder extends RecyclerView.ViewHolder  {

        TextView carPlateNo;

        public AvailableCarsViewHolder(@NonNull View itemView) {
            super(itemView);
            carPlateNo = itemView.findViewById(R.id.tv_car_plate_no);



        }

    }

}
