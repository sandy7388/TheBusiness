package com.example.admin.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.PartyInvoiceAdapter;
import com.example.admin.demo.adapter.UserListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.PartyInvoicePojo;
import com.example.admin.demo.model.UserListPojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageUserActivity extends AppCompatActivity {
    private Toolbar toolbar_user_list;
    private TextView tv_app_name_nav_bar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar pb_user_list;
    private SessionManagement session;
    private String strLoginId,strCompanyId;
    private ArrayList<UserListPojo> userListPojoArrayList;
    private SoapPrimitive resultString;
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        toolbar_user_list = findViewById(R.id.toolbar_user_list);
        View view = toolbar_user_list.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        floatingActionButton = findViewById(R.id.fb_add_user);
        recyclerView = findViewById(R.id.recyclerView);
        tv_app_name_nav_bar.setText("User's List");
        Functions.setToolBar(ManageUserActivity.this, toolbar_user_list, "Add Item", true);
        pb_user_list = findViewById(R.id.pb_user_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListAdapter.isUserEdit = false;
                startActivity(new Intent(ManageUserActivity.this,AddUserActivity.class));
            }
        });

        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strLoginId = user.get(SessionManagement.KEY_LOGIN_ID);
            strCompanyId = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        new AsyncCallUserList().execute(strCompanyId);

    }

    private class AsyncCallUserList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_user_list.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String strPartyId = params[0];
            userListPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListUser";
            String METHOD_NAME = "ListUser";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("compId", strPartyId);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

            }catch (Exception e){
                e.printStackTrace();
            }

            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            pb_user_list.setVisibility(View.GONE);
            try {
                if (!TextUtils.isEmpty(result)) {
                    final JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jsonObject = jarray.getJSONObject(i);
                        userListPojoArrayList.add(new UserListPojo(jsonObject.getString("UserMobile"),
                                jsonObject.getString("UserName"),jsonObject.getString("UserId"),
                                jsonObject.getString("UserLoginId"),jsonObject.getString("UserAddress"),
                                jsonObject.getString("UserEmail"),jsonObject.getString("UserStatus")));
                        if (userListAdapter
                                != null) {
                            userListAdapter
                                    .notifyDataSetChanged();
                        }
                    }
                    userListAdapter = new UserListAdapter(userListPojoArrayList,ManageUserActivity.this );
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ManageUserActivity.this));
                    recyclerView.setAdapter(userListAdapter);

                } else {
                    Toast.makeText(ManageUserActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (AddUserActivity.isRefreshed){
            AsyncCallUserList callInvoiceList = new AsyncCallUserList();
            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strCompanyId);
            AddUserActivity.isRefreshed = false;
        }
    }
}
