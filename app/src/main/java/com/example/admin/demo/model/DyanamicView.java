package com.example.admin.demo.model;

import android.widget.EditText;
import android.widget.TextView;

public class DyanamicView {

    private EditText edt_itemName,edt_itemPrice,edt_itemQty;
    private TextView tv_amount;

    public EditText getEdt_itemName() {
        return edt_itemName;
    }

    public void setEdt_itemName(EditText edt_itemName) {
        this.edt_itemName = edt_itemName;
    }

    public EditText getEdt_itemPrice() {
        return edt_itemPrice;
    }

    public void setEdt_itemPrice(EditText edt_itemPrice) {
        this.edt_itemPrice = edt_itemPrice;
    }

    public EditText getEdt_itemQty() {
        return edt_itemQty;
    }

    public void setEdt_itemQty(EditText edt_itemQty) {
        this.edt_itemQty = edt_itemQty;
    }

    public TextView getTv_amount() {
        return tv_amount;
    }

    public void setTv_amount(TextView tv_amount) {
        this.tv_amount = tv_amount;
    }
}
