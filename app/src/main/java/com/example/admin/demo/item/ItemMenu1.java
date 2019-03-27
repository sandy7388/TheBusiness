package com.example.admin.demo.item;

public class ItemMenu1 {
    String name;
    String productdestails, price, qty;

    public ItemMenu1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ItemMenu1(String productdestails, String price, String qty) {
        this.productdestails = productdestails;
        this.price = price;
        this.qty = qty;
    }

    public String getProductdestails() {
        return productdestails;
    }

    public void setProductdestails(String productdestails) {
        this.productdestails = productdestails;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
