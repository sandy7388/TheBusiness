package com.example.admin.demo.model;

public class ProductDetailsPojo
{
    private String AsOfDate;

    private String ItemCode;

    private String PurchasePrice;

    private String OpeningStock;

    private String CategoryName;

    private String HSNCode;

    private String CompanyId;

    private String ProductId;

    private String CategoryId;

    private String SalePrice;

    private String MinimumStockQty;

    private String ShortName;

    private String TaxId;

    private String UserLoginId;

    private String PricePerUnit;

    private String ProductName;

    private String IsIncludeTax;

    private String SrNo;

    private String UnitId;

    private String UnitName;

    private String TaxGroupName;

    public String getTaxGroupName() {
        return TaxGroupName;
    }

    public void setTaxGroupName(String taxGroupName) {
        TaxGroupName = taxGroupName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getAsOfDate ()
    {
        return AsOfDate;
    }

    public void setAsOfDate (String AsOfDate)
    {
        this.AsOfDate = AsOfDate;
    }

    public String getItemCode ()
    {
        return ItemCode;
    }

    public void setItemCode (String ItemCode)
    {
        this.ItemCode = ItemCode;
    }

    public String getPurchasePrice ()
    {
        return PurchasePrice;
    }

    public void setPurchasePrice (String PurchasePrice)
    {
        this.PurchasePrice = PurchasePrice;
    }

    public String getOpeningStock ()
    {
        return OpeningStock;
    }

    public void setOpeningStock (String OpeningStock)
    {
        this.OpeningStock = OpeningStock;
    }

    public String getCategoryName ()
    {
        return CategoryName;
    }

    public void setCategoryName (String CategoryName)
    {
        this.CategoryName = CategoryName;
    }

    public String getHSNCode ()
    {
        return HSNCode;
    }

    public void setHSNCode (String HSNCode)
    {
        this.HSNCode = HSNCode;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getProductId ()
    {
        return ProductId;
    }

    public void setProductId (String ProductId)
    {
        this.ProductId = ProductId;
    }

    public String getCategoryId ()
    {
        return CategoryId;
    }

    public void setCategoryId (String CategoryId)
    {
        this.CategoryId = CategoryId;
    }

    public String getSalePrice ()
    {
        return SalePrice;
    }

    public void setSalePrice (String SalePrice)
    {
        this.SalePrice = SalePrice;
    }

    public String getMinimumStockQty ()
    {
        return MinimumStockQty;
    }

    public void setMinimumStockQty (String MinimumStockQty)
    {
        this.MinimumStockQty = MinimumStockQty;
    }

    public String getShortName ()
    {
        return ShortName;
    }

    public void setShortName (String ShortName)
    {
        this.ShortName = ShortName;
    }

    public String getTaxId ()
    {
        return TaxId;
    }

    public void setTaxId (String TaxId)
    {
        this.TaxId = TaxId;
    }

    public String getUserLoginId ()
    {
        return UserLoginId;
    }

    public void setUserLoginId (String UserLoginId)
    {
        this.UserLoginId = UserLoginId;
    }

    public String getPricePerUnit ()
    {
        return PricePerUnit;
    }

    public void setPricePerUnit (String PricePerUnit)
    {
        this.PricePerUnit = PricePerUnit;
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

    public String getSrNo ()
    {
        return SrNo;
    }

    public void setSrNo (String SrNo)
    {
        this.SrNo = SrNo;
    }

    public String getUnitId ()
    {
        return UnitId;
    }

    public void setUnitId (String UnitId)
    {
        this.UnitId = UnitId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AsOfDate = "+AsOfDate+", ItemCode = "+ItemCode+", PurchasePrice = "+PurchasePrice+", OpeningStock = "+OpeningStock+", CategoryName = "+CategoryName+", HSNCode = "+HSNCode+", CompanyId = "+CompanyId+", ProductId = "+ProductId+", CategoryId = "+CategoryId+", SalePrice = "+SalePrice+", MinimumStockQty = "+MinimumStockQty+", ShortName = "+ShortName+", TaxId = "+TaxId+", UserLoginId = "+UserLoginId+", PricePerUnit = "+PricePerUnit+", ProductName = "+ProductName+", IsIncludeTax = "+IsIncludeTax+", SrNo = "+SrNo+", UnitId = "+UnitId+"]";
    }
}
