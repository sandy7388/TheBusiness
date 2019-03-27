package com.example.admin.demo.model;

public class PaymentListPojo
{
    private String ChequeDepositDate;

    private String CompanyId;

    private String PaymentDate;

    private String PaymentId;

    private String UserLiginId;

    private String PartyName;

    private String PartyCurrentBalance;

    private String IsReceivable;

    private String NickName;

    private String PaidAmount;

    private String PartyId;

    private String ReferenceNo;

    private String PaymentStatus;

    private String PaymentMode;

    public PaymentListPojo(String chequeDepositDate, String paymentDate,
                           String paymentId, String partyName,
                           String partyCurrentBalance, String paidAmount,
                           String partyId, String referenceNo,
                           String paymentStatus, String paymentMode) {
        ChequeDepositDate = chequeDepositDate;
        PaymentDate = paymentDate;
        PaymentId = paymentId;
        PartyName = partyName;
        PartyCurrentBalance = partyCurrentBalance;
        PaidAmount = paidAmount;
        PartyId = partyId;
        ReferenceNo = referenceNo;
        PaymentStatus = paymentStatus;
        PaymentMode = paymentMode;
    }

    public String getChequeDepositDate ()
    {
        return ChequeDepositDate;
    }

    public void setChequeDepositDate (String ChequeDepositDate)
    {
        this.ChequeDepositDate = ChequeDepositDate;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getPaymentDate ()
    {
        return PaymentDate;
    }

    public void setPaymentDate (String PaymentDate)
    {
        this.PaymentDate = PaymentDate;
    }

    public String getPaymentId ()
    {
        return PaymentId;
    }

    public void setPaymentId (String PaymentId)
    {
        this.PaymentId = PaymentId;
    }

    public String getUserLiginId ()
    {
        return UserLiginId;
    }

    public void setUserLiginId (String UserLiginId)
    {
        this.UserLiginId = UserLiginId;
    }

    public String getPartyName ()
    {
        return PartyName;
    }

    public void setPartyName (String PartyName)
    {
        this.PartyName = PartyName;
    }

    public String getPartyCurrentBalance ()
    {
        return PartyCurrentBalance;
    }

    public void setPartyCurrentBalance (String PartyCurrentBalance)
    {
        this.PartyCurrentBalance = PartyCurrentBalance;
    }

    public String getIsReceivable ()
    {
        return IsReceivable;
    }

    public void setIsReceivable (String IsReceivable)
    {
        this.IsReceivable = IsReceivable;
    }

    public String getNickName ()
    {
        return NickName;
    }

    public void setNickName (String NickName)
    {
        this.NickName = NickName;
    }

    public String getPaidAmount ()
    {
        return PaidAmount;
    }

    public void setPaidAmount (String PaidAmount)
    {
        this.PaidAmount = PaidAmount;
    }

    public String getPartyId ()
    {
        return PartyId;
    }

    public void setPartyId (String PartyId)
    {
        this.PartyId = PartyId;
    }

    public String getReferenceNo ()
    {
        return ReferenceNo;
    }

    public void setReferenceNo (String ReferenceNo)
    {
        this.ReferenceNo = ReferenceNo;
    }

    public String getPaymentStatus ()
    {
        return PaymentStatus;
    }

    public void setPaymentStatus (String PaymentStatus)
    {
        this.PaymentStatus = PaymentStatus;
    }

    public String getPaymentMode ()
    {
        return PaymentMode;
    }

    public void setPaymentMode (String PaymentMode)
    {
        this.PaymentMode = PaymentMode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ChequeDepositDate = "+ChequeDepositDate+", CompanyId = "+CompanyId+", PaymentDate = "+PaymentDate+", PaymentId = "+PaymentId+", UserLiginId = "+UserLiginId+", PartyName = "+PartyName+", PartyCurrentBalance = "+PartyCurrentBalance+", IsReceivable = "+IsReceivable+", NickName = "+NickName+", PaidAmount = "+PaidAmount+", PartyId = "+PartyId+", ReferenceNo = "+ReferenceNo+", PaymentStatus = "+PaymentStatus+", PaymentMode = "+PaymentMode+"]";
    }
}

