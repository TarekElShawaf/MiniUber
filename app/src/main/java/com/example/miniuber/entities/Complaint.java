package com.example.miniuber.entities;

import java.io.Serializable;

public class Complaint implements Serializable {
    private int complaintID;
    private String problem;
    private int tripId;
    private String date;

    public Complaint() {
    }

    public Complaint(int id, String problem, int tripId, String date) {
        this.complaintID = id;
        this.problem = problem;
        this.tripId = tripId;
        this.date = date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getDate() {
        return date;
    }
}
