package com.example.admin.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.BankAccountListAdapter;
import com.example.admin.demo.adapter.TaxAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.item.ItemBankList;
import com.example.admin.demo.R;
import com.example.admin.demo.model.TaxListPojo;
import com.example.admin.demo.session.SessionManagement;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class BankListActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionMenu fab_main;
    FloatingActionButton f_btn_add_bank, f_btn_add_bank_to_bank, f_btn_add_bank_transfer;
    private BankAccountListAdapter bankAccountListAdapter;
    private RecyclerView rv_bank_list;
    private Toolbar toolbar_bank_list;
    private TextView appname;
    public static boolean isBankAdded = false;
    private ArrayList<ItemBankList> bankLists;
    private SoapPrimitive resultString;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_list);
        toolbar_bank_list = findViewById(R.id.toolbar_bank_list);
        View view = toolbar_bank_list.getRootView();
        appname = view.findViewById(R.id.app_name_nav_bar);
        appname.setText("Bank Account List");
        Functions.setToolBar(BankListActivity.this, toolbar_bank_list, "Bank Account List", true);
        rv_bank_list = findViewById(R.id.rv_bank_list);
        fab_main = (FloatingActionMenu) findViewById(R.id.fab_main);
        f_btn_add_bank = (FloatingActionButton) findViewById(R.id.f_btn_add_bank);
        f_btn_add_bank_to_bank = (FloatingActionButton) findViewById(R.id.f_btn_add_bank_to_bank);
        f_btn_add_bank_transfer = (FloatingActionButton) findViewById(R.id.f_btn_add_bank_transfer);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        f_btn_add_bank.setOnClickListener(this);
        f_btn_add_bank_to_bank.setOnClickListener(this);
        f_btn_add_bank_transfer.setOnClickListener(this);
        AsyncBankList asyncCallWS = new AsyncBankList();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_btn_add_bank_transfer:
                fab_main.close(true);
                startActivity(new Intent(BankListActivity.this, AddBankAdjustment.class));
                break;
            case R.id.f_btn_add_bank:
                fab_main.close(true);
                startActivity(new Intent(BankListActivity.this, AddBankAccount.class));
                break;
            case R.id.f_btn_add_bank_to_bank:
                fab_main.close(true);
                startActivity(new Intent(BankListActivity.this, AddBankToBank.class));
                break;
        }

    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        if (isBankAdded){
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            AsyncBankList asyncCallWS = new AsyncBankList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isBankAdded = false;
        }
    }

    private class AsyncBankList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            bankLists = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllBankAccount";
            String METHOD_NAME = "ListAllBankAccount";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("CompId", compid);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (!TextUtils.isEmpty(result)) {
                    final JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        bankLists.add(new ItemBankList(jarray.getJSONObject(i).getString("BankName"),
                                jarray.getJSONObject(i).getString("BankAccountId"),
                                jarray.getJSONObject(i).getString("CurrentBalance")));
                        if (bankAccountListAdapter
                                != null) {
                            bankAccountListAdapter
                                    .notifyDataSetChanged();
                        }
                    }

                    bankAccountListAdapter = new BankAccountListAdapter(BankListActivity.this, bankLists);
                    rv_bank_list.setNestedScrollingEnabled(false);
                    rv_bank_list.setHasFixedSize(true);
                    rv_bank_list.setLayoutManager(new LinearLayoutManager(BankListActivity.this));
                    rv_bank_list.setAdapter(bankAccountListAdapter);
                } else {
                    Toast.makeText(BankListActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
