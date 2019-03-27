package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.VehicleReportAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.model.VehicleReportPojo;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.Common;
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
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;

public class SalesReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, CallBack, CompoundButton.OnCheckedChangeListener {

    private SoapPrimitive resultString;
    private ArrayList<RoutePojo> routePojoList;
    private Spinner sp_route_name, sp_trip_name, sp_category_name;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private ArrayList<ProductCategory> productCategoryArrayList;
    private String strTripId, strRouteId, strTripName, strDateTimeFrom, strDateTimeTo,
            routeId, tripId, tripName, routeName, strCategoryId, strCategoryName,strCompanyName,
            strPartyId,strPartyBalance,strPartyName;
    private TextView appNameNavBar, et_date, et_date_to, tv_vehicle_total_qty;
    private Calendar calendarFrom, calendar_cheque;
    private SimpleDateFormat sdfFromDate;
    private int date_Year_From, date_Month_From, date_Day_From, date_Year_cheque,
            date_Month_cheque, date_Day_cheque;
    private DecimalFormat df;
    private Button btn_submit;
    private ArrayList<CustomerDetailsPojo> customerDetailsPojoArrayList;
    private InstantAutoComplete autoCompleteTextView;
    private Switch aSwitch;
    private boolean strCashStatus;
    private RecyclerView recyclerView;
    private ArrayList<VehicleReportPojo> vehicleReportPojoArrayList;
    private VehicleReportAdapter vehicleReportAdapter;
    private ImageView iv_printer;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        sp_route_name = findViewById(R.id.sp_route_name);
        sp_trip_name = findViewById(R.id.sp_trip_name);
        sp_category_name = findViewById(R.id.sp_category_name);
        et_date = findViewById(R.id.et_date_from);
        et_date_to = findViewById(R.id.et_date_to);
        recyclerView = findViewById(R.id.rv_sales_report);
        Toolbar toolbar_sales = findViewById(R.id.toolbar_sales);
        autoCompleteTextView = findViewById(R.id.autoTextCustomer);
        tv_vehicle_total_qty = findViewById(R.id.tv_vehicle_total_qty);
        iv_printer = findViewById(R.id.iv_printer);
        appNameNavBar = toolbar_sales.findViewById(R.id.app_name_nav_bar);
        aSwitch = toolbar_sales.findViewById(R.id.toolbar_switch);
        Functions.setToolBar(SalesReportActivity.this, toolbar_sales, "Sales Report", true);
        appNameNavBar.setText("Sales Report");
        calendarFrom = Calendar.getInstance();
        date_Year_From = calendarFrom.get(Calendar.YEAR);
        date_Month_From = calendarFrom.get(Calendar.MONTH);
        date_Day_From = calendarFrom.get(Calendar.DAY_OF_MONTH);
        sdfFromDate = new SimpleDateFormat("yyyy-MM-dd");
        et_date.setText(sdfFromDate.format(calendarFrom.getTime()));
        btn_submit = findViewById(R.id.btn_submit);
        calendar_cheque = Calendar.getInstance();
        date_Year_cheque = calendar_cheque.get(Calendar.YEAR);
        date_Month_cheque = calendar_cheque.get(Calendar.MONTH);
        date_Day_cheque = calendar_cheque.get(Calendar.DATE);
        df = new DecimalFormat("#0.00");
        et_date_to.setText(sdfFromDate.format(calendarFrom.getTime()));
        sp_route_name.setOnItemSelectedListener(this);
        sp_trip_name.setOnItemSelectedListener(this);
        sp_category_name.setOnItemSelectedListener(this);
        aSwitch.setOnCheckedChangeListener(this);
        et_date.setOnClickListener(this);
        et_date_to.setOnClickListener(this);
        new AsyncCallRouteList().execute("1");
        new AsyncAutoCategoryList().execute("1");
        if (autoCompleteTextView.getText().toString().equals("")) {
            strPartyId = "-1";
        }
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strCompanyName = user.get(SessionManagement.KEY_COMPANY_NAME);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_route_name:
                routeName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < routePojoList.size(); i++) {
                    if (routeName.equals(routePojoList.get(i).getRouteName())) {
                        routeId = routePojoList.get(i).getRouteId();
                        new AsyncCallGetTripList().execute(routeId);
                        new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                                strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());
                    }
                }
                break;

            case R.id.sp_trip_name:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < tripListByRoutePojos.size(); i++) {
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())) {
                        tripId = tripListByRoutePojos.get(i).getTripId();
                        new AsyncAutoCustomerList().execute(routeId,tripId);

                        new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                                strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());

                    }
                }
                break;

            case R.id.sp_category_name:
                strCategoryName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < productCategoryArrayList.size(); i++) {
                    if (strCategoryName.equals(productCategoryArrayList.get(i).getServiceName())) {
                        strCategoryId = productCategoryArrayList.get(i).getServiceId();

                        new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                                strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_date_from:
                getDateFrom();
                break;
            case R.id.et_date_to:
                getDateTo();
                break;
            case R.id.iv_printer:
                printData();
                break;
        }

    }

    @Override
    public void AddParty(String str_name, String type, String id) {
        strPartyId = id;
        strPartyBalance = type;
        strPartyName = str_name;
        autoCompleteTextView.setText(strPartyName);
        autoCompleteTextView.dismissDropDown();
        Functions.hideKeyboard(SalesReportActivity.this);

        new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == aSwitch) {
            if (isChecked) {
                aSwitch.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.switch_track_checked_false_color), PorterDuff.Mode.SRC_IN);
                //autoCompleteTextView.setText("Cash Sale");
                strCashStatus = true;
                new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                        strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());
            } else {

                aSwitch.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.switch_track_checked_false_color), PorterDuff.Mode.SRC_IN);
                strCashStatus = false;
                new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                        strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());

            }
        }
    }

    private class AsyncCallRouteList extends AsyncTask<String, Void, String> {

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

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String str_rout_name = jarray.getJSONObject(i).getString("RouteName");
                        final String str_rout_id = jarray.getJSONObject(i).getString("RouteId");
                        RoutePojo routePojo = new RoutePojo();
                        routePojo.setRouteName(str_rout_name);
                        routePojo.setRouteId(str_rout_id);
                        routePojoList.add(routePojo);
                    }
                    routePojoList.add(0, new RoutePojo("All Route", "-1"));
                    String[] routeList = new String[routePojoList.size()];
                    for (int j = 0; j < routePojoList.size(); j++) {
                        routeList[j] = routePojoList.get(j).getRouteName();
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SalesReportActivity.this,
                            android.R.layout.simple_spinner_item, routeList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_route_name.setAdapter(dataAdapter);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

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


            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!TextUtils.isEmpty(result)) {

                try {
                    JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        String strTripId = jarray.getJSONObject(i).getString("TripId");
                        String strTripName = jarray.getJSONObject(i).getString("TripName");
                        String strRouteId = jarray.getJSONObject(i).getString("RouteId");

                        tripListByRoutePojos.add(new TripListByRoutePojo(strTripName, strTripId, strRouteId));
                    }
                    tripListByRoutePojos.add(new TripListByRoutePojo("All Trip", "-1", "-1"));
                    String[] tripList = new String[tripListByRoutePojos.size()];
                    for (int j = 0; j < tripListByRoutePojos.size(); j++) {
                        tripList[j] = tripListByRoutePojos.get(j).getTripName();
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SalesReportActivity.this,
                            android.R.layout.simple_spinner_item, tripList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private class AsyncAutoCategoryList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final String id = params[0];
            productCategoryArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllCategory";
            String METHOD_NAME = "ListAllCategory";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Request.addProperty("CompId", compid);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!TextUtils.isEmpty(result)) {
                try {
                    final JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String TripName = jarray.getJSONObject(i).getString("CategoryName");
                        final String TripId = jarray.getJSONObject(i).getString("CategoryId");
                        productCategoryArrayList.add(new ProductCategory(TripName, TripId));
                    }
                    productCategoryArrayList.add(0, new ProductCategory("All Category", "-1"));
                    String[] categoryName = new String[productCategoryArrayList.size()];
                    for (int j = 0; j < productCategoryArrayList.size(); j++) {
                        categoryName[j] = productCategoryArrayList.get(j).getServiceName();
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SalesReportActivity.this,
                            android.R.layout.simple_spinner_item, categoryName);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_category_name.setAdapter(dataAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }


    private void getDateFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strDateTimeFrom =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                        +  mFormat.format(Double.valueOf(dayOfMonth));
                et_date.setText(strDateTimeFrom);
                new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                        strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());
            }
        }, date_Year_From, date_Month_From, date_Day_From
        );
        datePickerDialog.show();
    }

    private void getDateTo() {
        DatePickerDialog chequeDateDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat = new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strDateTimeTo =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                        +  mFormat.format(Double.valueOf(dayOfMonth));

                new AsyncCallVehicleReport().execute(strPartyId,routeId,tripId,
                        strCategoryId,et_date.getText().toString(),et_date_to.getText().toString());

                et_date_to.setText(strDateTimeTo);
            }
        }, date_Year_cheque, date_Month_cheque, date_Day_cheque
        );
        chequeDateDialog.show();
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
                Request.addProperty("tripId", tripId);
                Request.addProperty("CompId", compid);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
            } catch (Exception e) {
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
                        customerDetailsPojoArrayList.add(new CustomerDetailsPojo(jarray.getJSONObject(i).getString("PartyId"),
                                jarray.getJSONObject(i).getString("NickName"),jarray.getJSONObject(i).getString("PartyName"),
                                jarray.getJSONObject(i).getString("RouteId"), jarray.getJSONObject(i).getString("TripId"),
                                jarray.getJSONObject(i).getString("PartyCurrentBalance")));
                    }
                    customerDetailsPojoArrayList.add(0, new CustomerDetailsPojo("-1", "", "All Party", "-1", "-1", "0"));
                    final String[] customerName = new String[customerDetailsPojoArrayList.size()];
                    for (int j = 0; j < customerDetailsPojoArrayList.size(); j++) {
                        customerName[j] = customerDetailsPojoArrayList.get(j).getPartyName();
                    }

                    autoCompleteTextView.setThreshold(0);
                    final CustomerAdapter customerAdapter = new CustomerAdapter(SalesReportActivity.this, customerDetailsPojoArrayList);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class AsyncCallVehicleReport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String partyId = params[0];
            String routeId = params[1];
            String tripId = params[2];
            String catId = params[3];
            String fromDate = params[4];
            String toDate = params[5];
            vehicleReportPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/SalesReports";
            String METHOD_NAME = "SalesReports";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("catId", catId);
                Request.addProperty("routeId", routeId);
                Request.addProperty("tripId", tripId);
                Request.addProperty("partyId", partyId);
                Request.addProperty("isCashSale", strCashStatus);
                Request.addProperty("fromDate", fromDate);
                Request.addProperty("toDate", toDate);
                Request.addProperty("CompId", compid);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!TextUtils.isEmpty(result)) {
                try {
                    final JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jsonObject = jarray.getJSONObject(i);
                        vehicleReportPojoArrayList.add(new VehicleReportPojo(jsonObject.getString("ProductName"),
                                jsonObject.getString("TotalQty"),jsonObject.getString("UnitName")));
                        if (vehicleReportAdapter
                                != null) {
                            vehicleReportAdapter
                                    .notifyDataSetChanged();
                        }
                    }

                    vehicleReportAdapter = new VehicleReportAdapter(vehicleReportPojoArrayList,SalesReportActivity.this );
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SalesReportActivity.this));
                    recyclerView.setAdapter(vehicleReportAdapter);

                    double total = 0;
                    for (int j = 0; j < vehicleReportPojoArrayList.size(); j++) {
                        total = total + Double.parseDouble(vehicleReportPojoArrayList.get(j).getStrItemQty());
                        tv_vehicle_total_qty.setText(total + "");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(SalesReportActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void printData(){
        Thread t = new Thread() {
            public void run() {
                try {
                    ScanActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> mPairedDevices = ScanActivity.mBluetoothAdapter.getBondedDevices();
                    if (mPairedDevices.size() > 0) {
                        OutputStream os = ScanActivity.mBluetoothSocket.getOutputStream();
                        String BILL = "";

                        BILL = "         Company Name  : "+strCompanyName +   "\n"
                                +"        Party Name    :"+strPartyName +  "\n"
                                +"           Route Name :  "+routeName +   "\n"
                                +"           Trip Name. : "+ tripName+     "\n " +
                                "          Date : "+ et_date.getText().toString() + "-" + et_date_to.getText().toString()+" \n" +
                                "           Category     :"+ strCategoryName  +"\n";
                        BILL = BILL
                                + "-----------------------------------------------\n";


                        BILL = BILL + String.format("%1$-21s %2$10s %3$10s", "Item", "Qty", "Unit");
                        BILL = BILL + "\n";
                        BILL = BILL
                                + "-----------------------------------------------";

                        for (int i=0;i<vehicleReportPojoArrayList.size();i++){
                            String name,unit;
                            double total=0,qty=0,price=0;
                            name = vehicleReportPojoArrayList.get(i).getStrItemName();
                            qty = Double.parseDouble(vehicleReportPojoArrayList.get(i).getStrItemQty());
                            unit = (vehicleReportPojoArrayList.get(i).getStrItemUnit());
                            BILL = BILL + "\n " + String.format("%1$-21s %2$10s %3$10s", name, df.format(qty), unit);
                        }

                        BILL = BILL
                                + "\n-----------------------------------------------";
                        BILL = BILL + "\n\n ";

                        double price = 0,qty = 0,tax = 0,total = 0;
                        for (int j = 0; j<vehicleReportPojoArrayList.size();j++){
                            //price = price+(invoiceItemDetailsArrayList.get(j).getTotal());
                            qty = qty+Double.parseDouble(vehicleReportPojoArrayList.get(j).getStrItemQty());
                            //tax = tax+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                            //total = total + (price *qty) + tax;

                        }

                        BILL = BILL + "                   Total Qty:" + "      " + df.format(qty) + "\n";


                        BILL = BILL
                                + "-----------------------------------------------\n";
                        BILL = BILL + "\n\n ";
                        os.write(BILL.getBytes());
                        //This is printer specific code you can comment ==== > Start

                        // Setting height
                        int gs = 29;
                        os.write(Common.intToByteArray(gs));
                        int h = 104;
                        os.write(Common.intToByteArray(h));
                        int n = 162;
                        os.write(Common.intToByteArray(n));

                        // Setting Width
                        int gs_width = 29;
                        os.write(Common.intToByteArray(gs_width));
                        int w = 119;
                        os.write(Common.intToByteArray(w));
                        int n_width = 2;
                        os.write(Common.intToByteArray(n_width));
                    } else {
                        Toast.makeText(SalesReportActivity.this, "No device Connected", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.e("ScanActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

}
