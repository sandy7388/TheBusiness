package com.example.admin.demo.item;

public class TaxRateItem {

    String strId,strName,strRate,strType;

    public TaxRateItem(String strId, String strName, String strRate, String strType) {
        this.strId = strId;
        this.strName = strName;
        this.strRate = strRate;
        this.strType = strType;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrRate() {
        return strRate;
    }

    public void setStrRate(String strRate) {
        this.strRate = strRate;
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }
}
