package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.session.SessionManagement;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddBankAccount extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar_bank_account;
    private TextView app_name;
    private EditText bank_acct_displayName,bank_acct_name,
            bank_acct_number,opening_balanace_amount,opening_balance_date;
    private Button delete_info_view,save_new_info,edit_info_view;
    private Date date;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ProgressBar pb_add_product;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private SoapPrimitive resultString;
    private String compid = "", loginid = "";
    private SessionManagement session;
    private CoordinatorLayout cl_add_bank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_info);
        toolbar_bank_account = findViewById(R.id.toolbar_bank_account);
        bank_acct_displayName = findViewById(R.id.bank_acct_displayName);
        bank_acct_name = findViewById(R.id.bank_acct_name);
        bank_acct_number = findViewById(R.id.bank_acct_number);
        opening_balanace_amount = findViewById(R.id.opening_balanace_amount);
        opening_balance_date = findViewById(R.id.opening_balance_date);
        delete_info_view = findViewById(R.id.delete_info_view);
        save_new_info = findViewById(R.id.save_new_info);
        edit_info_view = findViewById(R.id.edit_info_view);
        cl_add_bank = findViewById(R.id.cl_add_bank);
        View view = toolbar_bank_account.getRootView();
        app_name = view.findViewById(R.id.app_name_nav_bar);
        app_name.setText("Bank Account Add");
        Functions.setToolBar(AddBankAccount.this, toolbar_bank_account, "Bank To Bank", true);
        delete_info_view.setOnClickListener(this);
        save_new_info.setOnClickListener(this);
        edit_info_view.setOnClickListener(this);
        opening_balance_date.setOnClickListener(this);
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        opening_balance_date.setText(simpleDateFormat.format(calendar.getTime()));
        session = new SessionManagement(this);
        // Getting saved details of session
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_info_view:
                break;
            case R.id.save_new_info:
                saveBankInfo();
                break;
            case R.id.edit_info_view:
                break;
            case R.id.opening_balance_date:
                getDateFrom();
                break;
        }

    }

    private void saveBankInfo() {
        if (TextUtils.isEmpty(bank_acct_displayName.getText().toString())) {
            Snackbar.make(cl_add_bank, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(opening_balanace_amount.getText().toString())) {
            Snackbar.make(cl_add_bank, "Please Enter opening balance", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(opening_balance_date.getText().toString())) {
            Snackbar.make(cl_add_bank, "Please select data", Snackbar.LENGTH_SHORT).show();

        } else {
            Date date = new Date();
            String selectedDate = opening_balance_date.getText().toString();
            try {
                date = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(bank_acct_number.getText().toString())){
                bank_acct_number.setText("0");
            }
            else if (TextUtils.isEmpty(bank_acct_name.getText().toString())){
                bank_acct_name.setText("0");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);

            try {
                AsyncCallAddBankDetails callSalesInvoice = new AsyncCallAddBankDetails();
                callSalesInvoice.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                        formatedDate, bank_acct_displayName.getText().toString(),
                        bank_acct_name.getText().toString(),
                        bank_acct_number.getText().toString(),
                        opening_balanace_amount.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }

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
                opening_balance_date.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    private class  AsyncCallAddBankDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //pb_add_product.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String product = params[0];
            String openingDate = params[1];
            String displayName = params[2];
            String bankName = params[3];
            String accountNumber = params[4];
            String currentBalance = params[5];

            addBankDetails(displayName, bankName,
                    accountNumber, currentBalance, openingDate,loginid,compid);
            return product;
        }

        @Override
        protected void onPostExecute(String result) {
            BankListActivity.isBankAdded = true;
            if (result.equals("success")) {

                //pb_add_product.setVisibility(View.GONE);
                Toast.makeText(AddBankAccount.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                //pb_add_product.setVisibility(View.GONE);
                Toast.makeText(AddBankAccount.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String addBankDetails(String displayName, String bankName,
                                String accountNumber, String currentBalance,
                                String openingDate,String loginId,String compId) {
        String SOAP_ACTION = "http://tempuri.org/SaveBankAccount";
        String METHOD_NAME = "SaveBankAccount";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("accHolderName", displayName);
            Request.addProperty("bankName", bankName);
            Request.addProperty("accountNumber", accountNumber);
            Request.addProperty("balance", currentBalance);
            Request.addProperty("accDate", openingDate);
            Request.addProperty("loginId", loginId);
            Request.addProperty("compId", compId);
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
                        Toast.makeText(AddBankAccount.this, "Bank Successfully Added", Toast.LENGTH_SHORT).show();
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

    private void clearAllField() {
        bank_acct_displayName.setText("");
        bank_acct_name.setText("");
        bank_acct_number.setText("");
        opening_balanace_amount.setText("");
    }

}
