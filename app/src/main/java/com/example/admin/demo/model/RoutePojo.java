package com.example.admin.demo.model;

public class RoutePojo
{
    private String RouteName;

    private String RouteId;

    public RoutePojo() {
    }

    public RoutePojo(String routeName, String routeId) {
        RouteName = routeName;
        RouteId = routeId;
    }

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

    @Override
    public String toString()
    {
        return "ClassPojo [RouteName = "+RouteName+", RouteId = "+RouteId+"]";
    }
}
