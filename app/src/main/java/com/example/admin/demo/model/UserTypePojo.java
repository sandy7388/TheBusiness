package com.example.admin.demo.model;


public class UserTypePojo
{
    private String UserTypeId;

    private String UserType;

    public String getUserTypeId ()
    {
        return UserTypeId;
    }

    public void setUserTypeId (String UserTypeId)
    {
        this.UserTypeId = UserTypeId;
    }

    public String getUserType ()
    {
        return UserType;
    }

    public void setUserType (String UserType)
    {
        this.UserType = UserType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UserTypeId = "+UserTypeId+", UserType = "+UserType+"]";
    }
}
