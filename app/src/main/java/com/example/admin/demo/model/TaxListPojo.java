package com.example.admin.demo.model;

public class TaxListPojo {

    private String strTaxName,strTaxId,strTaxRate;

    public TaxListPojo(String strTaxName, String strTaxId) {
        this.strTaxName = strTaxName;
        this.strTaxId = strTaxId;
    }

    public TaxListPojo(String strTaxName, String strTaxId, String strTaxRate) {
        this.strTaxName = strTaxName;
        this.strTaxId = strTaxId;
        this.strTaxRate = strTaxRate;
    }

    public String getStrTaxRate() {
        return strTaxRate;
    }

    public void setStrTaxRate(String strTaxRate) {
        this.strTaxRate = strTaxRate;
    }

    public String getStrTaxName() {
        return strTaxName;
    }

    public void setStrTaxName(String strTaxName) {
        this.strTaxName = strTaxName;
    }

    public String getStrTaxId() {
        return strTaxId;
    }

    public void setStrTaxId(String strTaxId) {
        this.strTaxId = strTaxId;
    }
}
