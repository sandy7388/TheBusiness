package com.example.admin.demo.model;

public class DeliveryBy
{
    public DeliveryBy(String userName, String userLoginId) {
        UserName = userName;
        UserLoginId = userLoginId;
    }

    private String UserMobile;

    private String UserTypeId;

    private String UserName;

    private String CompanyId;

    private String UserId;

    private String UserPassword;

    private String UserLoginId;

    private String UserAddress;

    private String UserEmail;

    private String UserStatus;

    public String getUserMobile ()
    {
        return UserMobile;
    }

    public void setUserMobile (String UserMobile)
    {
        this.UserMobile = UserMobile;
    }

    public String getUserTypeId ()
    {
        return UserTypeId;
    }

    public void setUserTypeId (String UserTypeId)
    {
        this.UserTypeId = UserTypeId;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getUserPassword ()
    {
        return UserPassword;
    }

    public void setUserPassword (String UserPassword)
    {
        this.UserPassword = UserPassword;
    }

    public String getUserLoginId ()
    {
        return UserLoginId;
    }

    public void setUserLoginId (String UserLoginId)
    {
        this.UserLoginId = UserLoginId;
    }

    public String getUserAddress ()
    {
        return UserAddress;
    }

    public void setUserAddress (String UserAddress)
    {
        this.UserAddress = UserAddress;
    }

    public String getUserEmail ()
    {
        return UserEmail;
    }

    public void setUserEmail (String UserEmail)
    {
        this.UserEmail = UserEmail;
    }

    public String getUserStatus ()
    {
        return UserStatus;
    }

    public void setUserStatus (String UserStatus)
    {
        this.UserStatus = UserStatus;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [UserMobile = "+UserMobile+", UserTypeId = "+UserTypeId+", UserName = "+UserName+", CompanyId = "+CompanyId+", UserId = "+UserId+", UserPassword = "+UserPassword+", UserLoginId = "+UserLoginId+", UserAddress = "+UserAddress+", UserEmail = "+UserEmail+", UserStatus = "+UserStatus+"]";
    }
}
