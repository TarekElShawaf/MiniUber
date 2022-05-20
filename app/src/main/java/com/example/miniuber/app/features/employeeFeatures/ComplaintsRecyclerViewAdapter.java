package com.example.miniuber.app.features.employeeFeatures;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniuber.R;
import com.example.miniuber.entities.Complaint;

import java.util.ArrayList;

public class ComplaintsRecyclerViewAdapter extends RecyclerView.Adapter<ComplaintsRecyclerViewAdapter.ComplaintsViewHolder> {

    ArrayList<Complaint> complaints;
    ComplaintsRecyclerViewListener listener;
    public ComplaintsRecyclerViewAdapter(ArrayList<Complaint> complaints,ComplaintsRecyclerViewListener listener){
        this.complaints = complaints;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ComplaintsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_custom_item,parent,false);
        return new ComplaintsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintsViewHolder holder, int position) {
        holder.complaintDate.setText(complaints.get(position).getDate());
        holder.complaintID.setText(String.valueOf(complaints.get(position).getComplaintID()));
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    class ComplaintsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView complaintDate,complaintID;
        public ComplaintsViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintDate = itemView.findViewById(R.id.tv_complaint_date);
            complaintID = itemView.findViewById(R.id.tv_complaint_id);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {

            listener.onClick(view,getAdapterPosition());

        }
    }
}
