package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.CollectionReportAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.CollectionReport;
import com.example.admin.demo.model.DeliveryBy;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.model.VehicleReportPojo;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.Common;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CollectionReportActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar_collection_report;
    private TextView tv_app_name_nav_bar,et_date,et_date_to,tv_vehicle_total_qty;
    private Calendar calendarFrom,calendar_cheque;
    private SimpleDateFormat sdfFromDate;
    private String strDateTimeFrom,strDateTimeTo;
    private int date_Year_From,date_Month_From,date_Day_From,
            date_Year_cheque, date_Month_cheque, date_Day_cheque,hour_From,minute_From,hour_To,minute_To;
    private SoapPrimitive resultString;
    public List<RoutePojo> routePojoList;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private String[] tripList,routeList,customerName;
    private Spinner sp_user_name,sp_trip_name,sp_route_name;
    private String strTripId,strRouteId,strRouteName,strTripName,strCompanyName;
    private String routeId,tripId,tripName,routeName,user_name,user_id,compid;
    private ArrayList<DeliveryBy> deliveryByArrayList;
    private CollectionReportAdapter collectionReportAdapter;
    private RecyclerView rv_collection_report;
    private ArrayList<CollectionReport> collectionReportArrayList;
    private Button btn_submit;
    private ImageView iv_printer;
    private BluetoothAdapter mBluetoothAdapter;
    private DecimalFormat df;
    private SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_report);
        toolbar_collection_report = findViewById(R.id.toolbar_vehicle_report);
        View view = toolbar_collection_report.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Payment Collection Report");
        Functions.setToolBar(CollectionReportActivity.this, toolbar_collection_report,
                "Add Item", true);
        et_date = findViewById(R.id.et_date_from);
        et_date_to = findViewById(R.id.et_date_to);
        iv_printer = findViewById(R.id.iv_printer);
        tv_vehicle_total_qty = findViewById(R.id.tv_vehicle_total_qty);
        sp_user_name = findViewById(R.id.sp_user_name);
        sp_trip_name = findViewById(R.id.sp_trip_name);
        sp_route_name = findViewById(R.id.sp_route_name);
        rv_collection_report = findViewById(R.id.rv_collection_report);
        calendarFrom= Calendar.getInstance();
        date_Year_From=calendarFrom.get(Calendar.YEAR);
        date_Month_From=calendarFrom.get(Calendar.MONTH);
        date_Day_From = calendarFrom.get(Calendar.DAY_OF_MONTH);
        hour_From = calendarFrom.get(Calendar.HOUR_OF_DAY);
        minute_From = calendarFrom.get(Calendar.MINUTE);
        sdfFromDate = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        et_date.setText(sdfFromDate.format(calendarFrom.getTime()));
        btn_submit = findViewById(R.id.btn_submit);
        calendar_cheque = Calendar.getInstance();
        date_Year_cheque = calendar_cheque.get(Calendar.YEAR);
        date_Month_cheque = calendar_cheque.get(Calendar.MONTH);
        date_Day_cheque = calendar_cheque.get(Calendar.DATE);
        hour_To = calendar_cheque.get(Calendar.HOUR_OF_DAY);
        minute_To = calendar_cheque.get(Calendar.MINUTE);
        df = new DecimalFormat("#0.00");
        et_date_to.setText(sdfFromDate.format(calendar_cheque.getTime()));
        et_date.setOnClickListener(this);
        et_date_to.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_printer.setOnClickListener(this);
        sp_route_name.setOnItemSelectedListener(this);
        sp_user_name.setOnItemSelectedListener(this);
        sp_trip_name.setOnItemSelectedListener(this);
        new AsyncCallGetCustomerList().execute("1");
        new AsyncCallRoute().execute("1");
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strCompanyName = user.get(SessionManagement.KEY_COMPANY_NAME);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }

    }

    private void getDateFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strDateTimeFrom = mFormat.format(Double.valueOf(dayOfMonth)) + "/" +mFormat.format(Double.valueOf(month))
                        + "/" + year;
                timePickerFrom();
            }
        }, date_Year_From, date_Month_From, date_Day_From
        );
        datePickerDialog.show();
    }
    private void timePickerFrom() {

        final Calendar c = Calendar.getInstance();
        hour_From = c.get(Calendar.HOUR_OF_DAY);
        minute_From = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        hour_From = hourOfDay;
                        minute_From = minute;
                        //fromDate.setText(strDateTimeFrom + " " + hourOfDay + ":" + minute);
                        et_date.setText(strDateTimeFrom + " " + mFormat.format(hourOfDay) + ":" + mFormat.format(minute));
                        //et_date.setText(strDateTimeFrom);
                    }
                }, hour_From, minute_From, false);
        timePickerDialog.show();
    }

    private void timePickerTo() {

        final Calendar c = Calendar.getInstance();
        hour_To = c.get(Calendar.HOUR_OF_DAY);
        minute_To = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        hour_To = hourOfDay;
                        minute_To = minute;
                        //et_date_to.setText(strDateTimeTo);
                        et_date_to.setText(strDateTimeTo + " " + mFormat.format(hourOfDay) + ":" + mFormat.format(minute));
                    }
                }, hour_To, minute_To, false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_date_from:
                getDateFrom();
                break;
            case R.id.et_date_to:
                getDateTo();
                break;
            case R.id.btn_submit:
                generateCollectionReport();
                break;
            case R.id.iv_printer:
                printData(routeName,tripName,et_date.getText().toString(),et_date_to.getText().toString(),user_name,collectionReportArrayList);
                break;
        }
    }

    private void generateCollectionReport() {
        Date date = new Date();
        Date date1 = new Date();
        String fromDate = et_date.getText().toString();
        String toDate = et_date_to.getText().toString();
        try {
            date = sdfFromDate.parse(fromDate);
            date1 = sdfFromDate.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formatedFromDate = simpleDateFormat.format(date);
        String formatedToDate = simpleDateFormat.format(date1);

        new AsyncCallCollectionReport().execute(user_id,routeId,tripId,formatedFromDate,formatedToDate);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sp_trip_name:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< tripListByRoutePojos.size(); i++){
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())){
                        tripId = tripListByRoutePojos.get(i).getTripId();
                    }
                }
                break;
            case R.id.sp_route_name:
                routeName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< routePojoList.size(); i++){
                    if (routeName.equals(routePojoList.get(i).getRouteName())){
                        routeId = routePojoList.get(i).getRouteId();
                        AsyncCallGetTripList asyncAutoCustomerList = new AsyncCallGetTripList();
                        asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,routeId);
                    }
                }
                break;
            case R.id.sp_user_name:
                user_name = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< deliveryByArrayList.size(); i++){
                    if (user_name.equals(deliveryByArrayList.get(i).getUserName())){
                        user_id = deliveryByArrayList.get(i).getUserLoginId();
                        //String strRouteId = routeId;
                    }
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class AsyncCallRoute extends AsyncTask<String, Void, String> {

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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CollectionReportActivity.this,
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CollectionReportActivity.this,
                            android.R.layout.simple_spinner_item, tripList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);
                }
            });
        }

    }
    private class AsyncCallGetCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            deliveryByArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListDeliveryBy";
            String METHOD_NAME = "ListDeliveryBy";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
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
                        final String UserName = jarray.getJSONObject(i).getString("UserName");
                        final String UserLoginId = jarray.getJSONObject(i).getString("UserLoginId");
                        deliveryByArrayList.add(new DeliveryBy(UserName,UserLoginId));
                    }
                    deliveryByArrayList.add(0,new DeliveryBy("All User","-1"));
                    customerName = new String[deliveryByArrayList.size()];
                    for (int j = 0; j < deliveryByArrayList.size(); j++) {
                        customerName[j] = deliveryByArrayList.get(j).getUserName();
                    }

                }

            } catch (Exception e) {
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CollectionReportActivity.this,
                            android.R.layout.simple_spinner_item, customerName);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_user_name.setAdapter(dataAdapter);
                }
            });
        }

    }

    private void getDateTo() {
        DatePickerDialog chequeDateDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strDateTimeTo = mFormat.format(Double.valueOf(dayOfMonth)) + "/" +mFormat.format(Double.valueOf(month))
                        + "/" + year;

                timePickerTo();
            }
        }, date_Year_cheque, date_Month_cheque, date_Day_cheque
        );
        chequeDateDialog.show();
    }

    private class AsyncCallCollectionReport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String user_id = params[0];
            String routeId = params[1];
            String tripId = params[2];
            String fromDate = params[3];
            String toDate = params[4];
            collectionReportAdapter = new CollectionReportAdapter(getVehicleReport(user_id,
                    routeId,tripId,fromDate,toDate),CollectionReportActivity.this );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_collection_report.setHasFixedSize(true);
                    rv_collection_report.setLayoutManager(new LinearLayoutManager(CollectionReportActivity.this));
                    rv_collection_report.setAdapter(collectionReportAdapter);

                }

            });


            return user_id;
        }

        @Override
        protected void onPostExecute(String result) {
            double total = 0;
            for (int j = 0; j < collectionReportArrayList.size(); j++) {
                total = total + Double.parseDouble(collectionReportArrayList.get(j).getStrAmount());
                tv_vehicle_total_qty.setText(total + "");
            }
            if (result.equals("success")) {
                Toast.makeText(CollectionReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(CollectionReportActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public ArrayList<CollectionReport> getVehicleReport(String catId, String routeId,
                                                        String tripId, String fromDate,String toDate){
        collectionReportArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/PaymentCollectionReports";
        String METHOD_NAME = "PaymentCollectionReports";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("loginId", catId);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
            Request.addProperty("fromdate", fromDate);
            Request.addProperty("todate", toDate);
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
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final String strName = jsonObject.getString("PartyName");
                    final String strAmount = jsonObject.getString("PaidAmount");
                    final String strDate = jsonObject.getString("PaymentDate");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            collectionReportArrayList.add(new CollectionReport(strName,strDate,strAmount));
                            if (collectionReportAdapter
                                    != null) {
                                collectionReportAdapter
                                        .notifyDataSetChanged();
                            }

                        }
                    });
                }

            } else {
                Toast.makeText(CollectionReportActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return  collectionReportArrayList;
    }

    public void printData(final String routeName, final String tripName,final String date,final String date1,
                          final String category, final ArrayList<CollectionReport> vehicleReportPojoArrayList){
        Thread t = new Thread() {
            public void run() {
                try {
                    ScanActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> mPairedDevices = ScanActivity.mBluetoothAdapter.getBondedDevices();
                    if (mPairedDevices.size() > 0) {
                        OutputStream os = ScanActivity.mBluetoothSocket
                                .getOutputStream();
                        String BILL = "";

                        BILL = "         Company Name  : "+strCompanyName +   "\n"
                                +"          User Name  : "+category +   "\n"
                                + "          Trip Name. : "+ tripName+     "\n " +
                                "          Invoice Date: "+ date     +" \n" +
                                "           Route Name:"+ routeName  +"\n";
                        BILL = BILL
                                + "-----------------------------------------------\n";


                        BILL = BILL + String.format("%1$-18s %2$10s %3$10s", "Item", "Amt", "Date");
                        BILL = BILL + "\n";
                        BILL = BILL
                                + "-----------------------------------------------";

                        for (int i=0;i<vehicleReportPojoArrayList.size();i++){
                            String name,date;
                            double total=0,amount=0,price=0;
                            name = vehicleReportPojoArrayList.get(i).getStrName();
                            amount = Double.parseDouble(vehicleReportPojoArrayList.get(i).getStrAmount());
                            date = (vehicleReportPojoArrayList.get(i).getStrDate());
                            BILL = BILL + "\n " + String.format("%1$-18s %2$10s %3$10s", name, df.format(amount), date);
                        }

                        BILL = BILL
                                + "\n-----------------------------------------------";
                        BILL = BILL + "\n\n ";

                        double price = 0,qty = 0,tax = 0,total = 0;
                        for (int j = 0; j<vehicleReportPojoArrayList.size();j++){
                            //price = price+(invoiceItemDetailsArrayList.get(j).getTotal());
                            price = price+Double.parseDouble(vehicleReportPojoArrayList.get(j).getStrAmount());
                            //tax = tax+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                            //total = total + (price *qty) + tax;

                        }

                        BILL = BILL + "                   Total Amount:" + "      " + df.format(price) + "\n";


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
                        Toast.makeText(CollectionReportActivity.this, "No device Connected", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.e("ScanActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }
}
