package com.example.admin.demo.model;

public class TripListPojo {

    public TripListPojo(String routeName, String routeId, String tripName, String tripId) {
        RouteName = routeName;
        RouteId = routeId;
        TripName = tripName;
        TripId = tripId;
    }

    private String RouteName;

    private String RouteId;

    private String TripName;

    private String TripId;

    public String getRouteName ()
    {
        return RouteName;
    }

    public void setRouteName (String RouteName)
    {
        this.RouteName = RouteName;
    }

    public String getRouteId ()
    {
        return RouteId;
    }

    public void setRouteId (String RouteId)
    {
        this.RouteId = RouteId;
    }

    public String getTripName ()
    {
        return TripName;
    }

    public void setTripName (String TripName)
    {
        this.TripName = TripName;
    }

    public String getTripId ()
    {
        return TripId;
    }

    public void setTripId (String TripId)
    {
        this.TripId = TripId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [RouteName = "+RouteName+", RouteId = "+RouteId+", TripName = "+TripName+", TripId = "+TripId+"]";
    }
}
