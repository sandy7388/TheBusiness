package com.example.admin.demo.model;

public class ExpenseItems {



    private String ExpNumber;

    private String PaymentReference;

    private String ExpenseCategoryId;

    private String CompanyId;

    private String ExpenseCategory;

    private String ExpenseId;

    private String ExDate;

    private String UserLiginId;

    private String TotalAmount;

    private String PaymentMode;

    public String getExpNumber ()
    {
        return ExpNumber;
    }

    public void setExpNumber (String ExpNumber)
    {
        this.ExpNumber = ExpNumber;
    }

    public String getPaymentReference ()
    {
        return PaymentReference;
    }

    public void setPaymentReference (String PaymentReference)
    {
        this.PaymentReference = PaymentReference;
    }

    public String getExpenseCategoryId ()
    {
        return ExpenseCategoryId;
    }

    public void setExpenseCategoryId (String ExpenseCategoryId)
    {
        this.ExpenseCategoryId = ExpenseCategoryId;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getExpenseCategory ()
    {
        return ExpenseCategory;
    }

    public void setExpenseCategory (String ExpenseCategory)
    {
        this.ExpenseCategory = ExpenseCategory;
    }

    public String getExpenseId ()
    {
        return ExpenseId;
    }

    public void setExpenseId (String ExpenseId)
    {
        this.ExpenseId = ExpenseId;
    }

    public String getExDate ()
    {
        return ExDate;
    }

    public void setExDate (String ExDate)
    {
        this.ExDate = ExDate;
    }

    public String getUserLiginId ()
    {
        return UserLiginId;
    }

    public void setUserLiginId (String UserLiginId)
    {
        this.UserLiginId = UserLiginId;
    }

    public String getTotalAmount ()
    {
        return TotalAmount;
    }

    public void setTotalAmount (String TotalAmount)
    {
        this.TotalAmount = TotalAmount;
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
        return "ClassPojo [ExpNumber = "+ExpNumber+", PaymentReference = "+PaymentReference+", ExpenseCategoryId = "+ExpenseCategoryId+", CompanyId = "+CompanyId+", ExpenseCategory = "+ExpenseCategory+", ExpenseId = "+ExpenseId+", ExDate = "+ExDate+", UserLiginId = "+UserLiginId+", TotalAmount = "+TotalAmount+", PaymentMode = "+PaymentMode+"]";
    }
}
