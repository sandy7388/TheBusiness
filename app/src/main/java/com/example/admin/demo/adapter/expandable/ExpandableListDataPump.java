package com.example.admin.demo.adapter.expandable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> cricket = new ArrayList<String>();

        List<String> reports = new ArrayList<String>();
        reports.add("Vehicle Report");
        reports.add("Payment Report");
        reports.add("Next Day Order Report");
        reports.add("Sales Report");
        //reports.add("Sale Pending Report");


        expandableListDetail.put("Add Party", cricket);
        expandableListDetail.put("Items", cricket);
        expandableListDetail.put("Purchase Order", cricket);
        expandableListDetail.put("Next Day Order", cricket);
        expandableListDetail.put("Bulk Order", cricket);
        expandableListDetail.put("Sales Order", cricket);
        expandableListDetail.put("PM Order", cricket);
        expandableListDetail.put("Manage Payment", cricket);
        expandableListDetail.put("Add Tax", cricket);
        expandableListDetail.put("Expenses", cricket);
        expandableListDetail.put("Reports", reports);
        expandableListDetail.put("Bank Accounts", cricket);
        expandableListDetail.put("Manage User", cricket);
        expandableListDetail.put("Logout", cricket);
        expandableListDetail.put("Printer Settings", cricket);
        return expandableListDetail;
    }
}
