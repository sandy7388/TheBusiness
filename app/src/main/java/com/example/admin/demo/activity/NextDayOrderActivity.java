package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.NextOrderAdapter;
import com.example.admin.demo.adapter.PartyInvoiceAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.PartyInvoicePojo;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.CustomerAdapter;
import com.example.admin.demo.util.InstantAutoComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NextDayOrderActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, CallBack, PartyDetails, View.OnClickListener {
    private Toolbar toolbar_nxt_order;
    private TextView tv_app_name_nav_bar,toDate,tv_submit_nxt_order;
    private String[] ORDERS = {"Open Orders","Closed Orders"};
    private String[] ORDER_TYPE = {"Sale Order", "Purchase Order"};
    private Spinner sp_orders,sp_order_type;
    private InstantAutoComplete autoCompleteTextView;
    private RecyclerView recyclerView;
    private ArrayAdapter arrayAdapterOrders,arrayAdapterTypes;
    private String strOrders,strTypes;
    private SoapPrimitive resultString;
    private ArrayList<CustomerDetailsPojo> customerDetailsPojoArrayList;

    private CustomerAdapter customerAdapter = null;
    private String strPartyId, strPartyName, strPartyBalance,
            strTripId, strRouteId;
    private NextOrderAdapter nextOrderAdapter;
    private ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList;
    public List<RoutePojo> routePojoList;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private String[] tripList,routeList,customerName;
    private Spinner sp_user_name,sp_trip_name,sp_route_name;
    private String routeId,tripId,tripName,routeName,user_name,user_id,strTripName;
    private int date_Year_To,date_Month_To,date_Day_To;
    private Calendar calendarTo;
    private SimpleDateFormat sdfToDate;
    private String strDateTimeTo;
    private CoordinatorLayout cl_next_day_order;
    private SessionManagement session;
    private String compid = "", loginid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day_order);
        toolbar_nxt_order = findViewById(R.id.toolbar_nxt_order);
        View view = toolbar_nxt_order.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Order Details");
        Functions.setToolBar(NextDayOrderActivity.this, toolbar_nxt_order,
                "Add Item", true);
        autoCompleteTextView = findViewById(R.id.autoTextCustomer);
        sp_orders = findViewById(R.id.sp_orders);
        cl_next_day_order = findViewById(R.id.cl_next_day_order);
        toDate = findViewById(R.id.et_dateTo);
        sp_order_type = findViewById(R.id.sp_order_type);
        recyclerView = findViewById(R.id.rv_next_order);
        sp_user_name = findViewById(R.id.sp_user_name);
        sp_trip_name = findViewById(R.id.sp_trip_name);
        sp_route_name = findViewById(R.id.sp_route_name);
        sp_orders.setOnItemSelectedListener(this);
        sp_order_type.setOnItemSelectedListener(this);
        sp_route_name.setOnItemSelectedListener(this);
        sp_user_name.setOnItemSelectedListener(this);
        sp_trip_name.setOnItemSelectedListener(this);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        arrayAdapterOrders = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ORDERS);
        arrayAdapterOrders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_orders.setAdapter(arrayAdapterOrders);
        sp_orders.setSelection(0);
        arrayAdapterTypes = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ORDER_TYPE);
        arrayAdapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_order_type.setAdapter(arrayAdapterTypes);
        AsyncCallWSGetMenu asyncCallWSGetMenu = new AsyncCallWSGetMenu();
        asyncCallWSGetMenu.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");

        calendarTo=Calendar.getInstance();
        date_Year_To=calendarTo.get(Calendar.YEAR);
        date_Month_To=calendarTo.get(Calendar.MONTH);
        calendarTo.add(Calendar.DATE,1);
        date_Day_To=calendarTo.get(Calendar.DATE);
        sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
        toDate.setText( sdfToDate.format(calendarTo.getTime()));
        toDate.setOnClickListener(this);
        if (autoCompleteTextView.getText().toString().equals("")){
            strPartyId = "-1";
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Spinner spinner = (Spinner) adapterView;
        switch (spinner.getId()){
            case R.id.sp_route_name:
                routeName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< routePojoList.size(); i++){
                    if (routeName.equals(routePojoList.get(i).getRouteName())){
                        routeId = routePojoList.get(i).getRouteId();
                        Toast.makeText(this, routeId, Toast.LENGTH_SHORT).show();
                        AsyncCallGetTripList asyncAutoCustomerList = new AsyncCallGetTripList();
                        asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,routeId);

                        AsyncCallNextDayList asyncCallNextDayList = new AsyncCallNextDayList();
                        asyncCallNextDayList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
                    }
                }
                break;

            case R.id.sp_trip_name:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< tripListByRoutePojos.size(); i++){
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())){
                        tripId = tripListByRoutePojos.get(i).getTripId();
                        Toast.makeText(this, tripId, Toast.LENGTH_SHORT).show();
                        AsyncAutoCustomerList asyncCallWS = new AsyncAutoCustomerList();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,routeId,tripId);


                        AsyncCallNextDayList asyncCallNextDayList = new AsyncCallNextDayList();
                        asyncCallNextDayList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
                    }
                }
                break;

            case R.id.sp_user_name:
                user_name = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< customerDetailsPojoArrayList.size(); i++){
                    if (user_name.equals(customerDetailsPojoArrayList.get(i).getPartyName())){
                        user_id = customerDetailsPojoArrayList.get(i).getPartyId();
                        //String strRouteId = routeId;
                    }
                }
                break;
            case R.id.sp_orders:
                strOrders = String.valueOf(spinner.getAdapter().getItem(position));
                AsyncCallNextDayList asyncCallNextDayList = new AsyncCallNextDayList();
                asyncCallNextDayList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
                break;

            case R.id.sp_order_type:
                strTypes = String.valueOf(spinner.getAdapter().getItem(position));
                AsyncCallNextDayList asyncCallNextDayList1 = new AsyncCallNextDayList();
                asyncCallNextDayList1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
                break;


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void AddParty(String str_name, String type, String id) {
        strPartyId = id;
        strPartyBalance = type;
        strPartyName = str_name;
        autoCompleteTextView.setText(strPartyName);
        AsyncCallNextDayList asyncCallNextDayList1 = new AsyncCallNextDayList();
        asyncCallNextDayList1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
        autoCompleteTextView.dismissDropDown();
        Functions.hideKeyboard(NextDayOrderActivity.this);
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {
    }

    @Override
    public void PartyId(String id) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_dateTo:
                getDateTo();
                break;
        }
    }

    private class AsyncCallWSGetMenu extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            routePojoList = new ArrayList<>();
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
                    routePojoList.add(0,new RoutePojo("All Route","-1"));
                    //routePojoList.add(0,new RoutePojo("All","-1"));
                    routeList = new String[routePojoList.size()];
                    for (int j = 0; j < routePojoList.size(); j++) {
                        routeList[j] = routePojoList.get(j).getRouteName();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NextDayOrderActivity.this,
                            android.R.layout.simple_spinner_item, routeList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_route_name.setAdapter(dataAdapter);
                }
            });
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
                        strTripId = jarray.getJSONObject(i).getString("TripId");
                        strTripName = jarray.getJSONObject(i).getString("TripName");
                        strRouteId = jarray.getJSONObject(i).getString("RouteId");
                        //tripList1.add(strTripName);
                        TripListByRoutePojo tripListByRoutePojo = new TripListByRoutePojo();
                        tripListByRoutePojo.setRoutId(strRouteId);
                        tripListByRoutePojo.setTripId(strTripId);
                        tripListByRoutePojo.setTripName(strTripName);
                        tripListByRoutePojos.add(new TripListByRoutePojo(strTripName,strTripId,strRouteId));
                    }
                    tripListByRoutePojos.add(0,new TripListByRoutePojo("All Trip","-1",""));
                    tripList = new String[tripListByRoutePojos.size()];
                    for (int j = 0; j < tripListByRoutePojos.size(); j++) {
                        tripList[j] = tripListByRoutePojos.get(j).getTripName();
                    }

                }

            }
            catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler1 = new Handler(Looper.getMainLooper());
            uiHandler1.post(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(NextDayOrderActivity.this,
                            android.R.layout.simple_spinner_item, tripList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);
                }
            });
        }

    }

    private void getDateTo() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strDateTimeTo =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                        +  mFormat.format(Double.valueOf(dayOfMonth));
