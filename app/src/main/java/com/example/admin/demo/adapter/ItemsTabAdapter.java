package com.example.admin.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.demo.fragment.ProductsItemFragment;
import com.example.admin.demo.fragment.CategoryProductFragment;
import com.example.admin.demo.fragment.UnitFragment;


public class ItemsTabAdapter extends FragmentPagerAdapter {

    public ItemsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ProductsItemFragment();
        } else if (position == 1) {
            fragment = new CategoryProductFragment();
        }
        else if (position == 2){
            fragment = new UnitFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Products";
        } else if (position == 1) {
            title = "Category";
        }
        else if (position == 2){
            title = "Unit";
        }
        return title;
    }
}