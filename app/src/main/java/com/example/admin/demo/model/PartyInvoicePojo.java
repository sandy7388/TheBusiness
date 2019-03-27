package com.example.admin.demo.model;

import java.util.ArrayList;

public class PartyInvoicePojo
{
    private String PartyId;

    private String CompanyId;

    private String InvoiceDate;

    private String Remarks;

    private String RouteId;

    private String TaxAmount;

    private String DiscountAmount;

    private String InvoicePaidAmount;

    private String TripId;

    private String InvoiceSubTotal;

    private String IsCashSale;

    private String InvoicePendingAmount;

    private String OtherCharges;

    private String InvoiceAmount;

    private String InvoiceNo;

    private String SalesInvoiceId;

    private String UserLoginId;

    private String InvoiceStatus;

    private String NickName;

    private String PartyName;

    private String PartyCurrentBalance;

    private ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList;


    public PartyInvoicePojo() {
    }

    public PartyInvoicePojo(String invoiceDate, String routeId, String invoicePaidAmount,
                            String tripId, String invoicePendingAmount,
                            String invoiceAmount, String invoiceNo, String salesInvoiceId,
                            String invoiceStatus, String partyName, String partyCurrentBalance,
                            ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList) {
        InvoiceDate = invoiceDate;
        RouteId = routeId;
        InvoicePaidAmount = invoicePaidAmount;
        TripId = tripId;
        InvoicePendingAmount = invoicePendingAmount;
        InvoiceAmount = invoiceAmount;
        InvoiceNo = invoiceNo;
        SalesInvoiceId = salesInvoiceId;
        InvoiceStatus = invoiceStatus;
        PartyName = partyName;
        PartyCurrentBalance = partyCurrentBalance;
        this.invoiceItemDetailsArrayList = invoiceItemDetailsArrayList;
    }

    public ArrayList<InvoiceItemDetails> getInvoiceItemDetailsArrayList() {
        return invoiceItemDetailsArrayList;
    }

    public void setInvoiceItemDetailsArrayList(ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList) {
        this.invoiceItemDetailsArrayList = invoiceItemDetailsArrayList;
    }

    public String getPartyCurrentBalance() {
        return PartyCurrentBalance;
    }

    public void setPartyCurrentBalance(String partyCurrentBalance) {
        PartyCurrentBalance = partyCurrentBalance;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
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

    public String getInvoiceDate ()
    {
        return InvoiceDate;
    }

    public void setInvoiceDate (String InvoiceDate)
    {
        this.InvoiceDate = InvoiceDate;
    }

    public String getRemarks ()
    {
        return Remarks;
    }

    public void setRemarks (String Remarks)
    {
        this.Remarks = Remarks;
    }

    public String getRouteId ()
    {
        return RouteId;
    }

    public void setRouteId (String RouteId)
    {
        this.RouteId = RouteId;
    }

    public String getTaxAmount ()
    {
        return TaxAmount;
    }

    public void setTaxAmount (String TaxAmount)
    {
        this.TaxAmount = TaxAmount;
    }

    public String getDiscountAmount ()
    {
        return DiscountAmount;
    }

    public void setDiscountAmount (String DiscountAmount)
    {
        this.DiscountAmount = DiscountAmount;
    }

    public String getInvoicePaidAmount ()
    {
        return InvoicePaidAmount;
    }

    public void setInvoicePaidAmount (String InvoicePaidAmount)
    {
        this.InvoicePaidAmount = InvoicePaidAmount;
    }

    public String getTripId ()
    {
        return TripId;
    }

    public void setTripId (String TripId)
    {
        this.TripId = TripId;
    }

    public String getInvoiceSubTotal ()
    {
        return InvoiceSubTotal;
    }

    public void setInvoiceSubTotal (String InvoiceSubTotal)
    {
        this.InvoiceSubTotal = InvoiceSubTotal;
    }

    public String getIsCashSale ()
    {
        return IsCashSale;
    }

    public void setIsCashSale (String IsCashSale)
    {
        this.IsCashSale = IsCashSale;
    }

    public String getInvoicePendingAmount ()
    {
        return InvoicePendingAmount;
    }

    public void setInvoicePendingAmount (String InvoicePendingAmount)
    {
        this.InvoicePendingAmount = InvoicePendingAmount;
    }

    public String getOtherCharges ()
    {
        return OtherCharges;
    }

    public void setOtherCharges (String OtherCharges)
    {
        this.OtherCharges = OtherCharges;
    }

    public String getInvoiceAmount ()
    {
        return InvoiceAmount;
    }

    public void setInvoiceAmount (String InvoiceAmount)
    {
        this.InvoiceAmount = InvoiceAmount;
    }

    public String getInvoiceNo ()
    {
        return InvoiceNo;
    }

    public void setInvoiceNo (String InvoiceNo)
    {
        this.InvoiceNo = InvoiceNo;
    }

    public String getSalesInvoiceId ()
    {
        return SalesInvoiceId;
    }

    public void setSalesInvoiceId (String SalesInvoiceId)
    {
        this.SalesInvoiceId = SalesInvoiceId;
    }

    public String getUserLoginId ()
    {
        return UserLoginId;
    }

    public void setUserLoginId (String UserLoginId)
    {
        this.UserLoginId = UserLoginId;
    }

    public String getInvoiceStatus ()
    {
        return InvoiceStatus;
    }

    public void setInvoiceStatus (String InvoiceStatus)
    {
        this.InvoiceStatus = InvoiceStatus;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [PartyId = "+PartyId+", CompanyId = "+CompanyId+", InvoiceDate = "+InvoiceDate+", Remarks = "+Remarks+", RouteId = "+RouteId+", TaxAmount = "+TaxAmount+", DiscountAmount = "+DiscountAmount+", InvoicePaidAmount = "+InvoicePaidAmount+", TripId = "+TripId+", InvoiceSubTotal = "+InvoiceSubTotal+", IsCashSale = "+IsCashSale+", InvoicePendingAmount = "+InvoicePendingAmount+", OtherCharges = "+OtherCharges+", InvoiceAmount = "+InvoiceAmount+", InvoiceNo = "+InvoiceNo+", SalesInvoiceId = "+SalesInvoiceId+", UserLoginId = "+UserLoginId+", InvoiceStatus = "+InvoiceStatus+"]";
    }
}
