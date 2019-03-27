package com.example.admin.demo.model;

public class PartyType
{
    String strPartyType,strPartyId;

    public PartyType(String strPartyType, String strPartyId) {
        this.strPartyType = strPartyType;
        this.strPartyId = strPartyId;
    }

    public String getStrPartyType() {
        return strPartyType;
    }

    public void setStrPartyType(String strPartyType) {
        this.strPartyType = strPartyType;
    }

    public String getStrPartyId() {
        return strPartyId;
    }

    public void setStrPartyId(String strPartyId) {
        this.strPartyId = strPartyId;
    }
}
