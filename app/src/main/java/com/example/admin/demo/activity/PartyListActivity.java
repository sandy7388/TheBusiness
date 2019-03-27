package com.example.admin.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.activity.application.BaseApplication;
import com.example.admin.demo.adapter.SalesPartyListAdapter;
import com.example.admin.demo.adapter.PartyListPagerAdapter;
import com.example.admin.demo.R;
import com.example.admin.demo.model.PartyDetailsPojo;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartyListActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    private Toolbar toolbar_partylist;
    private TextView appNameNavBar;
    private SoapPrimitive resultString;
    public static List<RoutePojo> routePojoList;
    private PartyListPagerAdapter partyListPagerAdapter;
    private ArrayAdapter<String> dataAdapter;
    private String strTripId, strRouteId, strRouteName, strTripName;
    private String routeId, tripId, tripName, routeName;
    private Spinner sp_category_1;
    private RecyclerView rv_partymenu1;
    private SalesPartyListAdapter salesPartyListAdapter;
    private FloatingActionButton floatingbutton_addparty;
    private String[] tripList;
    private ArrayList<String> tripList1;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private ProgressBar pb_error_common_recyclerView;
    private EditText et_search_customer;
    private ArrayList<PartyDetailsPojo> partyDetailsPojoArrayList;
    public static boolean isRefreshedParty = false;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar_partylist = (Toolbar) findViewById(R.id.toolbar_partylist);
        View view = toolbar_partylist.getRootView();
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        sp_category_1 = view.findViewById(R.id.sp_category_1);
        floatingbutton_addparty = view.findViewById(R.id.floatingbutton_addparty);
        rv_partymenu1 = view.findViewById(R.id.rv_partymenu1);
        pb_error_common_recyclerView = findViewById(R.id.pb_error_common_recyclerView);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        routePojoList = new ArrayList<>();
        tripList1 = new ArrayList<>();
        et_search_customer = findViewById(R.id.et_search_customer);
        tripListByRoutePojos = new ArrayList<>();
        setSupportActionBar(toolbar_partylist);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appNameNavBar.setText("Party List");
        toolbar_partylist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        partyListPagerAdapter = new PartyListPagerAdapter(getSupportFragmentManager());
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        AsyncCallWSGetMenu asyncCallWS = new AsyncCallWSGetMenu();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        tabLayout.addOnTabSelectedListener(this);
        floatingbutton_addparty.setOnClickListener(this);
        sp_category_1.setOnItemSelectedListener(this);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#00bfbd"));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#00bfbd"));
        wrapTabIndicatorToTitle(tabLayout, 20, 20);
        scrollFabButton();
        //((BaseApplication)this.getApplicationContext()).scanActivity.connectPrinter();
        et_search_customer.addTextChangedListener(new TextWatcher() {
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
                if (!s.toString().isEmpty()) {
                    filter(s.toString());
                }
            }
        });

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        for (int i = 0; i < routePojoList.size(); i++) {
            if (tab.getText().equals(routePojoList.get(i).getRouteName())) {
                routeId = routePojoList.get(i).getRouteId();
                AsyncCallGetTripList asyncCallGetTripList = new AsyncCallGetTripList();
                asyncCallGetTripList.execute(routeId);
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingbutton_addparty:
                PurchaseActivity.isPurchase = false;
                PartyInvoiceActivity.isEditParty = false;
                startActivity(new Intent(PartyListActivity.this, AddPartyActivity.class));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_category_1:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < tripListByRoutePojos.size(); i++) {
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())) {
                        tripId = tripListByRoutePojos.get(i).getTripId();
                        String strRouteId = routeId;
                        AsyncCallWSList asyncCallWS = new AsyncCallWSList();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strRouteId, tripId);
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncCallWSGetMenu extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String SOAP_ACTION = "http://tempuri.org/ListRoute";
            String METHOD_NAME = "ListRoute";
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
                    JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String str_rout_name = jarray.getJSONObject(i).getString("RouteName");
                        final String str_rout_id = jarray.getJSONObject(i).getString("RouteId");
                        RoutePojo routePojo = new RoutePojo();
                        routePojo.setRouteName(str_rout_name);
                        routePojo.setRouteId(str_rout_id);
                        routePojoList.add(routePojo);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            viewPager.setAdapter(partyListPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            partyListPagerAdapter.notifyDataSetChanged();

        }

    }

    private class AsyncCallGetTripList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            tripListByRoutePojos = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListTripByRoute";
            String METHOD_NAME = "ListTripByRoute";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("RouteId", id);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
