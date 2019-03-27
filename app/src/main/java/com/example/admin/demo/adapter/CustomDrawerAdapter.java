package com.example.admin.demo.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.demo.item.DrawerItem;
import com.example.admin.demo.R;


public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = view
                    .findViewById(R.id.drawer_itemName);
            drawerHolder.icon = view.findViewById(R.id.drawer_icon);
            drawerHolder.main_image = view.findViewById(R.id.drawer_main);
            drawerHolder.ItemName = view.findViewById(R.id.drawer_itemName);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                dItem.getImg2()));
        drawerHolder.ItemName.setText(dItem.getItemName());
        drawerHolder.main_image.setImageDrawable(view.getResources().getDrawable(
                dItem.getImgResID()));

        return view;
    }

    private static class DrawerItemHolder {
        TextView drawer_itemTitle, ItemName;
        ImageView icon, main_image;
    }
}