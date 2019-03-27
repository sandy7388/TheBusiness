package com.example.admin.demo.model;

public class VehicleReportPojo {

    public VehicleReportPojo() {
    }

    public VehicleReportPojo(String strItemName, String strItemQty, String strItemUnit) {
        this.strItemName = strItemName;
        this.strItemQty = strItemQty;
        this.strItemUnit = strItemUnit;
    }

    private String strItemName,strItemQty,strItemUnit;

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrItemQty() {
        return strItemQty;
    }

    public void setStrItemQty(String strItemQty) {
        this.strItemQty = strItemQty;
    }

    public String getStrItemUnit() {
        return strItemUnit;
    }

    public void setStrItemUnit(String strItemUnit) {
        this.strItemUnit = strItemUnit;
    }
}
