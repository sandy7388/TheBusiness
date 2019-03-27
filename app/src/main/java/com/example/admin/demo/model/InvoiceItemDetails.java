package com.example.admin.demo.model;

public class InvoiceItemDetails
{
    private String UnitName;

    private String ItemCode;

    private String FreeQuantity;

    private String ItemSubTotalAmount;

    private String InvoiceNo;

    private String HSNCode;

    private String SalesInvoiceId;

    private String ItemTotalAmount;

    private String TaxId;

    private String ProductName;

    private String TaxAmount;

    private String ItemPrice;

    private String ItemMasterId;

    private String ItemDiscountAmount;

    private String ItemQty;

    private String SalesInvoiceItemId;

    private double Total;

    public InvoiceItemDetails() {
    }

    public InvoiceItemDetails(String itemSubTotalAmount, String productName,
                              String taxAmount, String itemPrice, String itemQty) {
        ItemSubTotalAmount = itemSubTotalAmount;
        ProductName = productName;
        TaxAmount = taxAmount;
        ItemPrice = itemPrice;
        ItemQty = itemQty;
    }

    public InvoiceItemDetails(String itemSubTotalAmount, String productName,
                              String taxAmount, String itemPrice, String itemQty, double total) {
        ItemSubTotalAmount = itemSubTotalAmount;
        ProductName = productName;
        TaxAmount = taxAmount;
        ItemPrice = itemPrice;
        ItemQty = itemQty;
        Total = total;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public String getUnitName ()
    {
        return UnitName;
    }

    public void setUnitName (String UnitName)
    {
        this.UnitName = UnitName;
    }

    public String getItemCode ()
    {
        return ItemCode;
    }

    public void setItemCode (String ItemCode)
    {
        this.ItemCode = ItemCode;
    }

    public String getFreeQuantity ()
    {
        return FreeQuantity;
    }

    public void setFreeQuantity (String FreeQuantity)
    {
        this.FreeQuantity = FreeQuantity;
    }

    public String getItemSubTotalAmount ()
    {
        return ItemSubTotalAmount;
    }

    public void setItemSubTotalAmount (String ItemSubTotalAmount)
    {
        this.ItemSubTotalAmount = ItemSubTotalAmount;
    }

    public String getInvoiceNo ()
    {
        return InvoiceNo;
    }

    public void setInvoiceNo (String InvoiceNo)
    {
        this.InvoiceNo = InvoiceNo;
    }

    public String getHSNCode ()
    {
        return HSNCode;
    }

    public void setHSNCode (String HSNCode)
    {
        this.HSNCode = HSNCode;
    }

    public String getSalesInvoiceId ()
    {
        return SalesInvoiceId;
    }

    public void setSalesInvoiceId (String SalesInvoiceId)
    {
        this.SalesInvoiceId = SalesInvoiceId;
    }

    public String getItemTotalAmount ()
    {
        return ItemTotalAmount;
    }

    public void setItemTotalAmount (String ItemTotalAmount)
    {
        this.ItemTotalAmount = ItemTotalAmount;
    }

    public String getTaxId ()
    {
        return TaxId;
    }

    public void setTaxId (String TaxId)
    {
        this.TaxId = TaxId;
    }

    public String getProductName ()
    {
        return ProductName;
    }

    public void setProductName (String ProductName)
    {
        this.ProductName = ProductName;
    }

    public String getTaxAmount ()
    {
        return TaxAmount;
    }

    public void setTaxAmount (String TaxAmount)
    {
        this.TaxAmount = TaxAmount;
    }

    public String getItemPrice ()
    {
        return ItemPrice;
    }

    public void setItemPrice (String ItemPrice)
    {
        this.ItemPrice = ItemPrice;
    }

    public String getItemMasterId ()
    {
        return ItemMasterId;
    }

    public void setItemMasterId (String ItemMasterId)
    {
        this.ItemMasterId = ItemMasterId;
    }

    public String getItemDiscountAmount ()
    {
        return ItemDiscountAmount;
    }

    public void setItemDiscountAmount (String ItemDiscountAmount)
    {
        this.ItemDiscountAmount = ItemDiscountAmount;
    }

    public String getItemQty ()
    {
        return ItemQty;
    }

    public void setItemQty (String ItemQty)
    {
        this.ItemQty = ItemQty;
    }

    public String getSalesInvoiceItemId ()
    {
        return SalesInvoiceItemId;
    }

    public void setSalesInvoiceItemId (String SalesInvoiceItemId)
    {
        this.SalesInvoiceItemId = SalesInvoiceItemId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UnitName = "+UnitName+", ItemCode = "+ItemCode+", FreeQuantity = "+FreeQuantity+", ItemSubTotalAmount = "+ItemSubTotalAmount+", InvoiceNo = "+InvoiceNo+", HSNCode = "+HSNCode+", SalesInvoiceId = "+SalesInvoiceId+", ItemTotalAmount = "+ItemTotalAmount+", TaxId = "+TaxId+", ProductName = "+ProductName+", TaxAmount = "+TaxAmount+", ItemPrice = "+ItemPrice+", ItemMasterId = "+ItemMasterId+", ItemDiscountAmount = "+ItemDiscountAmount+", ItemQty = "+ItemQty+", SalesInvoiceItemId = "+SalesInvoiceItemId+"]";
    }
}