//                        strTripId = jarray.getJSONObject(i).getString("TripId");
//                        strTripName = jarray.getJSONObject(i).getString("TripName");
//                        strRouteId = jarray.getJSONObject(i).getString("RouteId");
//                        TripListByRoutePojo tripListByRoutePojo = new TripListByRoutePojo();
//                        tripListByRoutePojo.setRoutId(strRouteId);
//                        tripListByRoutePojo.setTripId(strTripId);
//                        tripListByRoutePojo.setTripName(strTripName);
                        tripListByRoutePojos.add(new TripListByRoutePojo(jarray.getJSONObject(i).getString("TripName"),
                                jarray.getJSONObject(i).getString("TripId"),
                                jarray.getJSONObject(i).getString("RouteId")));
                    }
                    tripList = new String[tripListByRoutePojos.size()];
                    for (int j = 0; j < tripListByRoutePojos.size(); j++) {
                        tripList[j] = tripListByRoutePojos.get(j).getTripName();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PartyListActivity.this,
                                android.R.layout.simple_spinner_item, tripList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_category_1.setAdapter(dataAdapter);
                    }
                });
        }

    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    public void scrollFabButton() {
        rv_partymenu1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floatingbutton_addparty.getVisibility() == View.VISIBLE) {
                    floatingbutton_addparty.hide();
                } else if (dy > 0 && floatingbutton_addparty.getVisibility() != View.VISIBLE) {
                    floatingbutton_addparty.show();
                }
            }
        });
    }


    public ArrayList<PartyDetailsPojo> getList(String routeId, String tripId) {
        partyDetailsPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/SearchPartyById";
        String METHOD_NAME = "SearchPartyById";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
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
                    final String NickName = jarray.getJSONObject(i).getString("NickName");
                    final String isReceivable = jarray.getJSONObject(i).getString("IsReceivable");
                    Handler uiHandler = new Handler(Looper.getMainLooper());

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            PartyDetailsPojo partyDetailsPojo = new PartyDetailsPojo();
                            if (isReceivable.equals("true")) {
                                partyDetailsPojo.setPartyName(strPartyName);
                                partyDetailsPojo.setPartyId(strPartyId);
                                partyDetailsPojo.setPartyCurrentBalance(strAmount);
                                partyDetailsPojo.setRouteId(RouteId);
                                partyDetailsPojo.setTripId(TripId);
                                partyDetailsPojo.setNickName(NickName);
                                partyDetailsPojoArrayList.add(partyDetailsPojo);
                                if (salesPartyListAdapter
                                        != null) {
                                    salesPartyListAdapter
                                            .notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }

            } else {
                Toast.makeText(PartyListActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return partyDetailsPojoArrayList;
    }

    private class AsyncCallWSList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final String routeId = params[0];
            final String tripId = params[1];
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb_error_common_recyclerView.setVisibility(View.VISIBLE);
                }
            });
            salesPartyListAdapter = new SalesPartyListAdapter(PartyListActivity.this, getList(routeId, tripId));
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_partymenu1.setNestedScrollingEnabled(false);
                    rv_partymenu1.setHasFixedSize(true);
                    rv_partymenu1.setLayoutManager(new LinearLayoutManager(PartyListActivity.this));
                    rv_partymenu1.setAdapter(salesPartyListAdapter);

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
                    pb_error_common_recyclerView.setVisibility(View.GONE);
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
            salesPartyListAdapter.updateList(temp);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshedParty) {
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            String strRouteId = routeId;
            AsyncCallWSList asyncCallWS = new AsyncCallWSList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strRouteId, tripId);
            isRefreshedParty = false;
        } else if (PartyInvoiceActivity.isRefreshed) {
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            String strRouteId = routeId;
            AsyncCallWSList asyncCallWS = new AsyncCallWSList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strRouteId, tripId);
            PartyInvoiceActivity.isRefreshed = false;
        }
    }

}
