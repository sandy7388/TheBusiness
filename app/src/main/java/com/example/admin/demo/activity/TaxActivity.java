package com.example.admin.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.admin.demo.adapter.ViewPagerTabAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;

public class TaxActivity extends AppCompatActivity {
    TabLayout tabs;
    ViewPager viewpager;
    TextView appNameNavBar;
    ViewPagerTabAdapter viewPagerAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        Functions.setToolBar(TaxActivity.this, toolbar, "Tax List", true);

        viewPagerAdapter = new ViewPagerTabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewpager);
    }

}

