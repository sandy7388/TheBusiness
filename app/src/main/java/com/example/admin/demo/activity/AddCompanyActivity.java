package com.example.admin.demo.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.CompanyListAdapter;
import com.example.admin.demo.adapter.ProductItemsAdapter;
import com.example.admin.demo.functions.Functions;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class AddCompanyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ImageView iv_company_logo;
    private EditText et_company_name,et_company_contact,
            et_company_gstin,et_company_email,
            et_company_address,et_company_invoice,
            et_company_payment,et_company_bank,
            et_company_account,et_company_ifsc;
    private Spinner sp_state;
    private Toolbar toolbar_add_company;
    private TextView tv_app_name_nav_bar;
    private String strStateName,strCompanyId,strTaxNumber,strInvoiceNumber,strTaxInvoicePrefix;
    private CoordinatorLayout cl_add_item;
    private SoapPrimitive resultString;
    private Button btn_save_company,btn_clear_company,btn_delete_company,btn_edit_company;
    private ArrayAdapter<String> adapter;
    String[] STATES = {"Select State", "Maharashtra"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        toolbar_add_company = findViewById(R.id.toolbar_add_company);
        View view = toolbar_add_company.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Manage Company");
        Functions.setToolBar(AddCompanyActivity.this, toolbar_add_company,
                "Add Item", true);

        initialization();
    }
    void initialization(){
        iv_company_logo = findViewById(R.id.iv_company_logo);
        et_company_name = findViewById(R.id.et_company_name);
        et_company_contact = findViewById(R.id.et_company_contact);
        et_company_gstin = findViewById(R.id.et_company_gstin);
        et_company_email = findViewById(R.id.et_company_email);
        et_company_address = findViewById(R.id.et_company_address);
        et_company_invoice = findViewById(R.id.et_company_invoice);
        et_company_payment = findViewById(R.id.et_company_payment);
        et_company_bank = findViewById(R.id.et_company_bank);
        et_company_account = findViewById(R.id.et_company_account);
        et_company_ifsc = findViewById(R.id.et_company_ifsc);
        sp_state = findViewById(R.id.sp_state);
        cl_add_item = findViewById(R.id.cl_add_item);
        btn_clear_company = findViewById(R.id.btn_clear_company);
        btn_delete_company = findViewById(R.id.btn_delete_company);
        btn_save_company = findViewById(R.id.btn_save_company);
        btn_edit_company = findViewById(R.id.btn_edit_company);
        btn_clear_company.setOnClickListener(this);
        btn_delete_company.setOnClickListener(this);
        btn_save_company.setOnClickListener(this);
        btn_edit_company.setOnClickListener(this);
        sp_state.setOnItemSelectedListener(this);
        stateTypeSpinner();
        if (CompanyListAdapter.isCompanyEdit){
            strCompanyId = getIntent().getStringExtra("COMP_ID");
            new AsyncCallCompanyDetails().execute(strCompanyId);
            btn_delete_company.setVisibility(View.VISIBLE);
            btn_edit_company.setVisibility(View.VISIBLE);
            btn_save_company.setVisibility(View.GONE);
            btn_clear_company.setVisibility(View.GONE);

            et_company_name.setEnabled(false);
            et_company_contact.setEnabled(false);
            et_company_gstin.setEnabled(false);
            et_company_email.setEnabled(false);
            et_company_bank.setEnabled(false);
            et_company_account.setEnabled(false);
            et_company_ifsc.setEnabled(false);
            et_company_address.setEnabled(false);
            sp_state.setEnabled(false);
            et_company_invoice.setEnabled(false);
        }


    }

    void submitCompany() {

        if (strStateName.equals("Select State")) {
            Snackbar.make(cl_add_item, "Please Select State", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_company_name.getText().toString())) {
            Snackbar.make(cl_add_item, "Please Enter Business Name", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_company_contact.getText().toString())) {
            Snackbar.make(cl_add_item, "Please Enter Contact Number", Snackbar.LENGTH_SHORT).show();

        }
        else {

            new AsyncCallAddCompany()
            .execute(strStateName,et_company_invoice.getText().toString(),
                    et_company_name.getText().toString(),et_company_contact.getText().toString(),
                    et_company_gstin.getText().toString(),et_company_email.getText().toString(),
                    et_company_bank.getText().toString(),et_company_account.getText().toString(),
                    et_company_ifsc.getText().toString(),et_company_address.getText().toString());

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sp_state:
                strStateName = String.valueOf(spinner.getAdapter().getItem(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save_company:
                submitCompany();
                break;
            case R.id.btn_clear_company:
                break;
            case R.id.btn_edit_company:
                editAllItems();
                break;
            case R.id.btn_delete_company:
                new AsyncCallDeleteCompany().execute(strCompanyId);
                break;
        }
    }

    private class AsyncCallAddCompany extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String strState = strings[0];
            String strInvoice = strings[1];
            String strCompanyName = strings[2];
            String strContact = strings[3];
            String strGSTIN = strings[4];
            String strEmail = strings[5];
            String strBank = strings[6];
            String strAccount = strings[7];
            String strIFSC = strings[8];
            String strAddress = strings[9];
            String SOAP_ACTION,METHOD_NAME;
            if (CompanyListAdapter.isCompanyEdit){
                SOAP_ACTION = "http://tempuri.org/UpdateCompany";
                METHOD_NAME = "UpdateCompany";
            }
            else {
                SOAP_ACTION = "http://tempuri.org/SaveCompany";
                METHOD_NAME = "SaveCompany";
            }

            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("state", strState);
                Request.addProperty("invPrefix", strInvoice);
                Request.addProperty("companyName", strCompanyName);
                Request.addProperty("phone", strContact);
                Request.addProperty("gstin", strGSTIN);
                Request.addProperty("email", strEmail);
                Request.addProperty("bankName", strBank);
                Request.addProperty("accountNo", strAccount);
                Request.addProperty("IFSC", strIFSC);
                Request.addProperty("address", strAddress);
                Request.addProperty("taxNo", "");
                Request.addProperty("invoiceNo", "");
                Request.addProperty("taxInvPrefix", "");
                if (CompanyListAdapter.isCompanyEdit){
                    Request.addProperty("compId", strCompanyId);
                }
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (responseJSON.equals(null)) {
                    Toast.makeText(AddCompanyActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                } else {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (ProductItemsAdapter.isItemsEdit){
                                Toast.makeText(AddCompanyActivity.this, "Company Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AddCompanyActivity.this, "Company Successfully Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            //clearAllField();
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error";
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                CompanyListActivity.isCompanyAdded = true;
                Toast.makeText(AddCompanyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddCompanyActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearAllField() {
    }

    private class AsyncCallCompanyDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/GetCompanyDetailsById";
            String METHOD_NAME = "GetCompanyDetailsById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("cId", id);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONArray jarray = new JSONArray(responseJSON);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                et_company_name.setText(jarray.getJSONObject(0).getString("CompanyName"));
                                et_company_contact.setText(jarray.getJSONObject(0).getString("CompanyPhone"));

                                if (jarray.getJSONObject(0).getString("CAccountNumber").equals("null")){
                                    et_company_account.setText("");
                                }
                                else {
                                    et_company_account.setText(jarray.getJSONObject(0).getString("CAccountNumber"));
                                }

                                if (jarray.getJSONObject(0).getString("CAddress").equals("null")) {
                                    et_company_address.setText("");
                                } else {
                                    et_company_address.setText(jarray.getJSONObject(0).getString("CAddress"));
                                }

                                if (jarray.getJSONObject(0).getString("GSTINNumber").equals("null")) {
                                    et_company_gstin.setText("");
                                } else {
                                    et_company_gstin.setText(jarray.getJSONObject(0).getString("GSTINNumber"));
                                }

                                if (jarray.getJSONObject(0).getString("CompanyEmail").equals("null")) {
                                    et_company_email.setText("");
                                } else {
                                    et_company_email.setText(jarray.getJSONObject(0).getString("CompanyEmail"));
                                }

                                if (jarray.getJSONObject(0).getString("CompanyBankName").equals("null")) {
                                    et_company_bank.setText("");
                                } else {
                                    et_company_bank.setText(jarray.getJSONObject(0).getString("CompanyBankName"));
                                }

                                if (jarray.getJSONObject(0).getString("CBankIFSC").equals("null")) {
                                    et_company_ifsc.setText("");
                                } else {
                                    et_company_ifsc.setText(jarray.getJSONObject(0).getString("CBankIFSC"));
                                }
                                if (jarray.getJSONObject(0).getString("InvoicePrefix").equals("null")) {
                                    et_company_invoice.setText("");
                                } else {
                                    et_company_invoice.setText(jarray.getJSONObject(0).getString("InvoicePrefix"));
                                }

                                strCompanyId = jarray.getJSONObject(0).getString("CompanyId");
                                strStateName = jarray.getJSONObject(0).getString("CState");
                                if (strStateName != null) {
                                    int spinnerPosition = adapter.getPosition(strStateName);
                                    sp_state.setSelection(spinnerPosition);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } else {
                    Toast.makeText(AddCompanyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                }
            });
        }

    }

    private class AsyncCallDeleteCompany extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteCompany";
            String METHOD_NAME = "DeleteCompany";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("compId", id);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddCompanyActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(AddCompanyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error";

            }
            return "success";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                CompanyListActivity.isCompanyAdded = true;
                Toast.makeText(AddCompanyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddCompanyActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void stateTypeSpinner(){
        adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, STATES){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_state.setAdapter(adapter);
    }

    private void editAllItems() {
        et_company_name.setEnabled(true);
        et_company_gstin.setEnabled(true);
        et_company_contact.setEnabled(true);
        et_company_email.setEnabled(true);
        et_company_bank.setEnabled(true);
        et_company_account.setEnabled(true);
        et_company_ifsc.setEnabled(true);
        et_company_address.setEnabled(true);
        sp_state.setEnabled(true);
        et_company_invoice.setEnabled(true);
        btn_edit_company.setVisibility(View.GONE);
        btn_save_company.setVisibility(View.VISIBLE);
    }
}
