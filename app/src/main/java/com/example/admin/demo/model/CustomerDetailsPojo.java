package com.example.admin.demo.model;

public class CustomerDetailsPojo
{
    private String AsOfDate;

    private String UserLiginId;

    private String PartyId;

    private String CompanyId;

    private String NickName;

    private String PartyEmail;

    private String PartyName;

    private String RouteId;

    private String PartyPhone;

    private String TripId;

    private String SerialNumber;

    private String IsReceivable;

    private String PartyState;

    private String PayStatus;

    private String PartyGSTINNumber;

    private String PartyAddress;

    private String PartyCurrentBalance;

    private String PartyTypeId;

    public CustomerDetailsPojo() {
    }

    public CustomerDetailsPojo(String partyId, String partyName, String routeId) {
        PartyId = partyId;
        PartyName = partyName;
        RouteId = routeId;
    }

    public CustomerDetailsPojo(String partyId, String nickName, String partyName,
                               String routeId, String tripId, String partyCurrentBalance) {
        PartyId = partyId;
        NickName = nickName;
        PartyName = partyName;
        RouteId = routeId;
        TripId = tripId;
        PartyCurrentBalance = partyCurrentBalance;
    }

    public String getAsOfDate ()
    {
        return AsOfDate;
    }

    public void setAsOfDate (String AsOfDate)
    {
        this.AsOfDate = AsOfDate;
    }

    public String getUserLiginId ()
    {
        return UserLiginId;
    }

    public void setUserLiginId (String UserLiginId)
    {
        this.UserLiginId = UserLiginId;
    }

    public String getPartyId ()
    {
        return PartyId;
    }

    public void setPartyId (String PartyId)
    {
        this.PartyId = PartyId;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getNickName ()
    {
        return NickName;
    }

    public void setNickName (String NickName)
    {
        this.NickName = NickName;
    }

    public String getPartyEmail ()
    {
        return PartyEmail;
    }

    public void setPartyEmail (String PartyEmail)
    {
        this.PartyEmail = PartyEmail;
    }

    public String getPartyName ()
    {
        return PartyName;
    }

    public void setPartyName (String PartyName)
    {
        this.PartyName = PartyName;
    }

    public String getRouteId ()
    {
        return RouteId;
    }

    public void setRouteId (String RouteId)
    {
        this.RouteId = RouteId;
    }

    public String getPartyPhone ()
    {
        return PartyPhone;
    }

    public void setPartyPhone (String PartyPhone)
    {
        this.PartyPhone = PartyPhone;
    }

    public String getTripId ()
    {
        return TripId;
    }

    public void setTripId (String TripId)
    {
        this.TripId = TripId;
    }

    public String getSerialNumber ()
    {
        return SerialNumber;
    }

    public void setSerialNumber (String SerialNumber)
    {
        this.SerialNumber = SerialNumber;
    }

    public String getIsReceivable ()
    {
        return IsReceivable;
    }

    public void setIsReceivable (String IsReceivable)
    {
        this.IsReceivable = IsReceivable;
    }

    public String getPartyState ()
    {
        return PartyState;
    }

    public void setPartyState (String PartyState)
    {
        this.PartyState = PartyState;
    }

    public String getPayStatus ()
    {
        return PayStatus;
    }

    public void setPayStatus (String PayStatus)
    {
        this.PayStatus = PayStatus;
    }

    public String getPartyGSTINNumber ()
    {
        return PartyGSTINNumber;
    }

    public void setPartyGSTINNumber (String PartyGSTINNumber)
    {
        this.PartyGSTINNumber = PartyGSTINNumber;
    }

    public String getPartyAddress ()
    {
        return PartyAddress;
    }

    public void setPartyAddress (String PartyAddress)
    {
        this.PartyAddress = PartyAddress;
    }

    public String getPartyCurrentBalance ()
    {
        return PartyCurrentBalance;
    }

    public void setPartyCurrentBalance (String PartyCurrentBalance)
    {
        this.PartyCurrentBalance = PartyCurrentBalance;
    }

    public String getPartyTypeId ()
    {
        return PartyTypeId;
    }

    public void setPartyTypeId (String PartyTypeId)
    {
        this.PartyTypeId = PartyTypeId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AsOfDate = "+AsOfDate+", UserLiginId = "+UserLiginId+", PartyId = "+PartyId+", CompanyId = "+CompanyId+", NickName = "+NickName+", PartyEmail = "+PartyEmail+", PartyName = "+PartyName+", RouteId = "+RouteId+", PartyPhone = "+PartyPhone+", TripId = "+TripId+", SerialNumber = "+SerialNumber+", IsReceivable = "+IsReceivable+", PartyState = "+PartyState+", PayStatus = "+PayStatus+", PartyGSTINNumber = "+PartyGSTINNumber+", PartyAddress = "+PartyAddress+", PartyCurrentBalance = "+PartyCurrentBalance+", PartyTypeId = "+PartyTypeId+"]";
    }
}
