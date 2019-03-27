package com.example.admin.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.UnitConversionAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.UnitConversionPojo;
import com.example.admin.demo.model.UnitPojo;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.PrimaryUnitAdapter;
import com.example.admin.demo.util.SecondaryUnitAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnTouchListener, ProductIntefrace, View.OnFocusChangeListener, View.OnClickListener, CallBack {
    private AutoCompleteTextView at_primary_unit,at_secondary_unit;
    private LinearLayout ll_add_conversion;
    private Button btn_save,btn_reset;
    private RecyclerView rv_unit_conversion;
    private SoapPrimitive resultString;
    private Toolbar toolbar_manage_unit;
    private String[] primaryUnitName,secondaryUnitName;
    private PrimaryUnitAdapter primaryUnitAdapter;
    private SecondaryUnitAdapter secondaryUnitAdapter;
    private boolean atPrimaryFocus,atSecondaryFocus;
    private AlertDialog alertDialogItem;
    private AlertDialog.Builder alertDialogBuilderItem;
    private String strUnitId1,strUnitName1,strShortName1,strUnitId2,
            strUnitName2,strShortName2,strConversionId;
    private AutoCompleteTextView et_first_unit,et_second_unit,et_conversion_rate;
    private TextView tv_save_conversion,tv_cancel_conversion;
    private ArrayList<UnitConversionPojo> unitConversionPojoArrayList;
    private UnitConversionAdapter conversionAdapter;
    private SessionManagement session;
    private String compid = "", loginid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        toolbar_manage_unit = findViewById(R.id.toolbar_manage_unit);
        View view = toolbar_manage_unit.getRootView();
        TextView tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Unit Conversion");
        Functions.setToolBar(UnitActivity.this, toolbar_manage_unit, "Add Item", true);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        initViews();
        Intent intent = getIntent();
        if (intent !=null){
            strUnitName1 = intent.getStringExtra("UNIT_NAME");
            strUnitId1 = intent.getStringExtra("UNIT_ID");
            Toast.makeText(this, strUnitName1+"  " +strUnitId1, Toast.LENGTH_SHORT).show();
            at_primary_unit.setText(strUnitName1);
            atSecondaryFocus = true;
            atPrimaryFocus = false;
            at_secondary_unit.setFocusable(true);
            at_secondary_unit.requestFocus();
            //at_primary_unit.setEnabled(false);
        }
    }

    private void initViews() {
        at_primary_unit = findViewById(R.id.at_base_unit);
        at_secondary_unit = findViewById(R.id.at_secondary_unit);
        ll_add_conversion = findViewById(R.id.ll_add_conversion);
        btn_save = findViewById(R.id.btn_save_unit_conversion);
        btn_reset = findViewById(R.id.btn_clear_unit_conversion);
        rv_unit_conversion = findViewById(R.id.rv_unit_conversion);
        Functions.hideKeyboard(UnitActivity.this);
        at_primary_unit.setOnItemClickListener(this);
        at_secondary_unit.setOnItemClickListener(this);
        at_primary_unit.setOnTouchListener(this);
        at_secondary_unit.setOnTouchListener(this);
        at_primary_unit.setOnFocusChangeListener(this);
        ll_add_conversion.setOnClickListener(this);
        at_secondary_unit.setFocusable(false);
        btn_reset.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        at_primary_unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (atPrimaryFocus){
                    if (!s.toString().equals("")){
                        atSecondaryFocus = true;
                        at_secondary_unit.setFocusable(true);
                        at_secondary_unit.setOnTouchListener(UnitActivity.this);
                        at_secondary_unit.setOnFocusChangeListener(UnitActivity.this);
                        at_secondary_unit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (at_primary_unit.getText().toString().equals("")){
                                    atSecondaryFocus = false;
                                    //ll_add_conversion.setEnabled(false);
                                }
                                else
                                    at_secondary_unit.setFocusable(true);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (at_primary_unit.getText().toString().equals("")){
                                    atSecondaryFocus = false;
                                }
                                else
                                    at_secondary_unit.setFocusable(true);

                            }
                        });
                    }
                    else{
                        at_secondary_unit.setText("");
                        atSecondaryFocus = false;
                        at_secondary_unit.setFocusable(false);
                        Functions.hideKeyboard(UnitActivity.this);
                    }

                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.at_base_unit:
                break;
            case R.id.at_secondary_unit:

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.at_base_unit:
                AsyncAutoPrimaryUnitList asyncAutoPrimaryUnitList = new AsyncAutoPrimaryUnitList();
                asyncAutoPrimaryUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
                break;
            case R.id.at_secondary_unit:
                //atSecondaryFocus = true;
                AsyncAutoSecondaryUnitList asyncAutoSecondaryUnitList = new AsyncAutoSecondaryUnitList();
                asyncAutoSecondaryUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
                break;
        }
        return false;
    }

    @Override
    public void onProductSelected(String productId, String productName, String pricePerUnit, String taxId) {
        if (taxId.equals("primary")){
            at_primary_unit.setText(productName);
            strUnitId1 = productId;
            at_primary_unit.dismissDropDown();
            Functions.hideKeyboard(UnitActivity.this);
        }
        else if (taxId.equals("secondary")){
            strUnitId2 = productId;
            at_secondary_unit.setText(productName);
            at_secondary_unit.dismissDropDown();
            Functions.hideKeyboard(UnitActivity.this);
        }

        AsyncGetUnitConversion asyncGetUnitConversion = new AsyncGetUnitConversion();
        asyncGetUnitConversion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strUnitId1,strUnitId2);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.at_base_unit:
                atPrimaryFocus = hasFocus;
                break;
            case R.id.at_secondary_unit:
                atSecondaryFocus = hasFocus;
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_conversion:
                if (!TextUtils.isEmpty(at_secondary_unit.getText().toString())){
                    addConversionDialog();
                }
                break;
            case R.id.tv_save_conversion:
                if (TextUtils.isEmpty(et_first_unit.getText().toString())){
                    Toast.makeText(this, "Please select base unit", Toast.LENGTH_SHORT).show();
                }else {
                    AsynSaveUnitConversion asynSaveUnitConversion = new AsynSaveUnitConversion();
                    asynSaveUnitConversion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",
                            et_conversion_rate.getText().toString());
                    alertDialogItem.dismiss();
                }

                break;
            case R.id.tv_cancel_conversion:
                et_conversion_rate.setText("");
                alertDialogItem.dismiss();
                break;
            case R.id.btn_save_unit_conversion:
                getUnitConversionId();
                break;
            case R.id.btn_clear_unit_conversion:
                resetAutocompleteTextView();
                break;
        }
    }

    private void getUnitConversionId() {
        Intent intent= new Intent();
        intent.putExtra("CONVERSION_ID",strConversionId);
        setResult(2,intent);
        finish();
    }

    private void resetAutocompleteTextView() {
        at_primary_unit.setText("");
        at_secondary_unit.setText("");
        AsyncGetUnitConversion asyncGetUnitConversion = new AsyncGetUnitConversion();
        asyncGetUnitConversion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1","");
    }

    private void addConversionDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_unit_conversion, null);
        alertDialogBuilderItem = new AlertDialog.Builder(this);
        et_conversion_rate = view.findViewById(R.id.et_conversion_rate);
        et_first_unit = view.findViewById(R.id.et_first_unit);
        et_second_unit = view.findViewById(R.id.et_second_unit);
        tv_save_conversion = view.findViewById(R.id.tv_save_conversion);
        tv_cancel_conversion = view.findViewById(R.id.tv_cancel_conversion);
        et_first_unit.setText(at_primary_unit.getText().toString());
        et_second_unit.setText(at_secondary_unit.getText().toString());
        tv_save_conversion.setOnClickListener(this);
        tv_cancel_conversion.setOnClickListener(this);
        alertDialogBuilderItem.setView(view);
        alertDialogItem = alertDialogBuilderItem.create();
        alertDialogItem.setCancelable(false);
        alertDialogItem.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogItem.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialogItem.show();
        alertDialogItem.getWindow().setAttributes(lp);
        alertDialogItem.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

    }

    @Override
    public void AddParty(String str_name, String type, String id) {

    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {
        strConversionId = strPartyId;
        Toast.makeText(this, strPartyId + "", Toast.LENGTH_SHORT).show();

    }

    // getting customer list
    private class AsyncAutoPrimaryUnitList extends AsyncTask<String, Void, String> {
        final ArrayList<UnitPojo> primaryUnitList = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String SOAP_ACTION = "http://tempuri.org/ListUnit";
            String METHOD_NAME = "ListUnit";
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
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String UnitName = jarray.getJSONObject(i).getString("UnitName");
                        final String UnitId = jarray.getJSONObject(i).getString("UnitId");
                        final String ShortName = jarray.getJSONObject(i).getString("ShortName");

                        primaryUnitList.add(new UnitPojo(UnitName, UnitId,ShortName));
                    }
                    //primaryUnitList.add(0,new UnitPojo("All","-1",""));
                    primaryUnitName = new String[primaryUnitList.size()];
                    for (int j = 0; j < primaryUnitList.size(); j++) {
                        primaryUnitName[j] = primaryUnitList.get(j).getUnitName();
                    }
                } else {
                    Toast.makeText(UnitActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }

            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    primaryUnitAdapter = new PrimaryUnitAdapter(UnitActivity.this, primaryUnitList);
                    at_primary_unit.setAdapter(primaryUnitAdapter);
                    if (primaryUnitName.length > 0) {
                        // show all suggestions
                        if (!at_primary_unit.getText().toString().equals(""))
                            primaryUnitAdapter.getFilter().filter(null);
                        if (atPrimaryFocus){
                            at_primary_unit.showDropDown();
                        }
                    }

                }
            });

        }
    }

    // getting customer list
    private class AsyncAutoSecondaryUnitList extends AsyncTask<String, Void, String> {
        final ArrayList<UnitPojo> secondaryUnitList = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String SOAP_ACTION = "http://tempuri.org/ListUnit";
            String METHOD_NAME = "ListUnit";
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
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONArray jarray = new JSONArray(responseJSON);
                    for (int i = 0; i < jarray.length(); i++) {
                        final String UnitName = jarray.getJSONObject(i).getString("UnitName");
                        final String UnitId = jarray.getJSONObject(i).getString("UnitId");
                        final String ShortName = jarray.getJSONObject(i).getString("ShortName");
                        secondaryUnitList.add(new UnitPojo(UnitName, UnitId,ShortName));
                    }
                    secondaryUnitName = new String[secondaryUnitList.size()];
                    for (int j = 0; j < secondaryUnitList.size(); j++) {
                        secondaryUnitName[j] = secondaryUnitList.get(j).getUnitName();
                    }
                } else {
                    Toast.makeText(UnitActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    secondaryUnitAdapter = new SecondaryUnitAdapter(UnitActivity.this, secondaryUnitList);
                    at_secondary_unit.setAdapter(secondaryUnitAdapter);
                    if (secondaryUnitName.length > 0) {
                        // show all suggestions
                        if (!at_secondary_unit.getText().toString().equals(""))
                            secondaryUnitAdapter.getFilter().filter(null);
                        if (atSecondaryFocus){
                            at_secondary_unit.showDropDown();
                        }

                    }
                }
            });

        }
    }

    private class AsynSaveUnitConversion extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String strRate = params[1];
            saveUnitConversion(strUnitId1,strUnitId2,strRate);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(UnitActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(UnitActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String saveUnitConversion(final String unitId1, final String unitId2, String conversion_rate){
        String SOAP_ACTION = "http://tempuri.org/SaveUnitConversion";
        String METHOD_NAME = "SaveUnitConversion";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("unitId", unitId1);
            Request.addProperty("unit2", unitId2);
            Request.addProperty("rate", conversion_rate);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (responseJSON.equals(null)) {
                Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AsyncGetUnitConversion asyncGetUnitConversion = new AsyncGetUnitConversion();
                        asyncGetUnitConversion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",unitId1,unitId2);
                        Toast.makeText(UnitActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        clearAllEditText();
                    }
                });
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    private void clearAllEditText() {
        et_second_unit.setText("");
        et_first_unit.setText("");
    }

    public ArrayList<UnitConversionPojo> getUnitConversion(String strPrimaryId,String strSecondId) {
        final ArrayList<UnitConversionPojo> unitConversionPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListConversionUnit";
        String METHOD_NAME = "ListConversionUnit";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("mainUnitId", strPrimaryId);
            Request.addProperty("SecondUnitId", strSecondId);
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
                    final String MainUnitId = jsonObject.getString("MainUnitId");
                    final String MainUnitName = jsonObject.getString("MainUnitName");
                    final String ConversionRate = jsonObject.getString("ConversionRate");
                    final String ConversionId = jsonObject.getString("ConversionId");
                    final String ConversionUnitName = jsonObject.getString("ConversionUnitName");
                    final String ConversionUnitId = jsonObject.getString("ConversionUnitId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            UnitConversionPojo unitConversionPojo = new UnitConversionPojo();
                            unitConversionPojo.setConversionRate(ConversionRate);
                            unitConversionPojo.setConversionId(ConversionId);
                            unitConversionPojo.setMainUnitId(MainUnitId);
                            unitConversionPojo.setMainUnitName(MainUnitName);
                            unitConversionPojo.setConversionUnitName(ConversionUnitName);
                            unitConversionPojo.setConversionUnitId(ConversionUnitId);

                            unitConversionPojoArrayList.add(unitConversionPojo);
                            if (conversionAdapter
                                    != null) {
                                conversionAdapter
                                        .notifyDataSetChanged();
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return unitConversionPojoArrayList;

    }

    private class AsyncGetUnitConversion extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String strFirstUnit = params[1];
            String strSecondUnit = params[2];
            conversionAdapter = new UnitConversionAdapter(getUnitConversion(strFirstUnit,strSecondUnit), UnitActivity.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_unit_conversion.setNestedScrollingEnabled(false);
                    rv_unit_conversion.setHasFixedSize(true);
                    rv_unit_conversion.setLayoutManager(new LinearLayoutManager(UnitActivity.this));
                    rv_unit_conversion.setAdapter(conversionAdapter);

                }

            });

            return strFirstUnit;
        }
    }


}
