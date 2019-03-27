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
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.CompanyListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.functions.OnRefreshView;
import com.example.admin.demo.item.ItemCompanyList;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class CompanyListActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshView {

    private FloatingActionButton fb_add_company;
    private SoapPrimitive resultString;
    private CompanyListAdapter companyListAdapter;
    private RecyclerView rv_company_list;
    private Toolbar toolbar_company_list;
    private TextView tv_app_name_nav_bar;
    public static boolean isCompanyAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        toolbar_company_list = findViewById(R.id.toolbar_company_list);
        View view = toolbar_company_list.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Manage Company");
        Functions.setToolBar(CompanyListActivity.this, toolbar_company_list,
                "Add Item", true);
        rv_company_list = findViewById(R.id.rv_company_list);
        fb_add_company = findViewById(R.id.fb_add_company);
        fb_add_company.setOnClickListener(this);
        new AsyncCallCompanyList().execute("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_add_company:
                CompanyListAdapter.isCompanyEdit = false;
                startActivity(new Intent(this,AddCompanyActivity.class));
                break;
        }
    }

    @Override
    public void Onrefresh(String name, String id) {

    }

    private class AsyncCallCompanyList extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            companyListAdapter = new CompanyListAdapter(CompanyListActivity.this, getCompanyList());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_company_list.setNestedScrollingEnabled(false);
                    rv_company_list.setHasFixedSize(true);
                    rv_company_list.setLayoutManager(new LinearLayoutManager(CompanyListActivity.this));
                    rv_company_list.setAdapter(companyListAdapter);

                }

            });

            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(CompanyListActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(CompanyListActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public ArrayList<ItemCompanyList> getCompanyList() {
        final ArrayList<ItemCompanyList> homeItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/GetCompanyList";
        String METHOD_NAME = "GetCompanyList";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
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
                final JSONArray jarray = new JSONArray(responseJSON);
                for (int i = 0; i < jarray.length(); i++) {
                    final String CompanyId = jarray.getJSONObject(i).getString("CompanyId");
                    final String CompanyName = jarray.getJSONObject(i).getString("CompanyName");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            homeItems.add(new ItemCompanyList(CompanyName, CompanyId));
                            if (companyListAdapter
                                    != null) {
                                companyListAdapter
                                        .notifyDataSetChanged();
                            }
                        }
                    });


                }


            } else {
                Toast.makeText(this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

        }
        return homeItems;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isCompanyAdded) {
            new AsyncCallCompanyList().execute("1");
            isCompanyAdded = false;
        }
    }
}
