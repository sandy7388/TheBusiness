package com.example.admin.demo.model;

public class CategoryListPojo {
    private String strCategoryName,strCategoryId;

    public CategoryListPojo(String strCategoryName, String strCategoryId) {
        this.strCategoryName = strCategoryName;
        this.strCategoryId = strCategoryId;
    }

    public String getStrCategoryName() {
        return strCategoryName;
    }

    public void setStrCategoryName(String strCategoryName) {
        this.strCategoryName = strCategoryName;
    }

    public String getStrCategoryId() {
        return strCategoryId;
    }

    public void setStrCategoryId(String strCategoryId) {
        this.strCategoryId = strCategoryId;
    }
}
