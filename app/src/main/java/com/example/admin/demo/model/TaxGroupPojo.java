package com.example.admin.demo.model;

public class TaxGroupPojo
{
    private String GroupName;

    private String TaxType;

    private String TaxRateName;

    private String TaxGroupId;

    private String TaxRate;

    private String TaxId;

    public TaxGroupPojo(String groupName, String taxType, String taxRateName,
                        String taxGroupId, String taxRate, String taxId) {
        GroupName = groupName;
        TaxType = taxType;
        TaxRateName = taxRateName;
        TaxGroupId = taxGroupId;
        TaxRate = taxRate;
        TaxId = taxId;
    }

    public String getGroupName ()
    {
        return GroupName;
    }

    public void setGroupName (String GroupName)
    {
        this.GroupName = GroupName;
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

    public String getTaxGroupId ()
    {
        return TaxGroupId;
    }

    public void setTaxGroupId (String TaxGroupId)
    {
        this.TaxGroupId = TaxGroupId;
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
        return "ClassPojo [GroupName = "+GroupName+", TaxType = "+TaxType+", TaxRateName = "+TaxRateName+", TaxGroupId = "+TaxGroupId+", TaxRate = "+TaxRate+", TaxId = "+TaxId+"]";
    }
}
