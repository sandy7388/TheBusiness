package com.example.admin.demo.model;

public class ExpenseDetailsListPojo
{
    private String ExpenseItemId;

    private String ExpNumber;

    private String UnitPrice;

    private String ExpenseId;

    private String ItemName;

    private String Quantity;

    private String TotalitemAmount;

    public String getExpenseItemId ()
    {
        return ExpenseItemId;
    }

    public void setExpenseItemId (String ExpenseItemId)
    {
        this.ExpenseItemId = ExpenseItemId;
    }

    public String getExpNumber ()
    {
        return ExpNumber;
    }

    public void setExpNumber (String ExpNumber)
    {
        this.ExpNumber = ExpNumber;
    }

    public String getUnitPrice ()
    {
        return UnitPrice;
    }

    public void setUnitPrice (String UnitPrice)
    {
        this.UnitPrice = UnitPrice;
    }

    public String getExpenseId ()
    {
        return ExpenseId;
    }

    public void setExpenseId (String ExpenseId)
    {
        this.ExpenseId = ExpenseId;
    }

    public String getItemName ()
    {
        return ItemName;
    }

    public void setItemName (String ItemName)
    {
        this.ItemName = ItemName;
    }

    public String getQuantity ()
    {
        return Quantity;
    }

    public void setQuantity (String Quantity)
    {
        this.Quantity = Quantity;
    }

    public String getTotalitemAmount ()
    {
        return TotalitemAmount;
    }

    public void setTotalitemAmount (String TotalitemAmount)
    {
        this.TotalitemAmount = TotalitemAmount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ExpenseItemId = "+ExpenseItemId+", ExpNumber = "+ExpNumber+", UnitPrice = "+UnitPrice+", ExpenseId = "+ExpenseId+", ItemName = "+ItemName+", Quantity = "+Quantity+", TotalitemAmount = "+TotalitemAmount+"]";
    }
}
