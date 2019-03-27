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
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.ExpenseDetailsAdapter;
import com.example.admin.demo.adapter.ExpenseListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.item.ItemExpenseList;
import com.example.admin.demo.model.ExpenseItems;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ExpensesDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView textView;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private String strExpCategoryName,strExpCategoryId;
    private ExpenseDetailsAdapter expenseDetailsAdapter;
    private ArrayList<ExpenseItems> expenseItemsArrayList;
    private SoapPrimitive resultString;
    public static boolean isExpenseDetailsListRefreshed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_details);
        floatingActionButton = findViewById(R.id.fab_add_expenses_list);
        progressBar = findViewById(R.id.pb_expenses_details);
        recyclerView = findViewById(R.id.rv_expenses_details);
        toolbar = findViewById(R.id.toolbar_expense_details);
        View view = toolbar.getRootView();
        textView = view.findViewById(R.id.app_name_nav_bar);
        Functions.setToolBar(ExpensesDetailsActivity.this, toolbar, "Expense List", true);
        floatingActionButton.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent !=null){
            strExpCategoryName = intent.getStringExtra("CATEGORY_NAME");
            strExpCategoryId= intent.getStringExtra("CATEGORY_ID");
            textView.setText(strExpCategoryName);
        }

        AsyncCallExpensesList callInvoiceList = new AsyncCallExpensesList();
        callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strExpCategoryId);

    }

    private ArrayList<ExpenseItems> getExpensedata(String strCategoryId) {
        expenseItemsArrayList = new ArrayList<>();

        String SOAP_ACTION = "http://tempuri.org/ListAllExpensesByCatId";
        String METHOD_NAME = "ListAllExpensesByCatId";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("ExpCatId",strCategoryId);
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
                    final String TotalAmount = jsonObject.getString("TotalAmount");
                    final String ExDate = jsonObject.getString("ExDate");
                    final String ExpenseCategory = jsonObject.getString("ExpenseCategory");
                    final String ExpNumber = jsonObject.getString("ExpNumber");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ExpenseItems itemExpenseList = new ExpenseItems();
                            itemExpenseList.setTotalAmount(TotalAmount);
                            itemExpenseList.setExDate(ExDate);
                            itemExpenseList.setExpenseCategory(ExpenseCategory);
                            itemExpenseList.setExpNumber(ExpNumber);
                            expenseItemsArrayList.add(itemExpenseList);
                            if (expenseDetailsAdapter != null){
                                expenseDetailsAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(ExpensesDetailsActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return expenseItemsArrayList;
    }

    private class AsyncCallExpensesList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            String strExpCategoryId = params[1];
            expenseDetailsAdapter = new ExpenseDetailsAdapter( getExpensedata(strExpCategoryId),ExpensesDetailsActivity.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ExpensesDetailsActivity.this));
                    recyclerView.setAdapter(expenseDetailsAdapter);

                }

            });

            return routeId;
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_expenses_list:
                Intent intent = new Intent(this,AddExpenseActivity.class);
                intent.putExtra("CATEGORY_NAME",strExpCategoryName);
                intent.putExtra("CATEGORY_ID",strExpCategoryId);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isExpenseDetailsListRefreshed){
            AsyncCallExpensesList callInvoiceList = new AsyncCallExpensesList();
            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strExpCategoryId);

            isExpenseDetailsListRefreshed = false;
        }
    }
}
