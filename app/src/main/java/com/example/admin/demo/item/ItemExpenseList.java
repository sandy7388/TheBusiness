package com.example.admin.demo.item;

public class ItemExpenseList {
    String str_expense_category, str_amount;


    public ItemExpenseList(String str_expense_category, String str_amount) {
        this.str_expense_category = str_expense_category;
        this.str_amount = str_amount;
    }

    public String getStr_expense_category() {
        return str_expense_category;
    }

    public void setStr_expense_category(String str_expense_category) {
        this.str_expense_category = str_expense_category;
    }

    public String getStr_amount() {
        return str_amount;
    }

    public void setStr_amount(String str_amount) {
        this.str_amount = str_amount;
    }
}

