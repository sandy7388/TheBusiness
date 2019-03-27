package com.example.admin.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.demo.activity.PartyListActivity;
import com.example.admin.demo.fragment.PartyListFragment;
import com.example.admin.demo.model.RoutePojo;

import java.util.List;


public class PartyListPagerAdapter extends FragmentPagerAdapter {
    static String title;


    List<RoutePojo> routePojos = PartyListActivity.routePojoList;


    public PartyListPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new PartyListFragment();
        return fragment;


    }

    @Override
    public int getCount() {
        return routePojos.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

        for (int i = 0; i < routePojos.size(); i++) {
            if (position == i) {
                title = routePojos.get(i).getRouteName();
            }
        }

        return title;

    }
}
