package com.example.admin.demo.item;

public class ItemCompanyList {
    String name, compid;


    public ItemCompanyList(String name, String compid) {
        this.name = name;
        this.compid = compid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }
}
