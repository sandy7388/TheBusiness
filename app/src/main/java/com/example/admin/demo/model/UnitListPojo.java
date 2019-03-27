package com.example.admin.demo.model;

public class UnitListPojo {
    private String strUnitId,strUnitName,strConversionRate;

    public UnitListPojo(String strUnitId, String strUnitName,
                        String strConversionRate) {
        this.strUnitId = strUnitId;
        this.strUnitName = strUnitName;
        this.strConversionRate = strConversionRate;
    }

    public String getStrUnitId() {
        return strUnitId;
    }

    public void setStrUnitId(String strUnitId) {
        this.strUnitId = strUnitId;
    }

    public String getStrUnitName() {
        return strUnitName;
    }

    public void setStrUnitName(String strUnitName) {
        this.strUnitName = strUnitName;
    }

    public String getStrConversionRate() {
        return strConversionRate;
    }

    public void setStrConversionRate(String strConversionRate) {
        this.strConversionRate = strConversionRate;
    }
}
