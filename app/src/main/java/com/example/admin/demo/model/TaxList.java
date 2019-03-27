package com.example.admin.demo.model;


public class TaxList
{
    private String TaxType;

    private String TaxRateName;

    private String TaxRate;

    private String TaxId;

    public TaxList(String taxRate) {
        TaxRate = taxRate;
    }

    public TaxList() {
    }

    public String getTaxType ()
    {
        return TaxType;
    }

    public void setTaxType (String TaxType)
    {
        this.TaxType = TaxType;
    }

    public String getTaxRateName ()
    {
        return TaxRateName;
    }

    public void setTaxRateName (String TaxRateName)
    {
        this.TaxRateName = TaxRateName;
    }

    public String getTaxRate ()
    {
        return TaxRate;
    }

    public void setTaxRate (String TaxRate)
    {
        this.TaxRate = TaxRate;
    }

    public String getTaxId ()
    {
        return TaxId;
    }

    public void setTaxId (String TaxId)
    {
        this.TaxId = TaxId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TaxType = "+TaxType+", TaxRateName = "+TaxRateName+", TaxRate = "+TaxRate+", TaxId = "+TaxId+"]";
    }
}
