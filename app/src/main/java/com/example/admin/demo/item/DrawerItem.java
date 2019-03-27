package com.example.admin.demo.item;

public class DrawerItem {

    String MenuTitle, ItemName;
    int imgResID, img2;

    public DrawerItem(String menuTitle, String itemName, int imgResID, int img2) {
        MenuTitle = menuTitle;
        ItemName = itemName;
        this.imgResID = imgResID;
        this.img2 = img2;
    }

    public String getMenuTitle() {
        return MenuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        MenuTitle = menuTitle;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public int getImg2() {
        return img2;
    }

    public void setImg2(int img2) {
        this.img2 = img2;
    }
}