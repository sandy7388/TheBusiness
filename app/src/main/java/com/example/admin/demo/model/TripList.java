package com.example.admin.demo.model;

public class TripList {
    private String tripName,tripId;

    public TripList(String tripName, String tripId) {
        this.tripName = tripName;
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
