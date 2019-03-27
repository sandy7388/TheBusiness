package com.example.admin.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.demo.fragment.PartyMenu1;
import com.example.admin.demo.fragment.TaxGroupFragment;

import java.util.ArrayList;
import java.util.List;


public class TabAdapter extends FragmentPagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position == 0) {
            fragment = new PartyMenu1();
        } else if (position == 1) {
            fragment = new TaxGroupFragment();
        } else if (position == 2) {
            fragment = new TaxGroupFragment();
        } else if (position == 3) {
            fragment = new TaxGroupFragment();
        }
        return fragment;
        //return mFragmentList.get(position);

    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(String title) {

        mFragmentTitleList.add(title);
    }

}