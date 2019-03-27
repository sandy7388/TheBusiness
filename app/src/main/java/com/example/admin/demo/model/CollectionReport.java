package com.example.admin.demo.model;

public class CollectionReport {
    private String strName,strDate,strAmount;

    public CollectionReport(String strName, String strDate, String strAmount) {
        this.strName = strName;
        this.strDate = strDate;
        this.strAmount = strAmount;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }
}
