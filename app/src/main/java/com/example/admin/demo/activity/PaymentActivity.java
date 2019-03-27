package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.demo.adapter.PaymentListAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.CustomerAdapter;
import com.example.admin.demo.util.InstantAutoComplete;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;

public class PaymentActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener, CallBack,
        TextWatcher, View.OnFocusChangeListener {

    private Toolbar toolbar_payment;
    private CoordinatorLayout cl_add_payment;
    private MaterialSpinner sp_party_type, sp_payment_mode;
    private CustomerAdapter customerAdapter = null;
    private String strPaymentDate, strChequeDate,strPartyId, strPartyName,
            strPartyBalance, strPartyType, strPaymentMode, strTripId,
            strRouteId, strPaymentId,compid = "", loginid = "";
    private int date_Year_cheque, date_Month_cheque, date_Day_cheque,
            date_Year_payment, date_Month_payment, date_Day_payment,hour_From,minute_From,hour_To,minute_To;
    private DatePickerDialog chequeDateDialog, paymentDateDialog;
    private SoapPrimitive resultString;
    private Calendar calendar_cheque, calendar_payment;
    private SimpleDateFormat simpleDateFormat;
    private static final String[] ITEMS = {"Receivable", "Payable"};
    private static final String[] ITEMS2 = {"Cash", "Cheque"};
    private InstantAutoComplete autoCompleteTextView;
    private String[] customerName;
    private ArrayList<CustomerDetailsPojo> customerDetailsPojoArrayList;
    private EditText edit_payment_date, edit_previous_balance_amount, edit_paid_amount,
            edit_payment_reference, edit_check_deposit_date,edit_balance_amount;
    private Button btn_cancel, btn_save, btn_edit_payment, btn_delete_payment;
    private TextView appNameNavBar;
    private boolean isReceivable,isPaidFocus;
    private SessionManagement session;
    private LinearLayout ll_cheque_deposit_date;
    private ArrayAdapter<String> paymentModeAdapter, partyTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        Functions.setToolBar(PaymentActivity.this, toolbar_payment, "Add Payment Activity", true);
        appNameNavBar.setText("Add Payment");
        onClickListener();
        partyTypeSpinner();
        paymentModeSpinner();
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        if (PartyInvoiceActivity.checkParty) {
            Intent intent = getIntent();
            if (intent != null) {
                strPartyName = intent.getStringExtra("PARTY_NAME");
                strPartyId = intent.getStringExtra("PARTY_ID");
                strPartyBalance = intent.getStringExtra("PARTY_BALANCE");
                strTripId = intent.getStringExtra("TRIP_ID");
                strRouteId = intent.getStringExtra("ROUTE_ID");
                autoCompleteTextView.setText(strPartyName);
                edit_previous_balance_amount.setText(strPartyBalance);
                appNameNavBar.setText(strPartyName);
                sp_party_type.setVisibility(View.GONE);

            }
        } else {
            sp_party_type.setVisibility(View.VISIBLE);
        }

        if (MainActivity.isSale) {
            strPartyType = "Receivable";
        } else if (MainActivity.isPurchase) {
            strPartyType = "Payable";
        }

        if (PaymentListAdapter.isPaymentEdit) {
            strPaymentId = getIntent().getStringExtra("PAYMENT_ID");
            String strName = getIntent().getStringExtra("PARTY_NAME");
            String strBalance = getIntent().getStringExtra("BALANCE");
            new AsyncCallPaymentDetails().execute(strPaymentId,strName,strBalance);
            btn_delete_payment.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
            btn_edit_payment.setVisibility(View.VISIBLE);
            btn_save.setVisibility(View.GONE);
            autoCompleteTextView.setEnabled(false);
            edit_payment_date.setEnabled(false);
            edit_previous_balance_amount.setEnabled(false);
            edit_paid_amount.setEnabled(false);
            edit_payment_reference.setEnabled(false);
            sp_party_type.setEnabled(false);
            sp_payment_mode.setEnabled(false);
            edit_check_deposit_date.setEnabled(false);

        }

    }

    private void initView() {
        cl_add_payment = findViewById(R.id.cl_add_payment);
        toolbar_payment = findViewById(R.id.toolbar_payment);
        View view = toolbar_payment.getRootView();
        appNameNavBar = view.findViewById(R.id.app_name_nav_bar);
        sp_party_type = findViewById(R.id.sp_party_type);
        autoCompleteTextView = findViewById(R.id.autoTextCustomer);
        sp_payment_mode = findViewById(R.id.sp_payment_mode);
        edit_payment_date = findViewById(R.id.edit_payment_date);
        edit_previous_balance_amount = findViewById(R.id.edit_previous_balance_amount);
        edit_balance_amount = findViewById(R.id.edit_balance_amount);
        edit_paid_amount = findViewById(R.id.edit_paid_amount);
        edit_payment_reference = findViewById(R.id.edit_payment_reference);
        edit_check_deposit_date = findViewById(R.id.edit_check_deposit_date);
        ll_cheque_deposit_date = findViewById(R.id.ll_cheque_deposit_date);
        btn_cancel = findViewById(R.id.btn_cancel_payment);
        btn_save = findViewById(R.id.btn_save_payment);
        btn_edit_payment = findViewById(R.id.btn_edit_payment);
        btn_delete_payment = findViewById(R.id.btn_delete_payment);
        calendar_cheque = Calendar.getInstance();
        date_Year_cheque = calendar_cheque.get(Calendar.YEAR);
        date_Month_cheque = calendar_cheque.get(Calendar.MONTH);
        date_Day_cheque = calendar_cheque.get(Calendar.DATE);
        hour_To = calendar_cheque.get(Calendar.HOUR_OF_DAY);
        minute_To = calendar_cheque.get(Calendar.MINUTE);
        calendar_payment = Calendar.getInstance();
        date_Year_payment = calendar_payment.get(Calendar.YEAR);
        date_Month_payment = calendar_payment.get(Calendar.MONTH);
        date_Day_payment = calendar_payment.get(Calendar.DATE);
        hour_From = calendar_payment.get(Calendar.HOUR_OF_DAY);
        minute_From = calendar_payment.get(Calendar.MINUTE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        edit_payment_date.setText(simpleDateFormat.format(calendar_payment.getTime()));
        edit_check_deposit_date.setText(simpleDateFormat.format(calendar_cheque.getTime()));
    }

    public void onClickListener() {
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delete_payment.setOnClickListener(this);
        btn_edit_payment.setOnClickListener(this);
        edit_check_deposit_date.setOnClickListener(this);
        edit_payment_date.setOnClickListener(this);
        sp_party_type.setOnItemSelectedListener(this);
        sp_payment_mode.setOnItemSelectedListener(this);
        edit_paid_amount.addTextChangedListener(this);
        edit_paid_amount.setOnFocusChangeListener(this);
    }

    // getting datepicker dialog
    private void getDate(View view) {
        switch (view.getId()) {
            case R.id.edit_check_deposit_date:
                chequeDateDialog = new DatePickerDialog(
                        this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        strChequeDate =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                                +  mFormat.format(Double.valueOf(dayOfMonth));
                        timePickerTo();
                        //edit_check_deposit_date.setText(strChequeDate);
                    }
                }, date_Year_cheque, date_Month_cheque, date_Day_cheque
                );
                chequeDateDialog.show();

                break;
            case R.id.edit_payment_date:
                paymentDateDialog = new DatePickerDialog(
                        this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        DecimalFormat mFormat= new DecimalFormat("00");
                        mFormat.setRoundingMode(RoundingMode.DOWN);
                        strPaymentDate =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                                +  mFormat.format(Double.valueOf(dayOfMonth));
                        timePickerFrom();
                    }
                }, date_Year_payment, date_Month_payment, date_Day_payment
                );
                paymentDateDialog.show();

                break;

        }

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
                        edit_payment_date.setText(strPaymentDate + " " + mFormat.format(hourOfDay) + ":" + mFormat.format(minute));
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
                        //fromDate.setText(strDateTimeFrom + " " + hourOfDay + ":" + minute);
                        edit_check_deposit_date.setText(strChequeDate + " " + mFormat.format(hourOfDay) + ":" + mFormat.format(minute));
                    }
                }, hour_To, minute_To, false);
        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_payment:
                submitPaymentDetails();
                break;
            case R.id.btn_cancel_payment:
                clearAllEditText();
                break;
            case R.id.edit_check_deposit_date:
                getDate(view);
                break;
            case R.id.edit_payment_date:
                getDate(view);
                break;
            case R.id.btn_delete_payment:
                PartyListActivity.isRefreshedParty = true;
                new AsyncCallDeletePayment().execute(strPaymentId);
                break;
            case R.id.btn_edit_payment:
                getEditablesDetails();
                break;
        }

    }

    private void getEditablesDetails() {
        autoCompleteTextView.setEnabled(true);
        edit_payment_date.setEnabled(true);
        edit_previous_balance_amount.setEnabled(true);
        edit_paid_amount.setEnabled(true);
        edit_payment_reference.setEnabled(true);
        sp_party_type.setEnabled(true);
        sp_payment_mode.setEnabled(true);
        edit_check_deposit_date.setEnabled(true);
        btn_edit_payment.setVisibility(View.GONE);
        btn_save.setVisibility(View.VISIBLE);
        isPaidFocus = true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_party_type:
                strPartyType = String.valueOf(spinner.getAdapter().getItem(position));
                if (strPartyType.equals("Receivable")) {
                    isReceivable = true;
                    AsyncAutoCustomerList asyncAutoCustomerList = new AsyncAutoCustomerList();
                    asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strPartyType);
                } else if (strPartyType.equals("Payable")) {
                    isReceivable = false;
                    AsyncAutoCustomerList asyncAutoCustomerList = new AsyncAutoCustomerList();
                    asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strPartyType);
                }
                break;

            case R.id.sp_payment_mode:
                strPaymentMode = String.valueOf(spinner.getAdapter().getItem(position));
                if (strPaymentMode.equals("Cash")) {
                    ll_cheque_deposit_date.setVisibility(View.GONE);
                } else if (strPaymentMode.equals("Cheque")) {
                    ll_cheque_deposit_date.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void partyTypeSpinner() {
        partyTypeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ITEMS) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        partyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_party_type.setAdapter(partyTypeAdapter);
    }

    private void paymentModeSpinner() {
        paymentModeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ITEMS2) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        paymentModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_payment_mode.setAdapter(paymentModeAdapter);
        sp_payment_mode.setSelection(0);
    }

    @Override
    public void AddParty(String str_name, String type, String id) {
        strPartyId = id;
        strPartyBalance = type;
        strPartyName = str_name;
        autoCompleteTextView.setText(strPartyName);
        edit_previous_balance_amount.setText(strPartyBalance);
        autoCompleteTextView.dismissDropDown();
        Functions.hideKeyboard(PaymentActivity.this);
        edit_payment_date.requestFocus();
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!autoCompleteTextView.getText().toString().equals("")){
            if (isPaidFocus){
                if (!s.toString().equals("")) {
                    if (count == 0) {
                        edit_balance_amount.setText("");
                    }
                }
            }
        }


    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!autoCompleteTextView.getText().toString().equals("")){
            if (isPaidFocus){
                if (!s.toString().equals("")){
                    double paidAmount,previousAmount,balance;
                    paidAmount = Double.parseDouble(s.toString());
                    previousAmount = Double.parseDouble(edit_previous_balance_amount.getText().toString());

                    edit_balance_amount.setText(String.valueOf(previousAmount - paidAmount));
                }
                else if (s.toString().equals("")){
                    double paidAmount,previousAmount,balance;
                    paidAmount = 0.00;
                    previousAmount = Double.parseDouble(edit_previous_balance_amount.getText().toString());

                    edit_balance_amount.setText(String.valueOf(previousAmount - paidAmount));
                }

            }
        }


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isPaidFocus = hasFocus;
    }

    // getting customer list
    private class AsyncAutoCustomerList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String strPartyType = params[1];
            customerDetailsPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllCustomer";
            String METHOD_NAME = "ListAllCustomer";
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
                        final String NickName = jarray.getJSONObject(i).getString("NickName");
                        final String PartyCurrentBalance = jarray.getJSONObject(i).getString("PartyCurrentBalance");
                        final String PartyName = jarray.getJSONObject(i).getString("PartyName");
                        final String PartyId = jarray.getJSONObject(i).getString("PartyId");
                        final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                        final String TripId = jarray.getJSONObject(i).getString("TripId");
                        final String IsReceivable = jarray.getJSONObject(i).getString("IsReceivable");
                        if (isReceivable) {
                            if (IsReceivable.equals("true")) {
                                CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
                                customerDetailsPojo.setNickName(NickName);
                                customerDetailsPojo.setPartyCurrentBalance(PartyCurrentBalance);
                                customerDetailsPojo.setPartyName(PartyName);
                                customerDetailsPojo.setPartyId(PartyId);
                                customerDetailsPojo.setTripId(TripId);
                                customerDetailsPojo.setRouteId(RouteId);
                                customerDetailsPojoArrayList.add(customerDetailsPojo);
                            }
                        }
                        if (!isReceivable) {
                            if (IsReceivable.equals("false")) {
                                CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
                                customerDetailsPojo.setNickName(NickName);
                                customerDetailsPojo.setPartyCurrentBalance(PartyCurrentBalance);
                                customerDetailsPojo.setPartyName(PartyName);
                                customerDetailsPojo.setPartyId(PartyId);
                                customerDetailsPojo.setTripId(TripId);
                                customerDetailsPojo.setRouteId(RouteId);
                                customerDetailsPojoArrayList.add(customerDetailsPojo);
                            }
                        }

                    }
                    customerName = new String[customerDetailsPojoArrayList.size()];
                    for (int j = 0; j < customerDetailsPojoArrayList.size(); j++) {
                        customerName[j] = customerDetailsPojoArrayList.get(j).getPartyName();
                    }
                }
            } catch (Exception e) {
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
                    autoCompleteTextView.setThreshold(0);
                    customerAdapter = new CustomerAdapter(PaymentActivity.this, customerDetailsPojoArrayList);
                    autoCompleteTextView.setAdapter(customerAdapter);
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

    private void submitPaymentDetails() {
        if (TextUtils.isEmpty(edit_payment_date.getText().toString())) {
            Snackbar.make(cl_add_payment, "Please Select Payment Date", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edit_previous_balance_amount.getText().toString())) {
            Snackbar.make(cl_add_payment, "Please Enter balance amount", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edit_check_deposit_date.getText().toString())) {
            Snackbar.make(cl_add_payment, "Please Select cheque deposit date", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(edit_paid_amount.getText().toString())) {
            Snackbar.make(cl_add_payment, "Please Enter Paid Amount", Snackbar.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            Date date1 = new Date();
            String cheQueDate = edit_check_deposit_date.getText().toString();
            String paymentDate = edit_payment_date.getText().toString();
            try {
                date = simpleDateFormat.parse(cheQueDate);
                date1 = simpleDateFormat.parse(paymentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedChequeDate = simpleDateFormat.format(date);
            String formatedPaymentDate = simpleDateFormat.format(date1);
            AsyncCallAddPaymentDetails asyncCallWS = new AsyncCallAddPaymentDetails();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1",
                    edit_paid_amount.getText().toString(),
                    edit_payment_reference.getText().toString(),
                    formatedChequeDate, formatedPaymentDate);
        }

    }

    private class AsyncCallAddPaymentDetails extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String paid_amount = params[1];
            String payment_ref = params[2];
            String cheque_date = params[3];
            String payment_date = params[4];
            addPaymentDetails(compid, loginid, strPartyId, paid_amount,
                    payment_ref, cheque_date, payment_date, strPaymentMode);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            //PaymentListActivity.isPaymentRefreshed = true;
            if (result.equals("success")) {
                PaymentListActivity.isPaymentRefreshed = true;
                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String addPaymentDetails(String compid, String loginid,
                                     String party_name,
                                     String paid_amount, String payment_ref,
                                     String cheque_date, String payment_date,
                                     String payment_mode) {

        String SOAP_ACTION, METHOD_NAME;
        if (PaymentListAdapter.isPaymentEdit) {
            SOAP_ACTION = "http://tempuri.org/UpdatePartyPayment";
            METHOD_NAME = "UpdatePartyPayment";
        } else {
            SOAP_ACTION = "http://tempuri.org/SavePayment";
            METHOD_NAME = "SavePayment";
        }
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("compId", compid);
            Request.addProperty("loginId", loginid);
            Request.addProperty("partyId", party_name);
            Request.addProperty("paidAmt", paid_amount);
            Request.addProperty("reference", payment_ref);
            Request.addProperty("depositDate", cheque_date);
            Request.addProperty("payDate", payment_date);
            Request.addProperty("mode", payment_mode);
            if (strPartyType.equals("Receivable")) {
                Request.addProperty("isReceivable", "true");
            } else {
                Request.addProperty("isReceivable", "false");
            }
            Request.addProperty("payStatus", strPartyType);
            if (PaymentListAdapter.isPaymentEdit) {
                Request.addProperty("paymentId", strPaymentId);
            }
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
                        if (PaymentListAdapter.isPaymentEdit) {
                            clearAllEditText();
                            Toast.makeText(PaymentActivity.this, "Payment Details Successfully Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (PartyInvoiceActivity.checkParty){
                            Toast.makeText(PaymentActivity.this, "Payment Details Successfully Saved", Toast.LENGTH_SHORT).show();
                            //clearAllEditText();
                            //PartyInvoiceActivity.isRefreshed = true;
                            //setResult(2);
                            Intent intent = new Intent(PaymentActivity.this, PartyInvoiceActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("PARTY_ID", strPartyId);
                            intent.putExtra("PARTY_NAME", strPartyName);
                            intent.putExtra("PARTY_BALANCE", edit_balance_amount.getText().toString());
                            intent.putExtra("TRIP_ID", strTripId);
                            intent.putExtra("ROUTE_ID", strRouteId);
                            startActivity(intent);
                            //finish();
                        }
                        else {
                            Toast.makeText(PaymentActivity.this, "Payment Details Successfully Saved", Toast.LENGTH_SHORT).show();
                            clearAllEditText();
                            //setResult(2);
                            finish();//finishing activity
                        }

                    }
                });

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
        return "success";
    }

    private void clearAllEditText() {
        edit_payment_reference.setText("");
        edit_paid_amount.setText("");
        autoCompleteTextView.setText("");
        edit_previous_balance_amount.setText("");
        edit_balance_amount.setText("");
    }

    private class AsyncCallPaymentDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            final String name = params[1];
            final String balance = params[2];

            String SOAP_ACTION = "http://tempuri.org/GetPartyPaymentById";
            String METHOD_NAME = "GetPartyPaymentById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("paymentId", id);
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

                                strPartyId = jarray.getJSONObject(0).getString("PartyId");
                                strPaymentMode = jarray.getJSONObject(0).getString("PaymentMode");
                                strPartyType = jarray.getJSONObject(0).getString("PaymentStatus");
//                                for (int i = 0; i < customerDetailsPojoArrayList.size(); i++) {
//                                    if (strPartyId.equals(customerDetailsPojoArrayList.get(i).getPartyId())) {
//                                        autoCompleteTextView.setText(customerDetailsPojoArrayList.get(i).getPartyName());
//                                    }
//                                }
                                autoCompleteTextView.setText(name);
                                edit_previous_balance_amount.setText(balance);
                                if (jarray.getJSONObject(0).getString("PaidAmount").equals("0")) {
                                    edit_paid_amount.setText("");
                                } else {
                                    edit_paid_amount.setText(jarray.getJSONObject(0).getString("PaidAmount"));
                                }

                                if (jarray.getJSONObject(0).getString("ReferenceNo").equals("0")) {
                                    edit_payment_reference.setText("");
                                } else {
                                    edit_payment_reference.setText(jarray.getJSONObject(0).getString("ReferenceNo"));
                                }

                                Date paymentDate = new Date();
                                Date chequeDate = new Date();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                paymentDate = simpleDateFormat.parse(jarray.getJSONObject(0).getString("PaymentDate"));
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                edit_payment_date.setText(simpleDateFormat1.format(paymentDate));
                                chequeDate = simpleDateFormat.parse(jarray.getJSONObject(0).getString("ChequeDepositDate"));
                                edit_check_deposit_date.setText(simpleDateFormat1.format(chequeDate));


                                if (strPaymentMode != null) {
                                    int spinnerPosition = paymentModeAdapter.getPosition(strPaymentMode);
                                    sp_payment_mode.setSelection(spinnerPosition);
                                }

                                if (strPartyType != null) {
                                    int spinnerPosition = partyTypeAdapter.getPosition(strPartyType);
                                    sp_party_type.setSelection(spinnerPosition);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } else {
                    Toast.makeText(PaymentActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }

    }

    private class AsyncCallDeletePayment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeletePartyPaymentById";
            String METHOD_NAME = "DeletePartyPaymentById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("PaymentId", id);
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
                            Toast.makeText(PaymentActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    });

                } else {
                    Toast.makeText(PaymentActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }


    }




}
