package com.example.admin.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.demo.fragment.RateFragment;
import com.example.admin.demo.fragment.TaxGroupFragment;


public class ViewPagerTabAdapter extends FragmentPagerAdapter {

    public ViewPagerTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new RateFragment();
        } else if (position == 1) {
            fragment = new TaxGroupFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Tax Rates";
        } else if (position == 1) {
            title = "Tax Groups";
        }
        return title;
    }
}