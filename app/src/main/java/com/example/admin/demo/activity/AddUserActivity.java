package com.example.admin.demo.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.UserListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.UserTypePojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private Toolbar toolbar_user_list;
    private TextView tv_app_name_nav_bar;
    private CoordinatorLayout cl_add_user;
    private EditText et_user_id,et_user_name,et_user_password,
            et_user_mobile,et_user_email,et_user_address;
    private Spinner sp_user_status,sp_user_type;
    private ProgressBar pb_add_user;
    private Button btn_save_user,btn_clear_user,btn_delete_user,btn_edit_user;
    private SessionManagement session;
    private String strLoginId,strCompanyId,strUserTye, strUserTypeId,strStatus,strUserId;
    private SoapPrimitive resultString;
    public List<UserTypePojo> userTypePojoList;
    private String[] userType;
    private RadioGroup rg_status;
    private RadioButton rb_active,rb_inactive;
    public static boolean isRefreshed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        toolbar_user_list = findViewById(R.id.toolbar_user_list);
        View view = toolbar_user_list.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Add User");
        Functions.setToolBar(AddUserActivity.this, toolbar_user_list, "Add Item", true);
        cl_add_user = findViewById(R.id.cl_add_user);
        et_user_id = findViewById(R.id.et_user_id);
        et_user_name = findViewById(R.id.et_user_name);
        et_user_password = findViewById(R.id.et_user_password);
        et_user_mobile = findViewById(R.id.et_user_mobile);
        et_user_email = findViewById(R.id.et_user_email);
        et_user_address = findViewById(R.id.et_user_address);
        sp_user_status = findViewById(R.id.sp_user_status);
        sp_user_type = findViewById(R.id.sp_user_type);
        pb_add_user = findViewById(R.id.pb_add_user);
        btn_save_user = findViewById(R.id.btn_save_user);
        btn_clear_user = findViewById(R.id.btn_clear_user);
        btn_edit_user = findViewById(R.id.btn_edit_user);
        btn_delete_user = findViewById(R.id.btn_delete_user);
        rg_status = findViewById(R.id.rg_status);
        rb_active = findViewById(R.id.rb_active);
        rb_inactive = findViewById(R.id.rb_inactive);
        btn_save_user.setOnClickListener(this);
        btn_clear_user.setOnClickListener(this);
        btn_edit_user.setOnClickListener(this);
        btn_delete_user.setOnClickListener(this);
        sp_user_status.setOnItemSelectedListener(this);
        sp_user_type.setOnItemSelectedListener(this);
        rg_status.setOnCheckedChangeListener(this);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strLoginId = user.get(SessionManagement.KEY_LOGIN_ID);
            strCompanyId = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        AsyncCallUserType asyncCallUserType = new AsyncCallUserType();
        asyncCallUserType.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");

        if (UserListAdapter.isUserEdit){
            strUserId = getIntent().getStringExtra("USER_ID");
            new AsyncCallUserDetails().execute(strUserId);
            btn_delete_user.setVisibility(View.VISIBLE);
            btn_clear_user.setVisibility(View.GONE);
            btn_edit_user.setVisibility(View.VISIBLE);
            btn_save_user.setVisibility(View.GONE);
            et_user_id.setEnabled(false);
            et_user_name.setEnabled(false);
            et_user_mobile.setEnabled(false);
            et_user_password.setEnabled(false);
            et_user_email.setEnabled(false);
            et_user_address.setEnabled(false);
            sp_user_type.setEnabled(false);
            rb_inactive.setEnabled(false);
            rb_active.setEnabled(false);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear_user:
                clearAllField();
                break;
            case R.id.btn_save_user:
                submitUser();
                break;

            case R.id.btn_delete_user:
                new AsyncCallDeleteUser().execute(strUserId);
                break;
            case R.id.btn_edit_user:
                getEditables();
                break;
        }

    }

    private void getEditables() {

        et_user_id.setEnabled(true);
        et_user_name.setEnabled(true);
        et_user_mobile.setEnabled(true);
        et_user_password.setEnabled(true);
        et_user_email.setEnabled(true);
        et_user_address.setEnabled(true);
        sp_user_type.setEnabled(true);
        rb_inactive.setEnabled(true);
        rb_active.setEnabled(true);
        btn_edit_user.setVisibility(View.GONE);
        btn_save_user.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sp_user_type:
            strUserTye = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< userTypePojoList.size(); i++){
                    if (strUserTye.equals(userTypePojoList.get(i).getUserType())){
                        strUserTypeId = userTypePojoList.get(i).getUserTypeId();
                    }
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void submitUser() {

        if (TextUtils.isEmpty(et_user_id.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter user id", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_user_name.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter username", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_user_password.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter Password", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_user_mobile.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter mobile number", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_user_email.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter email address", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_user_address.getText().toString())) {
            Snackbar.make(cl_add_user, "Please enter user address", Snackbar.LENGTH_SHORT).show();

        } else if (rg_status.getCheckedRadioButtonId() == -1) {
            Snackbar.make(cl_add_user, "Please select either active or inactive", Snackbar.LENGTH_SHORT).show();

        } else if (strUserTypeId == null) {
            Snackbar.make(cl_add_user, "Please select user type", Snackbar.LENGTH_SHORT).show();

        }
        else {

            AsyncCallAddUser addProduct = new AsyncCallAddUser();
            addProduct.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    et_user_id.getText().toString(),
                    et_user_name.getText().toString(),
                    et_user_password.getText().toString(),
                    et_user_mobile.getText().toString(),
                    et_user_email.getText().toString(),
                    et_user_address.getText().toString());

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        RadioButton checkedRadioButton = group.findViewById(checkedId);
        boolean isChecked = checkedRadioButton.isChecked();
        switch (group.getId())
        {
            case R.id.rg_status:
                if (checkedId == R.id.rb_active){
                    strStatus = checkedRadioButton.getText().toString();
                }
                else if (checkedId == R.id.rb_inactive){
                    strStatus = checkedRadioButton.getText().toString();

                }
        }

    }

    private class AsyncCallAddUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //pb_add_user.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String userName = params[1];
            String password = params[2];
            String mobile = params[3];
            String email = params[4];
            String address = params[5];

            addUserData(userId, userName, password, mobile, email,
                    address,strStatus, strUserTypeId,strCompanyId);
            return userId;
        }

        @Override
        protected void onPostExecute(String result) {
            isRefreshed = true;
            if (result.equals("success")) {
                //pb_add_user.setVisibility(View.GONE);
                isRefreshed = true;
                Toast.makeText(AddUserActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                //pb_add_user.setVisibility(View.GONE);
                Toast.makeText(AddUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String addUserData(String userId, String userName, String password,
                             String mobile, String email, String address,
                             String status, String type, String strCompanyId) {

        String SOAP_ACTION,METHOD_NAME;
        if (UserListAdapter.isUserEdit){
            SOAP_ACTION = "http://tempuri.org/UpdateUserLogin";
            METHOD_NAME = "UpdateUserLogin";
        }
        else {
            SOAP_ACTION = "http://tempuri.org/SaveUserLogin";
            METHOD_NAME = "SaveUserLogin";
        }
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("userId", userId);
            Request.addProperty("userName", userName);
            Request.addProperty("userPassword", password);
            Request.addProperty("mobile", mobile);
            Request.addProperty("userEmail", email);
            Request.addProperty("userAddress", address);
            Request.addProperty("status", status);
            Request.addProperty("userType", type);
            Request.addProperty("compId", strCompanyId);
            if (UserListAdapter.isUserEdit){
                Request.addProperty("loginId", strUserId);
            }
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (responseJSON.equals(null)) {
                Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (UserListAdapter.isUserEdit){
                            Toast.makeText(AddUserActivity.this, "User Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddUserActivity.this, "User Successfully Added", Toast.LENGTH_SHORT).show();
                        }

                        clearAllField();
                        finish();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }

        return "success";
    }

    private class AsyncCallUserType extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            userTypePojoList = new ArrayList<>();
            String id = params[0];
            String SOAP_ACTION = "http://tempuri.org/ListUserType";
            String METHOD_NAME = "ListUserType";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String UserTypeId = jarray.getJSONObject(i).getString("UserTypeId");
                        final String UserType = jarray.getJSONObject(i).getString("UserType");
                        UserTypePojo userTypePojo = new UserTypePojo();
                        userTypePojo.setUserType(UserType);
                        userTypePojo.setUserTypeId(UserTypeId);
                        userTypePojoList.add(userTypePojo);
                    }

                    userType = new String[userTypePojoList.size()];
                    for (int j = 0; j < userTypePojoList.size(); j++) {
                        userType[j] = userTypePojoList.get(j).getUserType();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler1 = new Handler(Looper.getMainLooper());
            uiHandler1.post(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddUserActivity.this,
                            android.R.layout.simple_spinner_item, userType);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_user_type.setAdapter(dataAdapter);
                }
            });
        }

    }

    private void clearAllField() {
        et_user_id.setText("");
        et_user_name.setText("");
        et_user_password.setText("");
        et_user_mobile.setText("");
        et_user_email.setText("");
        et_user_address.setText("");
        sp_user_type.setSelection(0);
        if (rb_active.isChecked()|| rb_inactive.isChecked()){
            rb_active.setChecked(false);
            rb_inactive.setChecked(false);
        }
    }

    private class AsyncCallUserDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/GetUserDetailById";
            String METHOD_NAME = "GetUserDetailById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("uId", id);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONArray jarray = new JSONArray(responseJSON);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (jarray.getJSONObject(0).getString("UserId").equals("0")) {
                                    et_user_id.setText("");
                                } else {
                                    et_user_id.setText(jarray.getJSONObject(0).getString("UserId"));
                                }
                                if (jarray.getJSONObject(0).getString("UserName").equals("0")) {
                                    et_user_name.setText("");
                                }
                                else {
                                    et_user_name.setText(jarray.getJSONObject(0).getString("UserName"));
                                }

                                if (jarray.getJSONObject(0).getString("UserPassword").equals("0")) {
                                    et_user_password.setText("");
                                } else {
                                    et_user_password.setText(jarray.getJSONObject(0).getString("UserPassword"));
                                }

                                if (jarray.getJSONObject(0).getString("UserEmail").equals("0")) {
                                    et_user_email.setText("");
                                } else {
                                    et_user_email.setText(jarray.getJSONObject(0).getString("UserEmail"));
                                }

                                if (jarray.getJSONObject(0).getString("UserMobile").equals("0")) {
                                    et_user_mobile.setText("");
                                } else {
                                    et_user_mobile.setText(jarray.getJSONObject(0).getString("UserMobile"));
                                }

                                if (jarray.getJSONObject(0).getString("UserAddress").equals("0")) {
                                    et_user_address.setText("");
                                } else {
                                    et_user_address.setText(jarray.getJSONObject(0).getString("UserAddress"));
                                }


                                if (jarray.getJSONObject(0).getString("UserStatus").equals("Active")) {
                                    rb_active.setChecked(true);
                                    rb_inactive.setChecked(false);
                                } else {
                                    rb_active.setChecked(false);
                                    rb_inactive.setChecked(true);
                                }
                                strUserTypeId = jarray.getJSONObject(0).getString("UserTypeId");
                                for (int i = 0; i < userTypePojoList.size(); i++) {
                                    if (strUserTypeId.equals(userTypePojoList.get(i).getUserTypeId())) {
                                        sp_user_type.setSelection(i);
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } else {
                    Toast.makeText(AddUserActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler1 = new Handler(Looper.getMainLooper());
            uiHandler1.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

    }

    private class AsyncCallDeleteUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteUser";
            String METHOD_NAME = "DeleteUser";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("userId", id);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddUserActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(AddUserActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }


    }
}
