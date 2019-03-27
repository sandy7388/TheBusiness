package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddBankToBank extends AppCompatActivity implements View.OnClickListener {
    private Spinner sp_to_bank, sp_from_bank;
    String[] toBank = {"Select Bank","SBI","Maharashtra Bank","Axis Bank","ICICI"};
    String[] fromBank = {"Select Bank","SBI","Maharashtra Bank","Axis Bank","ICICI"};
    private Toolbar toolbar_bank_to_bank;
    private TextView tv_app_name;
    private EditText edt_transfer_amount,edt_transfer_date,edt_transfer_description;
    private Button btn_save_transfer,btn_edit_transfer,btn_delete_transfer;
    private Date date;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ProgressBar pb_add_bank_transfer;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private SoapPrimitive resultString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_to_bank);
        toolbar_bank_to_bank = findViewById(R.id.toolbar_bank_to_bank);
        View view = toolbar_bank_to_bank.getRootView();
        tv_app_name = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name.setText("Bank Adjustment");
        sp_from_bank = findViewById(R.id.sp_from_bank);
        sp_to_bank = findViewById(R.id.sp_to_bank);
        edt_transfer_amount = findViewById(R.id.edt_transfer_amount);
        edt_transfer_date = findViewById(R.id.edt_transfer_date);
        edt_transfer_description = findViewById(R.id.edt_transfer_description);
        pb_add_bank_transfer = findViewById(R.id.pb_add_bank_transfer);
        btn_save_transfer = findViewById(R.id.btn_save_transfer);
        btn_edit_transfer = findViewById(R.id.btn_edit_transfer);
        btn_delete_transfer = findViewById(R.id.btn_delete_transfer);
        Functions.setAdapterwithposition(this, toBank, sp_to_bank, "Select Bank");
        Functions.setAdapterwithposition(this, fromBank, sp_from_bank, "Select Bank");
        Functions.setToolBar(AddBankToBank.this, toolbar_bank_to_bank, "Bank To Bank", true);
        btn_save_transfer.setOnClickListener(this);
        btn_edit_transfer.setOnClickListener(this);
        btn_delete_transfer.setOnClickListener(this);
        edt_transfer_date.setOnClickListener(this);
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        edt_transfer_date.setText(simpleDateFormat.format(calendar.getTime()));


    }

    @Override
    public void onClick(View v) {

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
                edt_transfer_date.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    private class  AsyncCallAddBankDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb_add_bank_transfer.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String product = params[0];
            String accountName = params[1];
            String accountType = params[2];
            String adjustmentAmount = params[3];
            String adjustmentDate = params[4];
            String description = params[5];
            addBankAdjustmentDetails(accountName, accountType,
                    adjustmentAmount, adjustmentDate, description);
            return product;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                pb_add_bank_transfer.setVisibility(View.GONE);
                Toast.makeText(AddBankToBank.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                pb_add_bank_transfer.setVisibility(View.GONE);
                Toast.makeText(AddBankToBank.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String addBankAdjustmentDetails(String amount, String date,
                                            String adjustmentAmount, String adjustmentDate,
                                            String description) {
        String SOAP_ACTION = "http://tempuri.org/SaveProduct";
        String METHOD_NAME = "SaveProduct";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("catId", amount);
            Request.addProperty("compId", date);
            Request.addProperty("loginId", description);
            Request.addProperty("product", adjustmentDate);
            Request.addProperty("shortName", description);
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
                        Toast.makeText(AddBankToBank.this, "Product Successfully Added", Toast.LENGTH_SHORT).show();
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
}
