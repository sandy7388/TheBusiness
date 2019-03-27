package com.example.admin.demo.model;

public class UnitPojo {
    private String unitName,unitId,shortName;

    public UnitPojo(String unitName, String unitId, String shortName) {
        this.unitName = unitName;
        this.unitId = unitId;
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
