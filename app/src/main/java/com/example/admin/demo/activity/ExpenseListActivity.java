package com.example.admin.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.ExpenseListAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.item.ItemExpenseList;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ExpenseListActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fab_main;
    final Context context = this;
    private RecyclerView recycler_view;
    private Toolbar toolbar_expense_list;
    private TextView appname;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private EditText edt_expenses_name;
    private Button btn_expenses_cancel,btn_expenses_save;
    private TextView tv_add,tv_cancel;
    private SoapPrimitive resultString;
    private ArrayList<ItemExpenseList> itemExpenseListArrayList;
    private ProgressBar pb_expenses_list;
    private ExpenseListAdapter expenseListAdapter;
    public static boolean isExpensesRefreshed = false;
    private LinearLayout parentLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);
        toolbar_expense_list = findViewById(R.id.toolbar_expense_list);
        View view = toolbar_expense_list.getRootView();
        appname = view.findViewById(R.id.app_name_nav_bar);
        appname.setText("Expense List");
        pb_expenses_list = findViewById(R.id.pb_expenses_list);
        Functions.setToolBar(ExpenseListActivity.this, toolbar_expense_list, "Expense List", true);
        recycler_view = findViewById(R.id.Expenselist_recycler_view);
        fab_main = findViewById(R.id.fab_add_expenses);
        fab_main.setOnClickListener(this);
        AsyncCallExpensesList callInvoiceList = new AsyncCallExpensesList();
        callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_expenses:
                DialogAddExpenses();
                break;
        }
    }
    public void DialogAddExpenses() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialogue_add_expenses, null);
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
        tv_add = promptsView.findViewById(R.id.btn_expenses_save);
        tv_cancel = promptsView.findViewById(R.id.btn_expenses_cancel);
        edt_expenses_name = promptsView.findViewById(R.id.et_expense_category);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_expenses_name.getText().toString())) {
                    Toast.makeText(ExpenseListActivity.this, "Please Enter Category", Toast.LENGTH_SHORT).show();
                } else {
                    AsyncCallAddExpenseCategory asyncCallWS = new AsyncCallAddExpenseCategory();
                    asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edt_expenses_name.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private class AsyncCallExpensesList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_expenses_list.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            expenseListAdapter = new ExpenseListAdapter(ExpenseListActivity.this,getExpensesCategory());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    recycler_view.setNestedScrollingEnabled(false);
                    recycler_view.setHasFixedSize(true);
                    recycler_view.setLayoutManager(new LinearLayoutManager(ExpenseListActivity.this));
                    recycler_view.setAdapter(expenseListAdapter);

                }

            });

            return routeId;
        }
        @Override
        protected void onPostExecute(String result) {
            pb_expenses_list.setVisibility(View.GONE);
        }
    }
    public ArrayList<ItemExpenseList> getExpensesCategory(){
        itemExpenseListArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListExpenseCategory";
        String METHOD_NAME = "ListExpenseCategory";
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
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final String ExpenseCategoryId = jsonObject.getString("ExpenseCategoryId");
                    final String ExpenseCategory = jsonObject.getString("ExpenseCategory");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ItemExpenseList itemExpenseList = new ItemExpenseList(ExpenseCategory,ExpenseCategoryId);
                            itemExpenseListArrayList.add(itemExpenseList);
                            if (expenseListAdapter != null){
                                expenseListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            } else {
                Toast.makeText(ExpenseListActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itemExpenseListArrayList;
    }

//    @Override
//    public void onResume()
//    {  // After a pause OR at startup
//        super.onResume();
//        if (isExpensesRefreshed){
//            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
//            AsyncCallExpensesList callInvoiceList = new AsyncCallExpensesList();
//            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
//            isExpensesRefreshed = false;
//        }
//    }

    private class AsyncCallAddExpenseCategory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_expenses_list.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String expCat = params[0];
            AddExpense(expCat);
            return expCat;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_expenses_list.setVisibility(View.GONE);
        }

    }

    private String AddExpense(String expCat){

        String SOAP_ACTION = "http://tempuri.org/SaveExpenseCategory";
        String METHOD_NAME = "SaveExpenseCategory";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("expCat", expCat);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (responseJSON.equals(null)) {
                Toast.makeText(ExpenseListActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExpenseListActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        AsyncCallExpensesList asyncCallWS = new AsyncCallExpensesList();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        alertDialog.dismiss();
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
