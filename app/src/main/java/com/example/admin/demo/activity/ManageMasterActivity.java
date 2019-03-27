package com.example.admin.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;

public class ManageMasterActivity extends AppCompatActivity {
    private Toolbar toolbar_managemaster;
    private LinearLayout llToolbarAppbar;
    private TextView appNameNavBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managemaster);

        initView();
        Functions.setToolBar(ManageMasterActivity.this, toolbar_managemaster, "Manage Master", true);
        appNameNavBar.setText("Manage Master");

    }

    private void initView() {
        toolbar_managemaster = (Toolbar) findViewById(R.id.toolbar_managemaster);
        llToolbarAppbar = (LinearLayout) findViewById(R.id.ll_toolbar_appbar);
        View view = toolbar_managemaster.getRootView();
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
    }
}
