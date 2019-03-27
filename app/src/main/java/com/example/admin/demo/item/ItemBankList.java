package com.example.admin.demo.item;

public class ItemBankList {
    private String BankName;

    private String CompanyId;

    private String BankAccountId;

    private String CurrentBalance;

    private String EntryDate;

    private String AccountHolderName;

    private String UserLiginId;

    private String AccountNumber;

    public ItemBankList(String bankName, String bankAccountId, String currentBalance) {
        BankName = bankName;
        BankAccountId = bankAccountId;
        CurrentBalance = currentBalance;
    }

    public String getBankName ()
    {
        return BankName;
    }

    public void setBankName (String BankName)
    {
        this.BankName = BankName;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getBankAccountId ()
    {
        return BankAccountId;
    }

    public void setBankAccountId (String BankAccountId)
    {
        this.BankAccountId = BankAccountId;
    }

    public String getCurrentBalance ()
    {
        return CurrentBalance;
    }

    public void setCurrentBalance (String CurrentBalance)
    {
        this.CurrentBalance = CurrentBalance;
    }

    public String getEntryDate ()
    {
        return EntryDate;
    }

    public void setEntryDate (String EntryDate)
    {
        this.EntryDate = EntryDate;
    }

    public String getAccountHolderName ()
    {
        return AccountHolderName;
    }

    public void setAccountHolderName (String AccountHolderName)
    {
        this.AccountHolderName = AccountHolderName;
    }

    public String getUserLiginId ()
    {
        return UserLiginId;
    }

    public void setUserLiginId (String UserLiginId)
    {
        this.UserLiginId = UserLiginId;
    }

    public String getAccountNumber ()
    {
        return AccountNumber;
    }

    public void setAccountNumber (String AccountNumber)
    {
        this.AccountNumber = AccountNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [BankName = "+BankName+", CompanyId = "+CompanyId+", BankAccountId = "+BankAccountId+", CurrentBalance = "+CurrentBalance+", EntryDate = "+EntryDate+", AccountHolderName = "+AccountHolderName+", UserLiginId = "+UserLiginId+", AccountNumber = "+AccountNumber+"]";
    }
}
