package com.example.admin.demo.model.edit_model;

public class ListInvoiceItems {
    private String ItemSubTotalAmount;

    private String ProductName;

    private String IsIncludeTax;

    private String ItemPrice;

    private String PurchasePrice;

    private String UnitName;

    private String TaxId;

    private String ItemDiscountAmount;

    private String FreeQuantity;

    private String TaxAmount;

    private String ItemTotalAmount;

    private String SalesInvoiceItemId;

    private String TaxGroupName;

    private String ItemQty;

    private String SalePrice;

    private String ItemMasterId;

    public ListInvoiceItems(String productName, String itemPrice,
                            String itemDiscountAmount, String taxAmount, String itemQty) {
        ProductName = productName;
        ItemPrice = itemPrice;
        ItemDiscountAmount = itemDiscountAmount;
        TaxAmount = taxAmount;
        ItemQty = itemQty;
    }

    public String getItemSubTotalAmount ()
    {
        return ItemSubTotalAmount;
    }

    public void setItemSubTotalAmount (String ItemSubTotalAmount)
    {
        this.ItemSubTotalAmount = ItemSubTotalAmount;
    }

    public String getProductName ()
    {
        return ProductName;
    }

    public void setProductName (String ProductName)
    {
        this.ProductName = ProductName;
    }

    public String getIsIncludeTax ()
    {
        return IsIncludeTax;
    }

    public void setIsIncludeTax (String IsIncludeTax)
    {
        this.IsIncludeTax = IsIncludeTax;
    }

    public String getItemPrice ()
    {
        return ItemPrice;
    }

    public void setItemPrice (String ItemPrice)
    {
        this.ItemPrice = ItemPrice;
    }

    public String getPurchasePrice ()
    {
        return PurchasePrice;
    }

    public void setPurchasePrice (String PurchasePrice)
    {
        this.PurchasePrice = PurchasePrice;
    }

    public String getUnitName ()
    {
        return UnitName;
    }

    public void setUnitName (String UnitName)
    {
        this.UnitName = UnitName;
    }

    public String getTaxId ()
    {
        return TaxId;
    }

    public void setTaxId (String TaxId)
    {
        this.TaxId = TaxId;
    }

    public String getItemDiscountAmount ()
    {
        return ItemDiscountAmount;
    }

    public void setItemDiscountAmount (String ItemDiscountAmount)
    {
        this.ItemDiscountAmount = ItemDiscountAmount;
    }

    public String getFreeQuantity ()
    {
        return FreeQuantity;
    }

    public void setFreeQuantity (String FreeQuantity)
    {
        this.FreeQuantity = FreeQuantity;
    }

    public String getTaxAmount ()
    {
        return TaxAmount;
    }

    public void setTaxAmount (String TaxAmount)
    {
        this.TaxAmount = TaxAmount;
    }

    public String getItemTotalAmount ()
    {
        return ItemTotalAmount;
    }

    public void setItemTotalAmount (String ItemTotalAmount)
    {
        this.ItemTotalAmount = ItemTotalAmount;
    }

    public String getSalesInvoiceItemId ()
    {
        return SalesInvoiceItemId;
    }

    public void setSalesInvoiceItemId (String SalesInvoiceItemId)
    {
        this.SalesInvoiceItemId = SalesInvoiceItemId;
    }

    public String getTaxGroupName ()
    {
        return TaxGroupName;
    }

    public void setTaxGroupName (String TaxGroupName)
    {
        this.TaxGroupName = TaxGroupName;
    }

    public String getItemQty ()
    {
        return ItemQty;
    }

    public void setItemQty (String ItemQty)
    {
        this.ItemQty = ItemQty;
    }

    public String getSalePrice ()
    {
        return SalePrice;
    }

    public void setSalePrice (String SalePrice)
    {
        this.SalePrice = SalePrice;
    }

    public String getItemMasterId ()
    {
        return ItemMasterId;
    }

    public void setItemMasterId (String ItemMasterId)
    {
        this.ItemMasterId = ItemMasterId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ItemSubTotalAmount = "+ItemSubTotalAmount+", ProductName = "+ProductName+", IsIncludeTax = "+IsIncludeTax+", ItemPrice = "+ItemPrice+", PurchasePrice = "+PurchasePrice+", UnitName = "+UnitName+", TaxId = "+TaxId+", ItemDiscountAmount = "+ItemDiscountAmount+", FreeQuantity = "+FreeQuantity+", TaxAmount = "+TaxAmount+", ItemTotalAmount = "+ItemTotalAmount+", SalesInvoiceItemId = "+SalesInvoiceItemId+", TaxGroupName = "+TaxGroupName+", ItemQty = "+ItemQty+", SalePrice = "+SalePrice+", ItemMasterId = "+ItemMasterId+"]";
    }
}
