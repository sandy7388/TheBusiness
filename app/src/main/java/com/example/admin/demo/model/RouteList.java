package com.example.admin.demo.model;

public class RouteList {
    String strRouteName,strRouteId;

    public RouteList(String strRouteName, String strRouteId) {
        this.strRouteName = strRouteName;
        this.strRouteId = strRouteId;
    }

    public String getStrRouteName() {
        return strRouteName;
    }

    public void setStrRouteName(String strRouteName) {
        this.strRouteName = strRouteName;
    }

    public String getStrRouteId() {
        return strRouteId;
    }

    public void setStrRouteId(String strRouteId) {
        this.strRouteId = strRouteId;
    }
}