//                if (month<=9 || dayOfMonth<=9){
//                    strDateTimeTo = year + "-" +"0"+ month
//                            + "-" +"0"+ dayOfMonth;
//                }
//                else {
//                    strDateTimeTo = year + "-" + month
//                            + "-" + dayOfMonth;
//                }
                toDate.setText(strDateTimeTo);
                AsyncCallNextDayList asyncCallNextDayList = new AsyncCallNextDayList();
                asyncCallNextDayList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        strOrders,strPartyId,routeId,tripId,toDate.getText().toString());
            }
        }, date_Year_To, date_Month_To, date_Day_To
        );
        datePickerDialog.show();
    }

    public ArrayList<PartyInvoicePojo> getNextDayOrders(String invStatus,
                                                        String partyId, String routeId,
                                                        String tripId, String invDate){
        partyInvoicePojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/PartyOpenSalesList";
        String METHOD_NAME = "PartyOpenSalesList";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("invStatus", invStatus);
            Request.addProperty("partyId", partyId);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
            Request.addProperty("invDate", invDate);
            Request.addProperty("CompId", compid);
            if (strTypes.equals("Sale Order")){
                Request.addProperty("isRecieveable", true);
            }
            else {
                Request.addProperty("isRecieveable", false);
            }

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
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final String InvoiceNo = jsonObject.getString("InvoiceNo");
                    final String InvoiceDate = jsonObject.getString("InvoiceDate");
                    final String InvoiceAmount = jsonObject.getString("InvoiceAmount");
                    final String InvoicePaidAmount = jsonObject.getString("InvoicePaidAmount");
                    final String InvoicePendingAmount = jsonObject.getString("InvoicePendingAmount");
                    final String PartyName = jsonObject.getString("PartyName");
                    final String NickName = jsonObject.getString("NickName");
                    final String SalesInvoiceId = jsonObject.getString("SalesInvoiceId");
                    final String InvoiceStatus = jsonObject.getString("InvoiceStatus");
                    final String RouteId = jsonObject.getString("RouteId");
                    final String TripId = jsonObject.getString("TripId");
                    final String PartyCurrentBalance = jsonObject.getString("PartyCurrentBalance");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            PartyInvoicePojo partyInvoicePojo = new PartyInvoicePojo();
                            partyInvoicePojo.setInvoiceNo(InvoiceNo);
                            partyInvoicePojo.setInvoiceDate(InvoiceDate);
                            partyInvoicePojo.setInvoiceAmount(InvoiceAmount);
                            partyInvoicePojo.setInvoicePaidAmount(InvoicePaidAmount);
                            partyInvoicePojo.setInvoicePendingAmount(InvoicePendingAmount);
                            partyInvoicePojo.setPartyName(PartyName);
                            partyInvoicePojo.setNickName(NickName);
                            partyInvoicePojo.setSalesInvoiceId(SalesInvoiceId);
                            partyInvoicePojo.setInvoiceStatus(InvoiceStatus);
                            partyInvoicePojo.setTripId(TripId);
                            partyInvoicePojo.setRouteId(RouteId);
                            partyInvoicePojo.setPartyCurrentBalance(PartyCurrentBalance);
                            partyInvoicePojoArrayList.add(partyInvoicePojo);
                            if (nextOrderAdapter
                                    != null) {
                                nextOrderAdapter
                                        .notifyDataSetChanged();
                            }

                        }
                    });
                }

            } else {
                Toast.makeText(NextDayOrderActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return  partyInvoicePojoArrayList;
    }

    private class AsyncCallNextDayList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //pb_list_party_invoice.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String invStatus = params[0];
            String partyId = params[1];
            String routeId = params[2];
            String tripId = params[3];
            String invDate = params[4];
            nextOrderAdapter = new NextOrderAdapter(getNextDayOrders(invStatus,partyId,
                    routeId,tripId,invDate),NextDayOrderActivity.this );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NextDayOrderActivity.this));
                    recyclerView.setAdapter(nextOrderAdapter);

                }

            });


            return invStatus;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(NextDayOrderActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(NextDayOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    // getting customer list
    private class AsyncAutoCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final String routeId = params[0];
            final String tripId = params[1];
            customerDetailsPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/SearchPartyById";
            String METHOD_NAME = "SearchPartyById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("routeId", routeId);
                Request.addProperty("tripId",tripId);
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
                        final String NickName = jarray.getJSONObject(i).getString("NickName");
                        final String PartyCurrentBalance = jarray.getJSONObject(i).getString("PartyCurrentBalance");
                        final String PartyName = jarray.getJSONObject(i).getString("PartyName");
                        final String PartyId = jarray.getJSONObject(i).getString("PartyId");
                        final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                        final String TripId = jarray.getJSONObject(i).getString("TripId");
                        final String IsReceivable = jarray.getJSONObject(i).getString("IsReceivable");
                        CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
                        customerDetailsPojo.setNickName(NickName);
                        customerDetailsPojo.setPartyCurrentBalance(PartyCurrentBalance);
                        customerDetailsPojo.setPartyName(PartyName);
                        customerDetailsPojo.setPartyId(PartyId);
                        customerDetailsPojo.setTripId(TripId);
                        customerDetailsPojo.setRouteId(RouteId);
                        customerDetailsPojoArrayList.add(customerDetailsPojo);
                    }
                    customerDetailsPojoArrayList.add(0,new CustomerDetailsPojo("-1","","All Party","-1","-1","0"));
                    customerName = new String[customerDetailsPojoArrayList.size()];
                    for (int j = 0; j < customerDetailsPojoArrayList.size(); j++) {
                        customerName[j] = customerDetailsPojoArrayList.get(j).getPartyName();
                    }

                }
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    autoCompleteTextView.setThreshold(0);
                    customerAdapter = new CustomerAdapter(NextDayOrderActivity.this, customerDetailsPojoArrayList);
                    autoCompleteTextView.setAdapter(customerAdapter);
                    autoCompleteTextView.setSelection(0);
                    autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (customerName.length > 0) {
                                // show all suggestions
                                if (!autoCompleteTextView.getText().toString().equals(""))
                                    customerAdapter.getFilter().filter(null);
                                autoCompleteTextView.showDropDown();
                            }
                            return false;
                        }
                    });
                }
            });

        }
    }

    public void resetRecyclerview(Context context,int i)
    {
        partyInvoicePojoArrayList.remove(i);
        recyclerView.removeViewAt(i);
        nextOrderAdapter.notifyItemRemoved(i);
        nextOrderAdapter.notifyItemRangeChanged(i, partyInvoicePojoArrayList.size());


    }
}
