package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.CollectionReportAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.CollectionReport;
import com.example.admin.demo.model.DeliveryBy;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.Common;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BulkOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    private Toolbar toolbar_bulk_order;
    private TextView tv_app_name_nav_bar,toDate;
    private SoapPrimitive resultString;
    private List<RoutePojo> routePojoList;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private String[] tripList,routeList,customerName;
    private Spinner sp_user_name,sp_trip_name,sp_route_name;
    private String strTripId,strRouteId,strRouteName,strTripName;
    private String routeId,tripId,tripName,routeName,user_name,user_id;
    private ArrayList<DeliveryBy> deliveryByArrayList;
    private CollectionReportAdapter collectionReportAdapter;
    private RecyclerView rv_collection_report;
    private ArrayList<CollectionReport> collectionReportArrayList;
    private RecyclerView recyclerView;
    private int date_Year_To,date_Month_To,date_Day_To;
    private Calendar calendarTo;
    private SimpleDateFormat sdfToDate;
    private String strDateTimeTo;
    private Button btnGenerate;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_order);
        toolbar_bulk_order = findViewById(R.id.toolbar_bulk_order);
        View view = toolbar_bulk_order.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Bulk Order");
        Functions.setToolBar(BulkOrderActivity.this, toolbar_bulk_order,
                "Add Item", true);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
        sp_user_name = findViewById(R.id.sp_user_name);
        sp_trip_name = findViewById(R.id.sp_trip_name);
        sp_route_name = findViewById(R.id.sp_route_name);
        toDate = findViewById(R.id.et_dateTo);
        btnGenerate = findViewById(R.id.btnGenerate);
        sp_route_name.setOnItemSelectedListener(this);
        sp_user_name.setOnItemSelectedListener(this);
        sp_trip_name.setOnItemSelectedListener(this);

        calendarTo= Calendar.getInstance();
        date_Year_To=calendarTo.get(Calendar.YEAR);
        date_Month_To=calendarTo.get(Calendar.MONTH);
        date_Day_To=calendarTo.get(Calendar.DATE);
        sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
        toDate.setText( sdfToDate.format(calendarTo.getTime()));
        toDate.setOnClickListener(this);
        btnGenerate.setOnClickListener(this);
        //new AsyncCallRoute().execute("1");

        AsyncCallRoute asyncCallWS = new AsyncCallRoute();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sp_route_name:
                routeName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< routePojoList.size(); i++){
                    if (routeName.equals(routePojoList.get(i).getRouteName())){
                        routeId = routePojoList.get(i).getRouteId();
                        new AsyncCallGetTripList().execute(routeId);
                    }
                }
                break;

            case R.id.sp_trip_name:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< tripListByRoutePojos.size(); i++){
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())){
                        tripId = tripListByRoutePojos.get(i).getTripId();
                        new AsyncCallGetCustomerList().execute(routeId,tripId);
                    }
                }
                break;

            case R.id.sp_user_name:
                user_name = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< deliveryByArrayList.size(); i++){
                    if (user_name.equals(deliveryByArrayList.get(i).getUserName())){
                        user_id = deliveryByArrayList.get(i).getUserLoginId();
                    }
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_dateTo:
                getDateTo();
                break;
            case R.id.btnGenerate:
                AsyncCallBulkReport asyncCallWS = new AsyncCallBulkReport();
                asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, routeId,tripId,user_id,toDate.getText().toString());
                //new AsyncCallBulkReport().execute(routeId,tripId,user_id,toDate.getText().toString());
                break;
        }
    }

    private void getDateTo() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                if (month<=9 || dayOfMonth<=9){
                    strDateTimeTo = year + "-" +"0"+ month
                            + "-" +"0"+ dayOfMonth;
                }
                else {
                    strDateTimeTo = year + "-" + month
                            + "-" + dayOfMonth;
                }
//                strDateTimeTo = year + "-" +"0"+ month
//                        + "-" + dayOfMonth;
                toDate.setText(strDateTimeTo);
            }
        }, date_Year_To, date_Month_To, date_Day_To
        );
        datePickerDialog.show();
    }

    private class AsyncCallRoute extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Common.showProgressDialog(BulkOrderActivity.this);
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
                        routePojoList.add(new RoutePojo(str_rout_name,str_rout_id));
                    }
                    routePojoList.add(0,new RoutePojo("All Route","-1"));
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
                    Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BulkOrderActivity.this,
                            android.R.layout.simple_spinner_item, routeList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_route_name.setAdapter(dataAdapter);
                }
            });
        }

    }

    private class AsyncCallGetTripList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Common.showProgressDialog(BulkOrderActivity.this);
        }

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
            catch (Exception e) {
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
                    Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BulkOrderActivity.this,
                            android.R.layout.simple_spinner_item, tripList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);
                }
            });
        }

    }


    private class AsyncCallGetCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Common.showProgressDialog(BulkOrderActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            String tripId = params[1];
            deliveryByArrayList = new ArrayList<>();
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
                        final String PartyName = jarray.getJSONObject(i).getString("PartyName");
                        final String PartyId = jarray.getJSONObject(i).getString("PartyId");
                        deliveryByArrayList.add(new DeliveryBy(PartyName,PartyId));
                    }
                    deliveryByArrayList.add(0,new DeliveryBy("All Party","-1"));
                    customerName = new String[deliveryByArrayList.size()];
                    for (int j = 0; j < deliveryByArrayList.size(); j++) {
                        customerName[j] = deliveryByArrayList.get(j).getUserName();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler1 = new Handler(Looper.getMainLooper());
            uiHandler1.post(new Runnable() {
                @Override
                public void run() {
                    Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BulkOrderActivity.this,
                            android.R.layout.simple_spinner_item, customerName);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_user_name.setAdapter(dataAdapter);
                }
            });
        }

    }

    private class AsyncCallBulkReport extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Common.showProgressDialog(BulkOrderActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            String tripId = params[1];
            String partyId = params[2];
            String date = params[3];
            String SOAP_ACTION = "http://tempuri.org/GenerateNextDayOrder";
            String METHOD_NAME = "GenerateNextDayOrder";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("routeId", routeId);
                Request.addProperty("tripId", tripId);
                Request.addProperty("PartyId", partyId);
                Request.addProperty("invDate", date);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                final String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (responseJSON.equals("0")){
                                Toast.makeText(BulkOrderActivity.this, "Failed to generate order.", Toast.LENGTH_SHORT).show();
                            }
                            else if (responseJSON.equals("1")){
                                Toast.makeText(BulkOrderActivity.this, "Order Has been Generated Successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(BulkOrderActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

            }
            catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            Common.closeProgressDialog();

        }

    }
}
