package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.VehicleReportAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.RoutePojo;
import com.example.admin.demo.model.TripListByRoutePojo;
import com.example.admin.demo.model.VehicleReportPojo;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.printer.UnicodeFormatter;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.CategoryAdapter;
import com.example.admin.demo.util.Common;
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
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VehicleLoadActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener, CallBack {

    private Toolbar toolbar_vehicle_report;
    private TextView tv_app_name_nav_bar,toDate,tv_vehicle_total_qty;
    private InstantAutoComplete autoCompleteTextView;
    private RecyclerView recyclerView;
    private int date_Year_To,date_Month_To,date_Day_To;
    private Calendar calendarTo;
    private SimpleDateFormat sdfToDate;
    private String strDateTimeTo;
    private SoapPrimitive resultString;
    public List<RoutePojo> routePojoList;
    private ArrayList<TripListByRoutePojo> tripListByRoutePojos;
    private String[] tripList,routeList, categoryName;
    private Spinner sp_trip_name,sp_route_name,sp_category_name;
    private String strTripId,strRouteId,strTripName,strCompanyName;
    private String routeId,tripId,tripName,routeName;
    private CategoryAdapter categoryAdapter = null;
    private ArrayList<ProductCategory> productCategoryArrayList;
    private ArrayList<VehicleReportPojo> vehicleReportPojoArrayList;
    private VehicleReportAdapter vehicleReportAdapter;
    private String strCategoryId,strCategoryName,compid;
    private ImageView iv_printer;
    private DecimalFormat df;
    private SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_load);
        toolbar_vehicle_report = findViewById(R.id.toolbar_vehicle_report);
        View view = toolbar_vehicle_report.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Vehicle Load Report");
        Functions.setToolBar(VehicleLoadActivity.this, toolbar_vehicle_report,
                "Add Item", true);
        sp_trip_name = findViewById(R.id.sp_trip_name);
        sp_route_name = findViewById(R.id.sp_route_name);
        sp_category_name = findViewById(R.id.sp_category_name);
        tv_vehicle_total_qty = findViewById(R.id.tv_vehicle_total_qty);
        recyclerView = findViewById(R.id.recyclerView);
        iv_printer = findViewById(R.id.iv_printer);
        autoCompleteTextView = findViewById(R.id.autoTextCustomer);
        toDate = findViewById(R.id.et_dateTo);
        calendarTo=Calendar.getInstance();
        date_Year_To=calendarTo.get(Calendar.YEAR);
        date_Month_To=calendarTo.get(Calendar.MONTH);
        date_Day_To=calendarTo.get(Calendar.DATE);
        sdfToDate = new SimpleDateFormat("yyyy-MM-dd");
        toDate.setText( sdfToDate.format(calendarTo.getTime()));
        toDate.setOnClickListener(this);
        if (autoCompleteTextView.getText().toString().equals("")){
            strCategoryId = "-1";
        }
        df = new DecimalFormat("#0.00");
        toDate.setOnClickListener(this);
        sp_route_name.setOnItemSelectedListener(this);
        sp_trip_name.setOnItemSelectedListener(this);
        sp_category_name.setOnItemSelectedListener(this);
        iv_printer.setOnClickListener(this);
        AsyncCallRouteList asyncCallRouteList = new AsyncCallRouteList();
        asyncCallRouteList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
        AsyncAutoCategoryList asyncCallWS = new AsyncAutoCategoryList();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strCompanyName = user.get(SessionManagement.KEY_COMPANY_NAME);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_dateTo:
                getDateTo();
                break;
            case R.id.iv_printer:
                printData(routeName,tripName,toDate.getText().toString(),strCategoryName,vehicleReportPojoArrayList);
                break;
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
                toDate.setText(strDateTimeTo);
                AsyncCallVehicleReport asyncCallVehicleReport = new AsyncCallVehicleReport();
                asyncCallVehicleReport.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        strCategoryId,routeId,tripId,toDate.getText().toString());
            }
        }, date_Year_To, date_Month_To, date_Day_To
        );
        datePickerDialog.show();
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
                        AsyncCallGetTripList asyncAutoCustomerList = new AsyncCallGetTripList();
                        asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,routeId);

                        AsyncCallVehicleReport asyncCallVehicleReport = new AsyncCallVehicleReport();
                        asyncCallVehicleReport.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                strCategoryId,routeId,tripId,toDate.getText().toString());
                    }
                }
                break;

            case R.id.sp_trip_name:
                tripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< tripListByRoutePojos.size(); i++){
                    if (tripName.equals(tripListByRoutePojos.get(i).getTripName())){
                        tripId = tripListByRoutePojos.get(i).getTripId();

                        AsyncCallVehicleReport asyncCallVehicleReport = new AsyncCallVehicleReport();
                        asyncCallVehicleReport.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                strCategoryId,routeId,tripId,toDate.getText().toString());

                    }
                }
                break;

            case R.id.sp_category_name:
                strCategoryName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i< productCategoryArrayList.size(); i++){
                    if (strCategoryName.equals(productCategoryArrayList.get(i).getServiceName())){
                        strCategoryId = productCategoryArrayList.get(i).getServiceId();
                        AsyncCallVehicleReport asyncCallVehicleReport = new AsyncCallVehicleReport();
                        asyncCallVehicleReport.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                strCategoryId,routeId,tripId,toDate.getText().toString());

                    }
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void AddParty(String str_name, String type, String id) {

    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {
        strCategoryName = strPartyId;
        strCategoryId = strPartyName;
        autoCompleteTextView.setText(strCategoryName);
        autoCompleteTextView.dismissDropDown();

        AsyncCallVehicleReport asyncCallVehicleReport = new AsyncCallVehicleReport();
        asyncCallVehicleReport.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                strCategoryId,routeId,tripId,toDate.getText().toString());
        Functions.hideKeyboard(VehicleLoadActivity.this);
    }

    private class AsyncCallRouteList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //Common.showProgressDialog(VehicleLoadActivity.this);
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

                    //Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VehicleLoadActivity.this,
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
            //Common.showProgressDialog(VehicleLoadActivity.this);
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

                        TripListByRoutePojo tripListByRoutePojo = new TripListByRoutePojo();
                        tripListByRoutePojo.setRoutId(strRouteId);
                        tripListByRoutePojo.setTripId(strTripId);
                        tripListByRoutePojo.setTripName(strTripName);
                        tripListByRoutePojos.add(new TripListByRoutePojo(strTripName,strTripId,strRouteId));
                    }
                    tripListByRoutePojos.add(new TripListByRoutePojo("All Trip","-1","-1"));
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

                    //Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VehicleLoadActivity.this,
                            android.R.layout.simple_spinner_item, tripList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);
                }
            });
        }

    }
    // getting customer list
    private class AsyncAutoCategoryList extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //Common.showProgressDialog(VehicleLoadActivity.this);
        }

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
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String TripName = jarray.getJSONObject(i).getString("CategoryName");
                        final String TripId = jarray.getJSONObject(i).getString("CategoryId");
                        productCategoryArrayList.add(new ProductCategory(TripName, TripId));
                    }
                    productCategoryArrayList.add(0,new ProductCategory("All Category","-1"));
                    categoryName = new String[productCategoryArrayList.size()];
                    for (int j = 0; j < productCategoryArrayList.size(); j++) {
                        categoryName[j] = productCategoryArrayList.get(j).getServiceName();
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

            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Common.closeProgressDialog();
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VehicleLoadActivity.this,
                            android.R.layout.simple_spinner_item, categoryName);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_category_name.setAdapter(dataAdapter);
                }
            });

        }
    }

    private class AsyncCallVehicleReport extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //Common.showProgressDialog(VehicleLoadActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String partyId = params[0];
            String routeId = params[1];
            String tripId = params[2];
            String invDate = params[3];
            vehicleReportAdapter = new VehicleReportAdapter(getVehicleReport(partyId,
                    routeId,tripId,invDate),VehicleLoadActivity.this );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(VehicleLoadActivity.this));
                    recyclerView.setAdapter(vehicleReportAdapter);

                }

            });


            return partyId;
        }

        @Override
        protected void onPostExecute(String result) {
            //Common.closeProgressDialog();
            double total = 0;
            for (int j = 0; j < vehicleReportPojoArrayList.size(); j++) {
                total = total + Double.parseDouble(vehicleReportPojoArrayList.get(j).getStrItemQty());
                tv_vehicle_total_qty.setText(total + "");
            }

        }
    }

    public ArrayList<VehicleReportPojo> getVehicleReport(String catId, String routeId,
                                                         String tripId, String invDate){
        vehicleReportPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/VehicleLoadReports";
        String METHOD_NAME = "VehicleLoadReports";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("catId", catId);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
            Request.addProperty("invDate", invDate);
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
                    final String ProductName = jsonObject.getString("ProductName");
                    final String TotalQty = jsonObject.getString("TotalQty");
                    final String UnitName = jsonObject.getString("UnitName");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            VehicleReportPojo vehicleReportPojo = new VehicleReportPojo();
                            vehicleReportPojo.setStrItemName(ProductName);
                            vehicleReportPojo.setStrItemQty(TotalQty);
                            vehicleReportPojo.setStrItemUnit(UnitName);
                            vehicleReportPojoArrayList.add(vehicleReportPojo);
                            if (vehicleReportAdapter
                                    != null) {
                                vehicleReportAdapter
                                        .notifyDataSetChanged();
                            }

                        }
                    });
                }

            } else {
                Toast.makeText(VehicleLoadActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            //Common.closeProgressDialog();
            ex.printStackTrace();

        }
        return  vehicleReportPojoArrayList;
    }

    public void printData(final String routeName, final String tripName,final String date,
                          final String category, final ArrayList<VehicleReportPojo> vehicleReportPojoArrayList){
        Thread t = new Thread() {
            public void run() {
                try {
                    ScanActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> mPairedDevices = ScanActivity.mBluetoothAdapter.getBondedDevices();
                    if (mPairedDevices.size() > 0) {
                        OutputStream os = ScanActivity.mBluetoothSocket.getOutputStream();
                        String BILL = "";

                        BILL = "         Company Name  : "+strCompanyName +   "\n"
                                +"           Route Name :  "+routeName +   "\n"
                                +"           Trip Name. : "+ tripName+     "\n " +
                                "          Invoice Date : "+ date     +" \n" +
                                "           Category     :"+ category  +"\n";
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
                        Toast.makeText(VehicleLoadActivity.this, "No device Connected", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.e("ScanActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

}
