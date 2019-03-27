package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.ExpenseListDetailsAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.model.DyanamicView;
import com.example.admin.demo.model.ExpenseDetailsListPojo;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private TextView textView,tv_add_new_item,tv_remove_items,tv_total_amount,tv_done_items;
    private ProgressBar pb_add_expense;
    private EditText et_expense_category,et_expense_date,et_reference_no,
            edt_item_name,edt_item_qty,edt_price_unit;
    private Spinner sp_payment_type;
    private RecyclerView recyclerView;
    private String strExpCategoryName,strExpCategoryId,strDate,strPaymentMode;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private int date_Year, date_Month, date_Day;
    private static final String[] PAYMENT_MODE = {"Cash", "Cheque"};
    private LinearLayout parentLinearLayout,linear_layout_add_expenses,ll_item_name,
            ll_item_qty,ll_item_price,ll_item_amount,ll_item_test;
    private ImageView iv_remove_items;
    private View rowView;
    private List<DyanamicView> dyanamicViewList =  new ArrayList<>();
    private EditText editTextItemName,editTextItemPrice,editItemQty;
    private TextView textViewAmount,textViewSpace,tv_add_expenseItemTotal;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private EditText et_add_expenseItemName,et_add_expenseItemQty,et_add_expenseItemPrice;
    private Button btn_cancel_expense,btn_add_expense;
    private SoapPrimitive resultString;
    private boolean isDetailsSaved = true;
    private String strInvoiceNo;
    private RecyclerView rv_expense_item_details;
    private ExpenseListDetailsAdapter adapter;
    private SessionManagement session;
    private ArrayList<ExpenseDetailsListPojo> expenseDetailsListPojoArrayList;
    private String compid = "", loginid = "";
    private CoordinatorLayout cl_addExpense;
    private EditText editTextTotalAmount;
    private Button btn_save_category;
    private ImageView iv_items_close_button;
    private boolean etItemQty,etItemPrice;
    private DecimalFormat df;
    private TextView tv_expense_qty,tv_expense_sub_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        toolbar = findViewById(R.id.toolbar_add_expenses);
        View view = toolbar.getRootView();
        textView = view.findViewById(R.id.app_name_nav_bar);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        linear_layout_add_expenses = findViewById(R.id.linear_layout_add_expenses);
        tv_add_new_item = findViewById(R.id.tv_add_new_item);
        tv_remove_items = findViewById(R.id.tv_remove_items);
        tv_done_items = findViewById(R.id.tv_done_items);
        cl_addExpense = findViewById(R.id.cl_addExpense);
        tv_expense_qty = findViewById(R.id.tv_expense_qty);
        tv_expense_sub_total = findViewById(R.id.tv_expense_sub_total);
        editTextTotalAmount = findViewById(R.id.editTextTotalAmount);
        rv_expense_item_details = findViewById(R.id.rv_expense_item_details);
        pb_add_expense = findViewById(R.id.pb_add_expense);
        et_reference_no = findViewById(R.id.et_reference_no);
        textView.setText("Expense");
        df = new DecimalFormat("#0.00");
        btn_save_category = findViewById(R.id.btn_save_category);
        btn_save_category.setOnClickListener(this);
        Functions.setToolBar(AddExpenseActivity.this, toolbar, "Expense List", true);
        initialization();
        generateInvoiceNo();
        session = new SessionManagement(this);
        // Getting saved details of session
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
    }

    private void generateInvoiceNo() {
        if (isDetailsSaved) {
            Random rand = new Random();
            int randInt = rand.nextInt(999999) + 1;
            strInvoiceNo = "EXP" + String.valueOf(randInt);
            isDetailsSaved = false;
        }
    }

    void initialization()
    {
        et_expense_date = findViewById(R.id.et_expense_date);
        et_expense_category = findViewById(R.id.et_expense_category);
        sp_payment_type = findViewById(R.id.sp_payment_type);
        et_reference_no = findViewById(R.id.et_reference_no);
        Intent intent = getIntent();
        if (intent !=null){
            strExpCategoryName = intent.getStringExtra("CATEGORY_NAME");
            strExpCategoryId= intent.getStringExtra("CATEGORY_ID");
            et_expense_category.setText(strExpCategoryName);
        }
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_expense_date.setText(simpleDateFormat.format(calendar.getTime()));
        et_expense_date.setOnClickListener(this);
        sp_payment_type.setOnItemSelectedListener(this);
        tv_remove_items.setOnClickListener(this);
        tv_add_new_item.setOnClickListener(this);
        tv_done_items.setOnClickListener(this);
        paymentModeSpinner();
    }

    // getting date picker dialog
    private void getDateFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                strDate = dayOfMonth + "/" + "0" + month
                        + "/" + year;
                et_expense_date.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_expense_date:
                getDateFrom();
                break;
            case R.id.ivRemoveItems:
                onDelete(v);
                break;
            case R.id.tv_add_new_item:
                //onAddField();
                addExpenseListDetails();

                break;
            case R.id.tv_done_items:
                addData();
                break;
            case R.id.tv_remove_items:
                fetchData();
                break;
            case R.id.btn_add_expense:
                addExpenseDetails();
                alertDialog.dismiss();
                break;
            case R.id.btn_cancel_expense:
                addExpenseDetails();
                break;
            case R.id.btn_save_category:
                submitSalesInvoice();
                break;
            case R.id.iv_items_close_button:
                alertDialog.dismiss();
                break;

        }
    }

    private void addExpenseListDetails() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense_details, null);
        alertDialogBuilder = new AlertDialog.Builder(this);
        et_add_expenseItemName = promptsView.findViewById(R.id.et_add_expenseItemName);
        et_add_expenseItemQty = promptsView.findViewById(R.id.et_add_expenseItemQty);
        et_add_expenseItemPrice = promptsView.findViewById(R.id.et_add_expenseItemPrice);
        tv_add_expenseItemTotal = promptsView.findViewById(R.id.tv_add_expenseItemTotal);
        btn_add_expense = promptsView.findViewById(R.id.btn_add_expense);
        btn_cancel_expense = promptsView.findViewById(R.id.btn_cancel_expense);
        iv_items_close_button = promptsView.findViewById(R.id.iv_items_close_button);
        btn_add_expense.setOnClickListener(this);
        btn_cancel_expense.setOnClickListener(this);
        iv_items_close_button.setOnClickListener(this);
        et_add_expenseItemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etItemPrice) {

                    if (!s.toString().equals("")) {
                        if (count == 0) {
                            tv_add_expenseItemTotal.setText("0");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etItemPrice) {
                    try {
                        //et_discount_percentage_item.removeTextChangedListener(generalTextWatcher);
                        if (!s.toString().equals("")){
                            double price = 0,qty = 0;
                            price = Double.parseDouble(s.toString());
                            qty = Double.parseDouble(et_add_expenseItemQty.getText().toString());

                            tv_add_expenseItemTotal.setText(df.format(price * qty));

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        et_add_expenseItemPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etItemPrice = hasFocus;

            }
        });
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
    }

    private void fetchData() {
        String[] strings = new String[dyanamicViewList.size()];
        for(int i=0; i < dyanamicViewList.size(); i++){
            strings[i] = dyanamicViewList.get(i).getEdt_itemName().getText().toString();
            //Toast.makeText(this, strings[i] +"", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, dyanamicViewList + "", Toast.LENGTH_SHORT).show();
            Log.d("dyanamicViewList",dyanamicViewList + "");
        }
    }

    private void paymentModeSpinner(){
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, PAYMENT_MODE){
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
        sp_payment_type.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId())
        {
            case R.id.sp_payment_type:
                strPaymentMode = String.valueOf(spinner.getAdapter().getItem(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onAddField() {
        //dyanamicViewList = new ArrayList<>();
        DyanamicView dyanamicView = new DyanamicView();
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_add_expenses);
        layout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 1; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            editTextItemName = new EditText(this);
            editTextItemPrice = new EditText(this);
            editItemQty = new EditText(this);
            textViewSpace = new TextView(this);
            textViewAmount = new TextView(this);
            iv_remove_items = new ImageView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            linear_layout_add_expenses.setOrientation(LinearLayout.VERTICAL);
            lp.weight = 1;
            editTextItemName.setLayoutParams(lp);
            editTextItemName.setId(R.id.edtItemName);
            editTextItemPrice.setLayoutParams(lp);
            editTextItemPrice.setId(R.id.edtItemPrice);
            editItemQty.setLayoutParams(lp);
            editItemQty.setId(R.id.edtItemQty);
            textViewAmount.setLayoutParams(lp);
            textViewAmount.setId(R.id.tvItemAmount);
//            editTextItemName.setText("00");
//            editTextItemPrice.setText("00");
//            editItemQty.setText("00");
            textViewAmount.setText("00");
            linearLayout.setLayoutParams(lp);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            lp1.weight = 0.5f;
            iv_remove_items.setLayoutParams(lp1);
            iv_remove_items.setId(R.id.ivRemoveItems);
            iv_remove_items.setPadding(0,12,0,12);
            iv_remove_items.setImageResource(R.drawable.remove_items);
            iv_remove_items.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            textViewSpace.setLayoutParams(lp1);
            row.addView(editTextItemName);
            row.addView(editItemQty);
            row.addView(editTextItemPrice);
            row.addView(textViewAmount);
            row.addView(linearLayout);
            linearLayout.addView(textViewSpace);
            linearLayout.addView(iv_remove_items);
            iv_remove_items.setOnClickListener(this);
            dyanamicView.setEdt_itemName(editTextItemName);
            dyanamicView.setEdt_itemQty(editItemQty);
            dyanamicView.setEdt_itemPrice(editTextItemPrice);
            dyanamicView.setTv_amount(textViewAmount);
            dyanamicViewList.add(dyanamicView);
            layout.addView(row);
        }
    }

    public void onDelete(View v) {
        linear_layout_add_expenses.removeView((View) v.getParent());
    }
    void addData(){
        for (int i = 0; i < linear_layout_add_expenses.getChildCount(); i++) {

            //Toast.makeText(this, editTextItemName.getText().toString() +"", Toast.LENGTH_SHORT).show();
        }

    }

    private void addExpenseDetails() {
        if (TextUtils.isEmpty(et_add_expenseItemName.getText().toString())) {
            Toast.makeText(this, "Please Select Product", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_add_expenseItemQty.getText().toString())) {
            Toast.makeText(this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_add_expenseItemPrice.getText().toString())) {
            Toast.makeText(this, "Please Enter price per unit", Toast.LENGTH_SHORT).show();
        }  else {
            AsyncCallExpenseItem callSalesInvoiceItem = new AsyncCallExpenseItem();
            callSalesInvoiceItem.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                    et_add_expenseItemName.getText().toString(),
                    et_add_expenseItemQty.getText().toString(),
                    et_add_expenseItemPrice.getText().toString(),
                    tv_add_expenseItemTotal.getText().toString());


        }
    }

    private class AsyncCallExpenseItem extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_add_expense.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String salesInvoiceId = "0";
            String id = params[0];
            String name = params[1];
            String itemQty = params[2];
            String price = params[3];
            String subtotal = params[4];
            saveExpenseItemServer(name, itemQty, price, subtotal);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_add_expense.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(AddExpenseActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddExpenseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveExpenseItemServer(String name, String itemQty,
                                       String unitPrice, String totAmount) {

        String SOAP_ACTION = "http://tempuri.org/SaveExpenseItems";
        String METHOD_NAME = "SaveExpenseItems";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("itemName", name);
            Request.addProperty("unitPrice", unitPrice);
            Request.addProperty("totAmount", totAmount);
            Request.addProperty("expId", "0");
            Request.addProperty("qty", itemQty);
            Request.addProperty("expNumber", strInvoiceNo);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (responseJSON.equals(null)) {
                Toast.makeText(this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else if (responseJSON.equals("-2")) {
                Toast.makeText(this, "Duplicate values", Toast.LENGTH_SHORT).show();
            } else {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AsyncCallExpenseItemList callOrderItemListList = new AsyncCallExpenseItemList();
                        callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
                        Toast.makeText(AddExpenseActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        clearAllItemEditText();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
        return "success";
    }

    private void clearAllItemEditText() {
        et_add_expenseItemName.setText("");
        et_add_expenseItemQty.setText("");
        et_add_expenseItemPrice.setText("");
        tv_add_expenseItemTotal.setText("0.00");
    }

    // attaching order items to adapter
    private class AsyncCallExpenseItemList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            String strInvoiceNo = params[1];
            adapter = new ExpenseListDetailsAdapter(getExpenseDetails(strInvoiceNo), AddExpenseActivity.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_expense_item_details.setNestedScrollingEnabled(false);
                    rv_expense_item_details.setHasFixedSize(true);
                    rv_expense_item_details.setLayoutManager(new LinearLayoutManager(AddExpenseActivity.this));
                    rv_expense_item_details.setAdapter(adapter);

                }

            });
            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    double total = 0, qty = 0;
                    for (int j = 0; j < expenseDetailsListPojoArrayList.size(); j++) {
                        total = total + Double.parseDouble(expenseDetailsListPojoArrayList.get(j).getTotalitemAmount());
                        qty = qty + Double.parseDouble(expenseDetailsListPojoArrayList.get(j).getQuantity());
                        tv_expense_sub_total.setText(df.format(total));
                        tv_expense_qty.setText(df.format(qty));
                        editTextTotalAmount.setText(df.format(total));
                    }
                }
            });
            if (result.equals("success")) {
                Toast.makeText(AddExpenseActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddExpenseActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public ArrayList<ExpenseDetailsListPojo> getExpenseDetails(String expNo) {
        expenseDetailsListPojoArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListAllExpensesItemByExpNo";
        String METHOD_NAME = "ListAllExpensesItemByExpNo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("expNo", expNo);
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
                    final String UnitPrice = jsonObject.getString("UnitPrice");
                    final String ItemName = jsonObject.getString("ItemName");
                    final String Quantity = jsonObject.getString("Quantity");
                    final String TotalitemAmount = jsonObject.getString("TotalitemAmount");

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ExpenseDetailsListPojo expenseDetailsListPojo = new ExpenseDetailsListPojo();
                            expenseDetailsListPojo.setItemName(ItemName);
                            expenseDetailsListPojo.setUnitPrice(UnitPrice);
                            expenseDetailsListPojo.setQuantity(Quantity);
                            expenseDetailsListPojo.setTotalitemAmount(TotalitemAmount);
                            expenseDetailsListPojoArrayList.add(expenseDetailsListPojo);
                            if (adapter
                                    != null) {
                                adapter
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
        return expenseDetailsListPojoArrayList;

    }

    // Save sales invoice server call
    public String saveExpenseInvoice(String compId, String loginId, String catId, String expDate,
                                   String invNo,String mode,
                                   String paidAmt,  String reference) {
        String SOAP_ACTION = "http://tempuri.org/SaveExpense";
        String METHOD_NAME = "SaveExpense";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("compId", compId);
            Request.addProperty("loginId", loginId);
            Request.addProperty("catId", catId);
            Request.addProperty("expDate", expDate);
            Request.addProperty("paidAmt", paidAmt);
            Request.addProperty("mode", mode);
            Request.addProperty("reference", reference);
            Request.addProperty("invNo", invNo);
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
                        Toast.makeText(AddExpenseActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        Functions.hideKeyboard(AddExpenseActivity.this);
                        clearAllEditText();
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
        strInvoiceNo = "";
        if (expenseDetailsListPojoArrayList != null){
            expenseDetailsListPojoArrayList.clear();
            AsyncCallExpenseItemList callOrderItemListList = new AsyncCallExpenseItemList();
            callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
        }
        tv_expense_sub_total.setText("");
        tv_expense_qty.setText("");
        editTextTotalAmount.setText("");
        et_reference_no.setText("");

    }

    private class AsyncCallExpenseInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String invDate = params[1];
            String totalAmount = params[2];
            String reference = params[3];

            saveExpenseInvoice(compid, loginid, strExpCategoryId,invDate,strInvoiceNo,
                    strPaymentMode,totalAmount, reference);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            isDetailsSaved = true;
            strInvoiceNo = "";
            generateInvoiceNo();
            ExpensesDetailsActivity.isExpenseDetailsListRefreshed = true;
            if (result.equals("success")) {
                startActivity(new Intent(AddExpenseActivity.this,PartyListActivity.class));
                PartyInvoiceActivity.isRefreshed = true;
                Toast.makeText(AddExpenseActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddExpenseActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void submitSalesInvoice() {
        if (TextUtils.isEmpty(et_expense_category.getText().toString())) {
            Snackbar.make(cl_addExpense, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_expense_date.getText().toString())) {
            Snackbar.make(cl_addExpense, "Please Select Date", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(editTextTotalAmount.getText().toString())) {
            Snackbar.make(cl_addExpense, "Please Enter party balance", Snackbar.LENGTH_SHORT).show();

        } else {
            Date date = new Date();
            String selectedDate = et_expense_date.getText().toString();
            try {
                date = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(et_reference_no.getText().toString())){
                et_reference_no.setText("0");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);
            AsyncCallExpenseInvoice callSalesInvoice = new AsyncCallExpenseInvoice();
            callSalesInvoice.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                    formatedDate, editTextTotalAmount.getText().toString(),
                    et_reference_no.getText().toString());
        }
    }

}
