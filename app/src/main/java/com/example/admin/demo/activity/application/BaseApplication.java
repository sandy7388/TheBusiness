package com.example.admin.demo.activity.application;

import android.app.Application;

import com.example.admin.demo.activity.PartyInvoiceActivity;
import com.example.admin.demo.printer.ScanActivity;

public class BaseApplication extends Application {
    public PartyInvoiceActivity partyInvoiceActivity;
    public ScanActivity scanActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        //partyInvoiceActivity = new PartyInvoiceActivity();
        scanActivity = new ScanActivity();
    }
}
