package com.example.admin.demo.session;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.admin.demo.activity.MainActivity;
import com.example.admin.demo.activity.LoginActivity;
import com.example.admin.demo.model.LoginPojo;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_NAME = "user_name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // LOGIN_ID address (make variable public to access from outside)
    public static final String KEY_LOGIN_ID = "login_id";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_STATUS = "user_status";

    // Email address (make variable public to access from outside)
    public static final String KEY_USER_TYPE = "user_type";

    // LOGIN_ID address (make variable public to access from outside)
    public static final String KEY_COMPANY_ID = "company_id";

    // User name (make variable public to access from outside)
    public static final String KEY_COMPANY_NAME = "company_name";

    // Email address (make variable public to access from outside)
    public static final String KEY_COMPANY_EMAIL = "company_email";

    // LOGIN_ID address (make variable public to access from outside)
    public static final String KEY_COMPANY_PHONE = "company_phone";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(LoginPojo loginPojo){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_NAME, loginPojo.getUserName());

        // Storing email in pref
        editor.putString(KEY_EMAIL, loginPojo.getUserEmail());

        editor.putString(KEY_LOGIN_ID, loginPojo.getUserLoginId());

        editor.putString(KEY_COMPANY_ID, loginPojo.getCompanyId());

        editor.putString(KEY_COMPANY_NAME, loginPojo.getCompanyName());

        editor.putString(KEY_COMPANY_EMAIL, loginPojo.getCompanyEmail());

        editor.putString(KEY_COMPANY_PHONE, loginPojo.getCompanyPhone());

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

        }

        else {
            _context.startActivity(new Intent(_context, MainActivity.class));
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_LOGIN_ID,pref.getString(KEY_LOGIN_ID,null));
        user.put(KEY_COMPANY_ID,pref.getString(KEY_COMPANY_ID,null));
        user.put(KEY_COMPANY_NAME, pref.getString(KEY_COMPANY_NAME, null));
        user.put(KEY_COMPANY_EMAIL,pref.getString(KEY_COMPANY_EMAIL,null));
        user.put(KEY_COMPANY_PHONE,pref.getString(KEY_COMPANY_PHONE,null));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
