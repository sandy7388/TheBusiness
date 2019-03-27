package com.example.admin.demo.model;

import java.util.ArrayList;
import java.util.Map;

public class ListTaxGroup
{

    private String strTitle;

    private String GroupName;

    private String TaxGroupId;

    private String TaxGroupRate;

    private ArrayList<TaxList> TaxList;

    private Map<String,ArrayList<TaxList>> map;

    private String TaxId;

    private String TaxRateName;

    public ListTaxGroup() {
    }

    public ListTaxGroup(String groupName, String taxGroupRate) {
        GroupName = groupName;
        TaxGroupRate = taxGroupRate;
    }

    public String getTaxRateName() {
        return TaxRateName;
    }

    public void setTaxRateName(String taxRateName) {
        TaxRateName = taxRateName;
    }

    public String getTaxGroupRate() {
        return TaxGroupRate;
    }

    public void setTaxGroupRate(String taxGroupRate) {
        TaxGroupRate = taxGroupRate;
    }


    public Map<String, ArrayList<TaxList>> getMap() {
        return map;
    }

    public void setMap(Map<String, ArrayList<TaxList>> map) {
        this.map = map;
    }


    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getGroupName ()
    {
        return GroupName;
    }

    public void setGroupName (String GroupName)
    {
        this.GroupName = GroupName;
    }

    public String getTaxGroupId ()
    {
        return TaxGroupId;
    }

    public void setTaxGroupId (String TaxGroupId)
    {
        this.TaxGroupId = TaxGroupId;
    }

    public ArrayList<TaxList> getTaxList() {
        return TaxList;
    }

    public void setTaxList(ArrayList<TaxList> taxList) {
        TaxList = taxList;
    }

    public String getTaxId ()
    {
        return TaxId;
    }

    public void setTaxId (String TaxId)
    {
        this.TaxId = TaxId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [GroupName = "+GroupName+", TaxGroupId = "+TaxGroupId+", TaxList = "+TaxList+", TaxId = "+TaxId+"]";
    }

}

