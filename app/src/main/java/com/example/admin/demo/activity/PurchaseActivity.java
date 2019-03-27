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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.PurchasePartyListAdapter;
import com.example.admin.demo.adapter.SalesPartyListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.PartyDetailsPojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText et_search_party;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressBar progressBar;
    private ArrayList<PartyDetailsPojo> partyDetailsPojoArrayList;
    private TextView appNameNavBar;
    private PurchasePartyListAdapter purchasePartyListAdapter;
    private SoapPrimitive resultString;
    public static boolean isPurchase = false;
    public static boolean isPurchaseActivityRefreshed = false;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_purchase);
        et_search_party = findViewById(R.id.et_search_party);
        recyclerView = findViewById(R.id.rv_party_list);
        floatingActionButton = findViewById(R.id.floatingbutton_addparty_purchase);
        progressBar = findViewById(R.id.pb_error_common_recyclerView);
        View view = toolbar.getRootView();
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        Functions.setToolBar(PurchaseActivity.this, toolbar, "Supplier List", true);
        appNameNavBar.setText("Supplier List");
        AsyncCallWSList asyncCallWS = new AsyncCallWSList();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        //session = new SessionManagement(this);


        et_search_party.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!s.toString().isEmpty()){
//
//                }
                filter(s.toString());
            }
        });
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingbutton_addparty_purchase:
                Intent intent = new Intent(PurchaseActivity.this, AddPartyActivity.class);
                isPurchase = true;
                PartyInvoiceActivity.isEditParty = false;
                startActivity(intent);
                break;
        }
    }

    private class AsyncCallWSList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final String routeId = params[0];
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            purchasePartyListAdapter = new PurchasePartyListAdapter(PurchaseActivity.this, getList());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PurchaseActivity.this));
                    recyclerView.setAdapter(purchasePartyListAdapter);

                }
            });
            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    void filter(String text) {
        if (!partyDetailsPojoArrayList.isEmpty()) {
            List<PartyDetailsPojo> temp = new ArrayList();
            for (PartyDetailsPojo partyDetailsPojo : partyDetailsPojoArrayList) {
                String name = partyDetailsPojo.getPartyName().toLowerCase();
                if (name.contains(text)) {
                    temp.add(partyDetailsPojo);
                }
            }
            purchasePartyListAdapter.updateList(temp);
        }
    }

    public ArrayList<PartyDetailsPojo> getList() {
        partyDetailsPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/SearchPartyById";
        String METHOD_NAME = "SearchPartyById";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("routeId", "-1");
            Request.addProperty("tripId", "-1");
            Request.addProperty("CompId", compid);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            transport.debug = true;
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (!TextUtils.isEmpty(responseJSON)) {
                final JSONArray jarray = new JSONArray(responseJSON);
                for (int i = 0; i < jarray.length(); i++) {
                    final String strPartyName = jarray.getJSONObject(i).getString("PartyName");
                    final String strPartyId = jarray.getJSONObject(i).getString("PartyId");
                    final String strAmount = jarray.getJSONObject(i).getString("PartyCurrentBalance");
                    final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                    final String TripId = jarray.getJSONObject(i).getString("TripId");
                    final String isReceivable = jarray.getJSONObject(i).getString("IsReceivable");
                    final String NickName = jarray.getJSONObject(i).getString("NickName");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    final int finalI = i;
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (isReceivable.equals("false")) {
                                PartyDetailsPojo partyDetailsPojo = new PartyDetailsPojo();
                                partyDetailsPojo.setPartyName(strPartyName);
                                partyDetailsPojo.setPartyId(strPartyId);
                                partyDetailsPojo.setPartyCurrentBalance(strAmount);
                                partyDetailsPojo.setRouteId(RouteId);
                                partyDetailsPojo.setTripId(TripId);
                                partyDetailsPojo.setNickName(NickName);
                                partyDetailsPojoArrayList.add(partyDetailsPojo);
                                if (purchasePartyListAdapter
                                        != null) {
                                    purchasePartyListAdapter
                                            .notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }

            } else {
                Toast.makeText(PurchaseActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return partyDetailsPojoArrayList;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isPurchaseActivityRefreshed) {
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            AsyncCallWSList asyncCallWS = new AsyncCallWSList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isPurchaseActivityRefreshed = false;
        } else if (PartyInvoiceActivity.isRefreshed) {
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            AsyncCallWSList asyncCallWS = new AsyncCallWSList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            PartyInvoiceActivity.isRefreshed = false;
        }
    }

}
