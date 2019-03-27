package com.example.admin.demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import com.crashlytics.android.Crashlytics;
import com.example.admin.demo.functions.SharedPreferencesDatabase;
import com.example.admin.demo.R;
import com.example.admin.demo.session.SessionManagement;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
         session = new SessionManagement(this);
        sharedPreferencesDatabase = new SharedPreferencesDatabase(this);
        sharedPreferencesDatabase.createDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SplashActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                session.checkLogin();
                finish();

            }
        }, 3000);
    }


}

