package com.example.admin.demo.model;

public class TripListByRoutePojo {
    private String tripName;

    private String tripId;

    private String routId;

    public TripListByRoutePojo() {
    }

    public TripListByRoutePojo(String tripName, String tripId, String routId) {
        this.tripName = tripName;
        this.tripId = tripId;
        this.routId = routId;
    }

    public String getRoutId() {
        return routId;
    }

    public void setRoutId(String routId) {
        this.routId = routId;
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
