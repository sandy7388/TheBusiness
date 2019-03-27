package com.example.admin.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.admin.demo.R;
import com.example.admin.demo.adapter.PaymentListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.PartyInvoicePojo;
import com.example.admin.demo.model.PaymentListPojo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class PaymentListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private TextView tv_toolbar;
    private ProgressBar pb_list_payment;
    private SoapPrimitive resultString;
    private PaymentListAdapter paymentListAdapter;
    private ArrayList<PaymentListPojo> paymentListPojoArrayList;
    private EditText edt_search_customer;
    public static boolean isPaymentRefreshed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        toolbar = findViewById(R.id.toolbar_payment_list);
        View view = toolbar.getRootView();
        tv_toolbar = view.findViewById(R.id.app_name_nav_bar);
        floatingActionButton = findViewById(R.id.fb_invoice_payment);
        recyclerView = findViewById(R.id.rv_party_payment);
        pb_list_payment = findViewById(R.id.pb_list_payment);
        edt_search_customer = findViewById(R.id.edt_search_customer);
        floatingActionButton.setOnClickListener(this);
        Functions.setToolBar(PaymentListActivity.this, toolbar, "Add PaymentActivity", true);
        //appNameNavBar.setText("Add Payment");
        tv_toolbar.setText("Payment List");
        scrollFabButton();
        AsyncCallPaymentList asyncCallPaymentList = new AsyncCallPaymentList();
        asyncCallPaymentList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
        edt_search_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    filter(s.toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_invoice_payment:
                PaymentListAdapter.isPaymentEdit = false;
                startActivity(new Intent(this,PaymentActivity.class));
                break;
        }
    }

    private class AsyncCallPaymentList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_list_payment.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            paymentListAdapter = new PaymentListAdapter(getPaymentList(),PaymentListActivity.this );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PaymentListActivity.this));
                    recyclerView.setAdapter(paymentListAdapter);

                }

            });


            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_payment.setVisibility(View.GONE);
        }
    }

    public ArrayList<PaymentListPojo> getPaymentList(){
        paymentListPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListPartyPayment";
        String METHOD_NAME = "ListPartyPayment";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

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
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final String ChequeDepositDate = jsonObject.getString("ChequeDepositDate");
                    final String PaymentDate = jsonObject.getString("PaymentDate");
                    final String PartyName = jsonObject.getString("PartyName");
                    final String PartyCurrentBalance = jsonObject.getString("PartyCurrentBalance");
                    final String PaidAmount = jsonObject.getString("PaidAmount");
                    final String PartyId = jsonObject.getString("PartyId");
                    final String ReferenceNo = jsonObject.getString("ReferenceNo");
                    final String PaymentStatus = jsonObject.getString("PaymentStatus");
                    final String PaymentMode = jsonObject.getString("PaymentMode");
                    final String PaymentId = jsonObject.getString("PaymentId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            PaymentListPojo paymentListPojo = new PaymentListPojo(ChequeDepositDate,
                                    PaymentDate,PaymentId,PartyName,PartyCurrentBalance,PaidAmount,
                                    PartyId,ReferenceNo,PaymentStatus,PaymentMode);

                            paymentListPojoArrayList.add(paymentListPojo);
                        }
                    });
                }

            } else {
                Toast.makeText(PaymentListActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return paymentListPojoArrayList;


    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (isPaymentRefreshed){
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            AsyncCallPaymentList callInvoiceList = new AsyncCallPaymentList();
            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
            isPaymentRefreshed = false;
        }
    }

    public void scrollFabButton(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy > 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });
    }

    void filter(String text){
        if (!paymentListPojoArrayList.isEmpty()){
            List<PaymentListPojo> temp = new ArrayList();
            for(PaymentListPojo paymentListPojo: paymentListPojoArrayList){
                String name = paymentListPojo.getPartyName().toLowerCase();
                if(name.contains(text)){
                    temp.add(paymentListPojo);
                }
            }
            paymentListAdapter.updateList(temp);
        }

    }
}
