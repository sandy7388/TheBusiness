package com.example.admin.demo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.ProductForSaleAdapter;
import com.example.admin.demo.adapter.TaxAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.ProductDetailsPojo;
import com.example.admin.demo.model.TaxListPojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity implements
        View.OnClickListener,CallBack,ProductIntefrace {
    private ArrayList<ProductDetailsPojo> productDetailsPojos;
    private ProductForSaleAdapter productForSaleAdapter;
    private SoapPrimitive resultString;
    private Toolbar toolbar_additem;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private ProgressBar pb_category_list;
    private RecyclerView rv_category;
    private TaxAdapter taxAdapter;
    private EditText et_item_tax_name,et_product_name_item,et_qty_item,
            et_free_qty_item,et_price_per_unit_item,et_discount_percentage_item,
            et_discount_amount_item;
    private String strTaxId;
    private CardView card_am,card_pm;
    private boolean card_status_am = false;
    private boolean card_status_pm = false;
    private String strTimeStatus;
    private TextView tv_add_sales_item,appNameNavBar,tv_dialog_add_customer,
            tv_am,tv_pm,tv_subtotal_item,tv_select_tax_item,tv_total_amount_item;
    private Button btn_cancel_add_item,btn_save_add_item;
    private DecimalFormat df;
    private SessionManagement session;
    private String compid = "", loginid = "";
    private String strProductId,strProductName,strPricePerUnit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        initView();
        Functions.setToolBar(AddItemActivity.this, toolbar_additem, "Add Item", true);
        appNameNavBar.setText("Add Item");
        AddItemActivity.this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
    }

    private void initView() {
        df = new DecimalFormat("#0.00");
        toolbar_additem = (Toolbar) findViewById(R.id.toolbar_additem);
        View view = toolbar_additem.getRootView();
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        et_item_tax_name = findViewById(R.id.et_item_tax_name);
        tv_am = findViewById(R.id.tv_am);
        tv_pm = findViewById(R.id.tv_pm);
        card_am = findViewById(R.id.card_am);
        card_pm = findViewById(R.id.card_pm);
        et_product_name_item = findViewById(R.id.et_product_name_item);
        btn_cancel_add_item = findViewById(R.id.btn_add_item);
        et_qty_item = findViewById(R.id.et_qty_item);
        et_free_qty_item = findViewById(R.id.et_free_qty_item);
        et_price_per_unit_item = findViewById(R.id.et_price_per_unit_item);
        tv_subtotal_item = findViewById(R.id.tv_subtotal_item);
        et_discount_percentage_item = findViewById(R.id.et_discount_percentage_item);
        et_discount_amount_item = findViewById(R.id.et_discount_amount_item);
        tv_select_tax_item = findViewById(R.id.tv_select_tax_item);
        tv_total_amount_item = findViewById(R.id.tv_total_amount_item);
        btn_save_add_item = findViewById(R.id.btn_save_and_new_item);
        et_discount_percentage_item.addTextChangedListener(generalTextWatcher);
        et_discount_amount_item.addTextChangedListener(generalTextWatcher);
        et_qty_item.addTextChangedListener(generalTextWatcher);
        et_product_name_item.setOnClickListener(this);
        et_item_tax_name.setOnClickListener(this);
        card_pm.setOnClickListener(this);
        card_am.setOnClickListener(this);
        cardAM();
        btn_save_add_item.setOnClickListener(this);
        btn_cancel_add_item.setOnClickListener(this);

    }

    public TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (et_qty_item.getText().hashCode() == s.hashCode()) {
                try {
                    double qty = Double.parseDouble(s.toString());
                    double pricePerUnit = Double.parseDouble(et_price_per_unit_item.getText().toString());
                    tv_subtotal_item.setText(String.valueOf(df.format(qty * pricePerUnit)));
                    //discount_amount = Double.parseDouble(s.toString());
//                    percentage = (discount_amount * 100)/totalAmount;
//                    et_discount_percentage.setText(String.valueOf(df.format(discount_amount)));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    private void cardAM() {
        card_status_pm = false;
        card_status_am = true;
        strTimeStatus = tv_am.getText().toString();
        card_am.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        card_pm.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        tv_am.setTextColor(getResources().getColor(android.R.color.white));
        tv_pm.setTextColor(getResources().getColor(R.color.colorPrimary));
        Toast.makeText(this, strTimeStatus, Toast.LENGTH_SHORT).show();
    }

    private void cardPM() {
        card_status_pm = true;
        card_status_am = false;
        strTimeStatus = tv_pm.getText().toString();
        card_am.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_pm.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_am.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_pm.setTextColor(getResources().getColor(android.R.color.white));
        Toast.makeText(this, strTimeStatus, Toast.LENGTH_SHORT).show();
    }
    public ArrayList<TaxListPojo> getTax() {
        final ArrayList<TaxListPojo> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListTax";
        String METHOD_NAME = "ListTax";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
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
                    final String strTaxName = jarray.getJSONObject(i).getString("TaxRateName");
                    final String strTaxId = jarray.getJSONObject(i).getString("TaxId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            allItems.add(new TaxListPojo(strTaxName, strTaxId));
                            if (taxAdapter
                                    != null) {
                                taxAdapter
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
        return allItems;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_item_tax_name:
                chooseCategory("tax");
                break;
            case R.id.card_am:
                cardAM();
                break;
            case R.id.card_pm:
                cardPM();
                break;
        }
    }

    @Override
    public void AddParty(String str_name, String type, String id) {
        et_item_tax_name.setText(str_name);
        strTaxId = id;
        alertDialog.dismiss();
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {

    }

    @Override
    public void onProductSelected(String productId, String productName,
                                  String pricePerUnit,String taxId) {
            strProductId = productId;
            strProductName = productName;
            strPricePerUnit = pricePerUnit;
            et_product_name_item.setText(strProductName);
            et_price_per_unit_item.setText(strPricePerUnit);

        alertDialog.dismiss();


    }

    public class AsyncTaxCall extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            pb_category_list.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            if (type.equals("tax"))
            {
                taxAdapter = new TaxAdapter(getTax(),AddItemActivity.this);
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        rv_category.setNestedScrollingEnabled(false);
                        rv_category.setHasFixedSize(true);
                        rv_category.setLayoutManager(new LinearLayoutManager(AddItemActivity.this));
                        rv_category.setAdapter(taxAdapter);
                    }
                });
            }
            else if (type.equals("product")) {
                productForSaleAdapter = new ProductForSaleAdapter(getProduct(), AddItemActivity.this);
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        rv_category.setNestedScrollingEnabled(false);
                        rv_category.setHasFixedSize(true);
                        rv_category.setLayoutManager(new LinearLayoutManager(AddItemActivity.this));
                        rv_category.setAdapter(productForSaleAdapter);
                    }
                });
            }
            return type;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_category_list.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    void chooseCategory(String type){
        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_category_name, null);
        alertDialogBuilder = new AlertDialog.Builder(this);
        rv_category = (RecyclerView) promptsView.findViewById(R.id.rv_category);
        pb_category_list = (ProgressBar) promptsView.findViewById(R.id.pb_category_list);
        alertDialogBuilder.setView(promptsView);
        alertDialog = alertDialogBuilder.create();
        if (type.equals("tax")){
            AsyncTaxCall asyncCallWS = new AsyncTaxCall();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"tax");
        }

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
    private ArrayList<ProductDetailsPojo> getProduct(){
        productDetailsPojos = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListProduct";
        String METHOD_NAME = "ListProduct";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("CompId", compid);
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
                    final String ProductName = jsonObject.getString("ProductName");
                    final String ProductId = jsonObject.getString("ProductId");
                    final String PricePerUnit = jsonObject.getString("PricePerUnit");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProductDetailsPojo productDetailsPojo = new ProductDetailsPojo();
                            productDetailsPojo.setProductName(ProductName);
                            productDetailsPojo.setProductId(ProductId);
                            productDetailsPojo.setPricePerUnit(PricePerUnit);
                            productDetailsPojos.add(productDetailsPojo);
                            if (productForSaleAdapter
                                    != null) {
                                productForSaleAdapter
                                        .notifyDataSetChanged();
                            }
                        }
                    });
                }

            } else {
                Toast.makeText(AddItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return productDetailsPojos;
    }

}
