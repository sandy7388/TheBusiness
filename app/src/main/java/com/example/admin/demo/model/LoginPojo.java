package com.example.admin.demo.model;

public class LoginPojo
{
    private String UserMobile;

    private String CompanyId;

    private String UserAddress;

    private String UserStatus;

    private String UserLoginId;

    private String UserName;

    private String UserEmail;

    private String UserPassword;

    private String UserId;

    private String UserTypeId;

    private String CompanyName;

    private String CompanyPhone;

    private String CompanyEmail;

    public String getUserMobile ()
    {
        return UserMobile;
    }

    public void setUserMobile (String UserMobile)
    {
        this.UserMobile = UserMobile;
    }

    public String getCompanyId ()
    {
        return CompanyId;
    }

    public void setCompanyId (String CompanyId)
    {
        this.CompanyId = CompanyId;
    }

    public String getUserAddress ()
    {
        return UserAddress;
    }

    public void setUserAddress (String UserAddress)
    {
        this.UserAddress = UserAddress;
    }

    public String getUserStatus ()
    {
        return UserStatus;
    }

    public void setUserStatus (String UserStatus)
    {
        this.UserStatus = UserStatus;
    }

    public String getUserLoginId ()
    {
        return UserLoginId;
    }

    public void setUserLoginId (String UserLoginId)
    {
        this.UserLoginId = UserLoginId;
    }

    public String getUserName ()
    {
        return UserName;
    }

    public void setUserName (String UserName)
    {
        this.UserName = UserName;
    }

    public String getUserEmail ()
    {
        return UserEmail;
    }

    public void setUserEmail (String UserEmail)
    {
        this.UserEmail = UserEmail;
    }

    public String getUserPassword ()
    {
        return UserPassword;
    }

    public void setUserPassword (String UserPassword)
    {
        this.UserPassword = UserPassword;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getUserTypeId ()
    {
        return UserTypeId;
    }

    public void setUserTypeId (String UserTypeId)
    {
        this.UserTypeId = UserTypeId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyPhone() {
        return CompanyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        CompanyPhone = companyPhone;
    }

    public String getCompanyEmail() {
        return CompanyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        CompanyEmail = companyEmail;
    }

    @Override
    public String toString() {
        return "LoginPojo{" +
                "UserMobile='" + UserMobile + '\'' +
                ", CompanyId='" + CompanyId + '\'' +
                ", UserAddress='" + UserAddress + '\'' +
                ", UserStatus='" + UserStatus + '\'' +
                ", UserLoginId='" + UserLoginId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", UserEmail='" + UserEmail + '\'' +
                ", UserPassword='" + UserPassword + '\'' +
                ", UserId='" + UserId + '\'' +
                ", UserTypeId='" + UserTypeId + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", CompanyPhone='" + CompanyPhone + '\'' +
                ", CompanyEmail='" + CompanyEmail + '\'' +
                '}';
    }
}
