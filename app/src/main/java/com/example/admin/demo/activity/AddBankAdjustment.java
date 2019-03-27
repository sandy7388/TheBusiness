package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.BankNameAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.item.ItemBankList;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddBankAdjustment extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private Spinner sp_account_name, sp_account_deposit;
    String[] DEPOSITE_TYPE = {"Deposite Money", "Withdraw Money","Increase Balance","Reduce Balance"};
    private EditText adjustment_amount,adjustment_date,adjustment_details;
    private TextView tv_app_name;
    private Toolbar toolbar_bank_adjustment;
    private Date date;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ProgressBar pb_add_bank_adjustment;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private SoapPrimitive resultString;
    private Button delete_adjustment,edit_adjustment,save_adjustment;
    private ArrayList<ItemBankList> bankLists;
    private BankNameAdapter bankNameAdapter;
    private String[] bankList;
    private String strBankName,strBankId,strDepositeType;
    private String compid = "", loginid = "";
    private SessionManagement session;
    private CoordinatorLayout cl_add_bank_adjustment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_adjustment);
        toolbar_bank_adjustment = findViewById(R.id.toolbar_bank_adjustment);
        View view = toolbar_bank_adjustment.getRootView();
        tv_app_name = view.findViewById(R.id.app_name_nav_bar);
        cl_add_bank_adjustment = findViewById(R.id.cl_add_bank_adjustment);
        tv_app_name.setText("Bank Adjustment");
        Functions.setToolBar(AddBankAdjustment.this, toolbar_bank_adjustment, "Bank To Bank", true);
        sp_account_name = findViewById(R.id.sp_account_name);
        sp_account_deposit = findViewById(R.id.sp_account_deposit);
        adjustment_amount = findViewById(R.id.adjustment_amount);
        adjustment_date = findViewById(R.id.adjustment_date);
        adjustment_details = findViewById(R.id.adjustment_details);
        pb_add_bank_adjustment = findViewById(R.id.pb_add_bank_adjustment);
        delete_adjustment = findViewById(R.id.delete_adjustment);
        edit_adjustment = findViewById(R.id.edit_adjustment);
        save_adjustment = findViewById(R.id.save_adjustment);
        adjustment_date.setOnClickListener(this);
        delete_adjustment.setOnClickListener(this);
        edit_adjustment.setOnClickListener(this);
        save_adjustment.setOnClickListener(this);
        sp_account_name.setOnItemSelectedListener(this);
        sp_account_deposit.setOnItemSelectedListener(this);
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        adjustment_date.setText(simpleDateFormat.format(calendar.getTime()));
        session = new SessionManagement(this);
        // Getting saved details of session
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
        AsyncCallBankList asyncCallGetTripList = new AsyncCallBankList();
        asyncCallGetTripList.execute("1");
        depositeTypeSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adjustment_date:
                getDateFrom();
                break;
            case R.id.delete_adjustment:
                //getDateFrom();
                break;
            case R.id.edit_adjustment:
                //getDateFrom();
                break;
            case R.id.save_adjustment:
                saveBankAdjustmentInfo();
                break;

        }
    }

    // getting datepicker dialog
    private void getDateFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                strDate = dayOfMonth + "/" + "0" + month
                        + "/" + year;
                adjustment_date.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_account_name:
                strBankName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < bankLists.size(); i++) {
                    if (strBankName.equals(bankLists.get(i).getBankName())) {
                        strBankId = bankLists.get(i).getBankAccountId();
                        //String strRouteId = strBankId;
                    }
                }
                break;
            case R.id.sp_account_deposit:
                strDepositeType = String.valueOf(spinner.getAdapter().getItem(position));
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class  AsyncCallAddBankDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb_add_bank_adjustment.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String product = params[0];
            String adjustmentDate = params[1];
            String adjustmentAmount = params[2];
            String description = params[3];
            addBankAdjustmentDetails(strBankId, strDepositeType,
                    adjustmentAmount, adjustmentDate, description,compid,loginid);
            return product;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_add_bank_adjustment.setVisibility(View.GONE);
            if (result.equals("success")) {
                pb_add_bank_adjustment.setVisibility(View.GONE);
                Toast.makeText(AddBankAdjustment.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                pb_add_bank_adjustment.setVisibility(View.GONE);
                Toast.makeText(AddBankAdjustment.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String addBankAdjustmentDetails(String bankId, String accountType,
                                  String adjustmentAmount, String adjustmentDate,
                                  String description,String compId,String loginId) {
        String SOAP_ACTION = "http://tempuri.org/SaveBankAdjustment";
        String METHOD_NAME = "SaveBankAdjustment";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("bankId", bankId);
            Request.addProperty("type", accountType);
            Request.addProperty("amt", adjustmentAmount);
            Request.addProperty("date", adjustmentDate);
            Request.addProperty("desc", description);
            Request.addProperty("compId", compId);
            Request.addProperty("loginId", loginId);
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
                        Toast.makeText(AddBankAdjustment.this, "Transaction Successfully Added", Toast.LENGTH_SHORT).show();
                        clearAllField();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }

        return "success";
    }

    private void saveBankAdjustmentInfo() {
        if (TextUtils.isEmpty(adjustment_amount.getText().toString())) {
            Snackbar.make(cl_add_bank_adjustment, "Please adjustment amount", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(adjustment_date.getText().toString())) {
            Snackbar.make(cl_add_bank_adjustment, "Please select date", Snackbar.LENGTH_SHORT).show();

        } else if (strBankName.equals("Select Bank")){
            Snackbar.make(cl_add_bank_adjustment, "Please select bank", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Date date = new Date();
            String selectedDate = adjustment_date.getText().toString();
            try {
                date = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(adjustment_details.getText().toString())){
                adjustment_details.setText("");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);

            try {
                AsyncCallAddBankDetails callSalesInvoice = new AsyncCallAddBankDetails();
                callSalesInvoice.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                        formatedDate, adjustment_amount.getText().toString(),
                        adjustment_details.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void clearAllField() {
        adjustment_details.setText("");
        adjustment_amount.setText("");

    }

    private class AsyncCallBankList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            bankLists = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllBankAccount";
            String METHOD_NAME = "ListAllBankAccount";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("CompId", compid);
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
                        final String BankName = jarray.getJSONObject(i).getString("BankName");
                        final String CurrentBalance = jarray.getJSONObject(i).getString("CurrentBalance");
                        final String BankAccountId = jarray.getJSONObject(i).getString("BankAccountId");

                        bankLists.add(new ItemBankList(BankName, BankAccountId,CurrentBalance));
                    }
                    bankLists.add(0,new ItemBankList("Select Bank","",""));

                    //tripListByRoutePojos.add(0,new TripListByRoutePojo("All","-1",""));
                    bankList = new String[bankLists.size()];
                    for (int j = 0; j < bankLists.size(); j++) {
                        bankList[j] = bankLists.get(j).getBankName();
                    }
                } else {
                    Toast.makeText(AddBankAdjustment.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddBankAdjustment.this,
                            android.R.layout.simple_spinner_item, bankList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_account_name.setAdapter(dataAdapter);
                }
            });
        }

    }

    private void depositeTypeSpinner(){
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, DEPOSITE_TYPE){
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
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_account_deposit.setAdapter(spinnerArrayAdapter);
    }
}
