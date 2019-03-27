package com.example.admin.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.CompanyListAdapter;
import com.example.admin.demo.functions.SharedPreferencesDatabase;
import com.example.admin.demo.R;
import com.example.admin.demo.model.LoginPojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private TextView tv_new_user;
    private SoapPrimitive resultString;
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private LinearLayout ll_main;
    AlertDialog alertDialog;
    AlertDialog alertDialog1;
    AlertDialog.Builder alertDialogBuilder;
    String SOAP_ACTION;
    private RecyclerView rv_company_type;
    SoapObject request = null, objMessages = null;
    private CompanyListAdapter companyListAdapter;
    SoapSerializationEnvelope envelope;
    private EditText et_select_company, et_password, et_name;
    private String comp_id = "";
    String str_status = "";
    SessionManagement session;
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferencesDatabase = new SharedPreferencesDatabase(this);
        sharedPreferencesDatabase.createDatabase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = LoginActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (arePermissionsEnabled()) {

            } else {
                requestMultiplePermissions();
            }
        }
        session = new SessionManagement(this);
        btn_login = findViewById(R.id.btn_login);
        ll_main = findViewById(R.id.ll_main);
        et_select_company = findViewById(R.id.et_select_company);
        et_password = findViewById(R.id.et_password);
        et_name = findViewById(R.id.et_name);
        tv_new_user = findViewById(R.id.tv_new_user);
        tv_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_username = et_name.getText().toString();
                String str_password = et_password.getText().toString();
                if (TextUtils.isEmpty(str_username)) {
                    Toast.makeText(LoginActivity.this, "Enter User Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }  else {
                    AsyncCallWS asyncCallWS = new AsyncCallWS();
                    asyncCallWS.execute(str_username, str_password);
                }


            }
        });
    }



    private class AsyncCallWS extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String uname = params[0];
            String pwd = params[1];

            String SOAP_ACTION = "http://tempuri.org/GetLogin";
            String METHOD_NAME = "GetLogin";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("userId", uname);
                Request.addProperty("userPassword", pwd);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (responseJSON.equals(null)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                } else {
                    final JSONArray jarray = new JSONArray(responseJSON);
                    final String UserLoginId = jarray.getJSONObject(0).getString("UserLoginId");
                    final String Username = jarray.getJSONObject(0).getString("UserName");
                    final String CompanyName = jarray.getJSONObject(0).getString("CompanyName");
                    final String CompanyId = jarray.getJSONObject(0).getString("CompanyId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Toast.makeText(LoginActivity.this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                            LoginPojo loginPojo = new LoginPojo();
                            loginPojo.setUserName(Username);
                            loginPojo.setUserLoginId(UserLoginId);
                            loginPojo.setCompanyId(CompanyId);
                            loginPojo.setCompanyName(CompanyName);
                            session.createLoginSession(loginPojo);
                            startActivity(intent);
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

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("error")) {
                Toast.makeText(LoginActivity.this, "Please Enter Valid User Name And Password", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(result)) {
                Toast.makeText(LoginActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();

            } else if (result.equals("success")) {
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }
}
