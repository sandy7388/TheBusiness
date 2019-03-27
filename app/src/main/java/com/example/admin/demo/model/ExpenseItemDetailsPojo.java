package com.example.admin.demo.model;

public class ExpenseItemDetailsPojo {

    private String strId,strItemName,strItemQty,strPriceUnit,strAmount;

    public ExpenseItemDetailsPojo(String strItemName, String strItemQty,
                                  String strPriceUnit, String strAmount) {
        this.strItemName = strItemName;
        this.strItemQty = strItemQty;
        this.strPriceUnit = strPriceUnit;
        this.strAmount = strAmount;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

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

    public String getStrPriceUnit() {
        return strPriceUnit;
    }

    public void setStrPriceUnit(String strPriceUnit) {
        this.strPriceUnit = strPriceUnit;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }
}
