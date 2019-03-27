package com.example.admin.demo.model;

public class UnitConversionPojo
{
    private String MainUnitId;

    private String ConversionRate;

    private String ConversionId;

    private String ConversionUnitName;

    private String MainUnitName;

    private String ConversionUnitId;

    public String getMainUnitId ()
    {
        return MainUnitId;
    }

    public void setMainUnitId (String MainUnitId)
    {
        this.MainUnitId = MainUnitId;
    }

    public String getConversionRate ()
    {
        return ConversionRate;
    }

    public void setConversionRate (String ConversionRate)
    {
        this.ConversionRate = ConversionRate;
    }

    public String getConversionId ()
    {
        return ConversionId;
    }

    public void setConversionId (String ConversionId)
    {
        this.ConversionId = ConversionId;
    }

    public String getConversionUnitName ()
    {
        return ConversionUnitName;
    }

    public void setConversionUnitName (String ConversionUnitName)
    {
        this.ConversionUnitName = ConversionUnitName;
    }

    public String getMainUnitName ()
    {
        return MainUnitName;
    }

    public void setMainUnitName (String MainUnitName)
    {
        this.MainUnitName = MainUnitName;
    }

    public String getConversionUnitId ()
    {
        return ConversionUnitId;
    }

    public void setConversionUnitId (String ConversionUnitId)
    {
        this.ConversionUnitId = ConversionUnitId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MainUnitId = "+MainUnitId+", ConversionRate = "+ConversionRate+", ConversionId = "+ConversionId+", ConversionUnitName = "+ConversionUnitName+", MainUnitName = "+MainUnitName+", ConversionUnitId = "+ConversionUnitId+"]";
    }
}
