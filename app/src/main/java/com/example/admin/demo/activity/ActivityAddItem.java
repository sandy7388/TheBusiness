package com.example.admin.demo.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.CategoryAdapter;
import com.example.admin.demo.adapter.ItemsTabAdapter;
import com.example.admin.demo.adapter.ProductItemsAdapter;
import com.example.admin.demo.adapter.SpinnerCustomAdapter;
import com.example.admin.demo.adapter.TaxAdapter;
import com.example.admin.demo.adapter.UnitAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.RefreshView;
import com.example.admin.demo.model.ListTaxGroup;
import com.example.admin.demo.model.ListTaxGroupPojo;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.TaxList;
import com.example.admin.demo.model.UnitPojo;
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

public class ActivityAddItem extends AppCompatActivity implements View.OnClickListener,
        CallBack, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private RadioButton rb_inclusive,rb_exclusive;
    private RadioGroup rg_all_tax;
    private Toolbar toolbar_add_item_list;
    private ImageView iv_hsn_sac_code;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private String[] category,unit;
    private String strTaxInclusive;
    private CoordinatorLayout cl_add_item;
    private EditText et_category_name,et_item_name,et_barcode,et_hsn_sac_code,et_sale_price,
            et_purchase_price,et_opening_stock,et_opening_stock_date,et_price_per_unit,
            et_minimum_stack_qty,et_serial_number,et_unit_name,et_tax_name,et_short_name;
    private Button btn_save_item,btn_manage_unit, btn_clear_item,btn_delete_item,btn_edit_item;
    private AlertDialog alertDialog;
    private SoapPrimitive resultString;
    private String strCategoryId,strCategoryName,strItemName,strItemCode,strHSNCode,strSalePrice,
            strPurchasePrice, strOpeningStock,strOpeningDate,strPricePerUnit,strMinStockQty,
            strSerialNumber,strUnitName,strUnitId,strTaxName,strTaxId,strShortName,
            strLoginId,strCompanyId,strSpinnerItem,strSpinnerItemId,strSpinnerItemRate,
            strConversionId,strProductId;
    private SessionManagement session;
    private Date date;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private ProgressBar pb_add_product;
    private Spinner sp_taxList,sp_category,sp_unit;
    private ArrayList<ListTaxGroupPojo> listTaxGroupPojoArrayList;
    private ArrayList<ListTaxGroup> listTaxGroupArrayList;
    private DecimalFormat df;
    private double groupTaxRate;
    private ArrayList<ProductCategory> productCategoryArrayList;
    private ArrayList<UnitPojo> unitPojoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_list);
        toolbar_add_item_list = findViewById(R.id.toolbar_add_item_list);
        View view = toolbar_add_item_list.getRootView();
        TextView tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Add Item");
        Functions.setToolBar(ActivityAddItem.this, toolbar_add_item_list, "Add Item", true);
        initialization();
        onClickListener();
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strLoginId = user.get(SessionManagement.KEY_LOGIN_ID);
            strCompanyId = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_opening_stock_date.setText(simpleDateFormat.format(calendar.getTime()));
        rg_all_tax.check(R.id.rb_exclusive);
        strTaxInclusive = rb_exclusive.getText().toString();
        if (ProductItemsAdapter.isItemsEdit){
            strProductId = getIntent().getStringExtra("PRODUCT_ID");
            new AsyncCallItemsDetails().execute(strProductId);
            btn_edit_item.setVisibility(View.VISIBLE);
            btn_delete_item.setVisibility(View.VISIBLE);
            btn_clear_item.setVisibility(View.GONE);
            btn_save_item.setVisibility(View.GONE);

            sp_category.setEnabled(false);
            et_item_name.setEnabled(false);
            et_short_name.setEnabled(false);
            sp_unit.setEnabled(false);
            btn_manage_unit.setEnabled(false);
            et_barcode.setEnabled(false);
            et_hsn_sac_code.setEnabled(false);
            et_sale_price.setEnabled(false);
            et_purchase_price.setEnabled(false);
            sp_taxList.setEnabled(false);
            rb_exclusive.setEnabled(false);
            rb_inclusive.setEnabled(false);
            et_opening_stock.setEnabled(false);
            et_opening_stock_date.setEnabled(false);
            et_minimum_stack_qty.setEnabled(false);
        }
    }

    void initialization() {
        et_category_name = findViewById(R.id.et_category_name);
        et_item_name = findViewById(R.id.et_item_name);
        et_barcode = findViewById(R.id.et_barcode);
        et_hsn_sac_code = findViewById(R.id.et_hsn_sac_code);
        iv_hsn_sac_code = findViewById(R.id.iv_hsn_sac_code);
        et_sale_price = findViewById(R.id.et_sale_price);
        rb_inclusive = findViewById(R.id.rb_inclusive);
        rb_exclusive = findViewById(R.id.rb_exclusive);
        rg_all_tax = findViewById(R.id.rg_all_tax);
        et_purchase_price = findViewById(R.id.et_purchase_price);
        et_opening_stock = findViewById(R.id.et_opening_stock);
        et_opening_stock_date = findViewById(R.id.et_opening_stock_date);
        et_price_per_unit = findViewById(R.id.et_price_per_unit);
        et_minimum_stack_qty = findViewById(R.id.et_minimum_stack_qty);
        et_serial_number = findViewById(R.id.et_serial_number);
        btn_save_item = findViewById(R.id.btn_save_item);
        btn_delete_item = findViewById(R.id.btn_delete_item);
        btn_edit_item = findViewById(R.id.btn_edit_item);
        cl_add_item = findViewById(R.id.cl_add_item);
        et_unit_name = findViewById(R.id.et_unit_name);
        btn_manage_unit = findViewById(R.id.btn_manage_unit);
        btn_clear_item = findViewById(R.id.btn_clear_item);
        et_tax_name = findViewById(R.id.et_tax_name);
        et_short_name = findViewById(R.id.et_short_name);
        pb_add_product = findViewById(R.id.pb_add_product);
        sp_taxList = findViewById(R.id.sp_tax_list);
        sp_category = findViewById(R.id.sp_category);
        sp_unit = findViewById(R.id.sp_unit);
        AsyncCallTaxGroupList asyncCallTaxGroupList = new AsyncCallTaxGroupList();
        asyncCallTaxGroupList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        df = new DecimalFormat("#0.00");
        new AsyncCallCategory().execute("0");
        new AsyncCallUnit().execute("0");
    }

    void onClickListener(){
        sp_taxList.setOnItemSelectedListener(this);
        sp_category.setOnItemSelectedListener(this);
        sp_unit.setOnItemSelectedListener(this);
        et_opening_stock_date.setOnClickListener(this);
        btn_save_item.setOnClickListener(this);
        btn_clear_item.setOnClickListener(this);
        btn_manage_unit.setOnClickListener(this);
        rg_all_tax.setOnCheckedChangeListener(this);
        btn_delete_item.setOnClickListener(this);
        btn_edit_item.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            if (intent !=null){
                strConversionId = intent.getStringExtra("CONVERSION_ID");
                Toast.makeText(this, strConversionId+"", Toast.LENGTH_SHORT).show();
            }
            else {
                strConversionId = "0";
                Toast.makeText(this, strConversionId+"", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_opening_stock_date:
                getDateFrom();
                break;

            case R.id.btn_save_item:
                submitCategory();
                break;
            case R.id.btn_manage_unit:
                Intent intent = new Intent(this,UnitActivity.class);
                if (strUnitName.equals("Select unit")){
                    Toast.makeText(this, "Select unit", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("UNIT_NAME",strUnitName);
                    intent.putExtra("UNIT_ID",strUnitId);
                    startActivityForResult(intent,2);
                }

                break;
            case R.id.btn_clear_item:
                clearAllField();
                break;

            case R.id.btn_delete_item:
                new AsyncCallDeleteProduct().execute(strProductId);
                break;

            case R.id.btn_edit_item:
                editAllItems();
                break;

        }

    }

    private void editAllItems() {
        sp_category.setEnabled(true);
        et_item_name.setEnabled(true);
        et_short_name.setEnabled(true);
        sp_unit.setEnabled(true);
        btn_manage_unit.setEnabled(true);
        et_barcode.setEnabled(true);
        et_hsn_sac_code.setEnabled(true);
        et_sale_price.setEnabled(true);
        et_purchase_price.setEnabled(true);
        sp_taxList.setEnabled(true);
        rb_exclusive.setEnabled(true);
        rb_inclusive.setEnabled(true);
        et_opening_stock.setEnabled(true);
        et_opening_stock_date.setEnabled(true);
        et_minimum_stack_qty.setEnabled(true);
        btn_edit_item.setVisibility(View.GONE);
        btn_save_item.setVisibility(View.VISIBLE);
    }

    private void clearAllField() {
        et_category_name.setText("");
        et_item_name.setText("");
        et_unit_name.setText("");
        et_barcode.setText("");
        et_hsn_sac_code.setText("");
        et_sale_price.setText("");
        //et_tax_name.setText("");
        if (rb_exclusive.isChecked()|| rb_inclusive.isChecked()){
            rb_exclusive.setChecked(true);
            rb_inclusive.setChecked(false);
        }
        //et_opening_stock_date.setText("");
        et_purchase_price.setText("");
        et_opening_stock.setText("");
        et_price_per_unit.setText("");
        et_minimum_stack_qty.setText("");
        et_serial_number.setText("");
        et_short_name.setText("");
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
                et_opening_stock_date.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    void submitCategory() {
        textGetter();
        if (strCategoryName.equals("Select Category")) {
            Snackbar.make(cl_add_item, "Please Select Category Name", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(strItemName)) {
            Snackbar.make(cl_add_item, "Please Enter Item Name", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strItemCode)) {
            Snackbar.make(cl_add_item, "Please Enter Item Code", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strHSNCode)) {
            Snackbar.make(cl_add_item, "Please Enter HSN/SAC Code", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strSalePrice)) {
            Snackbar.make(cl_add_item, "Please Enter Sales Price", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strPurchasePrice)) {
            Snackbar.make(cl_add_item, "Please Enter Purchase Price", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strOpeningStock)) {
            Snackbar.make(cl_add_item, "Please Enter Opening Stock", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strOpeningDate)) {
            Snackbar.make(cl_add_item, "Please Select Date", Snackbar.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(strMinStockQty)) {
            Snackbar.make(cl_add_item, "Please Enter Minimum Stock Quantity", Snackbar.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(strSpinnerItem)) {
            Snackbar.make(cl_add_item, "Please Select Tax", Snackbar.LENGTH_SHORT).show();

        } else if (strUnitName.equals("Select Unit")) {
            Snackbar.make(cl_add_item, "Please Select Unit", Snackbar.LENGTH_SHORT).show();

        } else if (strTaxInclusive == null){
            Snackbar.make(cl_add_item, "Please Select Tax is either inclusive or exclusive", Snackbar.LENGTH_SHORT).show();
        }
        else {
            date = new Date();
            try {
                date = simpleDateFormat.parse(strOpeningDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);
            if (TextUtils.isEmpty(et_price_per_unit.getText().toString())){
                et_price_per_unit.setText("0");
            }
            if (strConversionId == null){
                strConversionId = "0";
            }
            AsyncCallAddProduct addProduct = new AsyncCallAddProduct();
            addProduct.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,strItemName,
                    strShortName,strItemCode,strSalePrice,strPurchasePrice,
                    strOpeningStock,formatedDate,strPricePerUnit,strMinStockQty,
                    strSerialNumber,strTaxInclusive,strHSNCode);

        }
    }

    @Override
    public void AddParty(String str_name, String type, String id) {

        if (type.equals("category")){
            et_category_name.setText(str_name);
            strCategoryId = id;
            //Toast.makeText(this, "strCategoryId = " +strCategoryId, Toast.LENGTH_SHORT).show();
        }
        else if (type.equals("unit"))
        {
            et_unit_name.setText(str_name);
            strUnitId = id;
            //Toast.makeText(this,"strUnitId = " + strUnitId, Toast.LENGTH_SHORT).show();
        }
        else if (type.equals("tax"))
        {
            et_tax_name.setText(str_name);
            strTaxId = id;
            //Toast.makeText(this,"strTaxId = " + strTaxId, Toast.LENGTH_SHORT).show();
        }
        alertDialog.dismiss();
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton checkedRadioButton = group.findViewById(checkedId);
        boolean isChecked = checkedRadioButton.isChecked();
        switch (group.getId())
        {
            case R.id.rg_all_tax:
                if (checkedId == R.id.rb_inclusive){
                    strTaxInclusive = checkedRadioButton.getText().toString();
                    Toast.makeText(this, strTaxInclusive, Toast.LENGTH_SHORT).show();
                }
                else if (checkedId == R.id.rb_exclusive){
                    strTaxInclusive = checkedRadioButton.getText().toString();
                    Toast.makeText(this, strTaxInclusive, Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.sp_tax_list:
                strSpinnerItem = ((TextView)view.findViewById(R.id.tv_spinner_group_name)).getText().toString();
                strSpinnerItemRate = ((TextView)view.findViewById(R.id.tv_spinner_group_rate)).getText().toString();
                groupTaxRate = Double.parseDouble(strSpinnerItemRate);
                for (int i=0;i<listTaxGroupArrayList.size();i++){
                    if (strSpinnerItem.equals(listTaxGroupArrayList.get(i).getGroupName())){
                        strSpinnerItemId = listTaxGroupArrayList.get(i).getTaxGroupId();
                    }
                }

                break;

            case R.id.sp_category:
                strCategoryName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < productCategoryArrayList.size(); i++) {
                    if (strCategoryName.equals(productCategoryArrayList.get(i).getServiceName())) {
                        strCategoryId = productCategoryArrayList.get(i).getServiceId();
                    }
                }
                break;
            case R.id.sp_unit:
                strUnitName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < unitPojoArrayList.size(); i++) {
                    if (strUnitName.equals(unitPojoArrayList.get(i).getUnitName())) {
                        strUnitId = unitPojoArrayList.get(i).getUnitId();
                    }
                }
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void textGetter()
    {
        strItemName = et_item_name.getText().toString();
        strItemCode = et_barcode.getText().toString();
        strHSNCode = et_hsn_sac_code.getText().toString();
        strSalePrice = et_sale_price.getText().toString();
        strPurchasePrice = et_purchase_price.getText().toString();
        strOpeningStock = et_opening_stock.getText().toString();
        strOpeningDate = et_opening_stock_date.getText().toString();
        strPricePerUnit = et_price_per_unit.getText().toString();
        strMinStockQty = et_minimum_stack_qty.getText().toString();
        strSerialNumber = et_serial_number.getText().toString();
        strTaxName = et_tax_name.getText().toString();
        strShortName = et_short_name.getText().toString();
    }

    public String addProductData(String catId,String compId,String loginId,String product,
                               String shortName,String itemCode,String unitId,String salePrice,
                               String groupName,String purchasePrice,String openingStock,
                               String asOfDate,String pricePerUnit,String minimumStocQty,
                               String srNo,String isincludeTax,String hsnCode,String conversionId){

        String SOAP_ACTION,METHOD_NAME;

        if (ProductItemsAdapter.isItemsEdit){
            SOAP_ACTION = "http://tempuri.org/UpdateProduct";
            METHOD_NAME = "UpdateProduct";
        }
        else {
            SOAP_ACTION = "http://tempuri.org/SaveProduct";
            METHOD_NAME = "SaveProduct";
        }

        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("catId", catId);
            Request.addProperty("compId", compId);
            Request.addProperty("loginId", loginId);
            Request.addProperty("product", product);
            Request.addProperty("shortName", shortName);
            Request.addProperty("itemCode", itemCode);
            Request.addProperty("unitId", unitId);
            Request.addProperty("salePrice", salePrice);
            Request.addProperty("taxId", "0");
            Request.addProperty("purchasePrice", purchasePrice);
            Request.addProperty("openingStock", openingStock);
            Request.addProperty("asOfDate", asOfDate);
            Request.addProperty("pricePerUnit", "0");
            Request.addProperty("minimumStocQty", minimumStocQty);
            Request.addProperty("srNo", "0");
            Request.addProperty("groupName",groupName);
            if (isincludeTax.equals("Inclusive Tax")) {
                Request.addProperty("isincludeTax", "true");
            } else {
                Request.addProperty("isincludeTax", "false");
            }
            Request.addProperty("hsnCode", hsnCode);
            if (conversionId.equals("")){
                conversionId = "0";
            }
            Request.addProperty("conversionId", conversionId);
            if (ProductItemsAdapter.isItemsEdit){
                Request.addProperty("productId", strProductId);
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
                        if (ProductItemsAdapter.isItemsEdit){
                            Toast.makeText(ActivityAddItem.this, "Product Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ActivityAddItem.this, "Product Successfully Added", Toast.LENGTH_SHORT).show();
                        }

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

    private class  AsyncCallAddProduct extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pb_add_product.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String product = params[0];
            String shortName = params[1];
            String itemCode = params[2];
            String salePrice = params[3];
            String purchasePrice = params[4];
            String openingStock = params[5];
            String asOfDate = params[6];
            String pricePerUnit = params[7];
            String minimumStocQty = params[8];
            String srNo = params[9];
            String isincludeTax = params[10];
            String hsnCode = params[11];
            if (strConversionId == null){
                strConversionId = "0";
            }
            addProductData(strCategoryId, strCompanyId, strLoginId, product, shortName,
                    itemCode,strUnitId, salePrice, strSpinnerItem, purchasePrice, openingStock,
                    asOfDate, pricePerUnit, minimumStocQty, srNo, isincludeTax,hsnCode,strConversionId);
            return product;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_add_product.setVisibility(View.GONE);
            if (result.equals("success")) {
                pb_add_product.setVisibility(View.GONE);
                Toast.makeText(ActivityAddItem.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                pb_add_product.setVisibility(View.GONE);
                Toast.makeText(ActivityAddItem.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class AsyncCallTaxGroupList extends  AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            listTaxGroupPojoArrayList = new ArrayList<>();
            listTaxGroupArrayList = new ArrayList<>();
            final ArrayList<TaxList> taxListArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListTaxGroup";
            String METHOD_NAME = "ListTaxGroup";
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
                    JSONObject jsonObject = new JSONObject(responseJSON);
                    JSONArray jsonArray = jsonObject.getJSONArray("ListTaxGroup");
                    for (int j=0;j<jsonArray.length();j++){
                        JSONObject object = jsonArray.getJSONObject(j);
                        final String strTaxGroupId = object.getString("TaxGroupId");
                        final String strGroupName = object.getString("GroupName");
                        JSONArray array = object.getJSONArray("TaxList");
                        double rate = 0;
                        for (int k =0;k<array.length();k++){
                            JSONObject jsonObject1 = array.getJSONObject(k);
                            final String TaxId = jsonObject1.getString("TaxId");
                            final String TaxType = jsonObject1.getString("TaxType");
                            final String TaxRateName = jsonObject1.getString("TaxRateName");
                            final String TaxRate = jsonObject1.getString("TaxRate");
                            rate = rate +  Double.parseDouble(TaxRate);
                            final TaxList taxList = new TaxList();
                            taxList.setTaxId(TaxId);
                            taxList.setTaxRateName(TaxRateName);
                            taxList.setTaxRate(TaxRate);
                            taxList.setTaxType(TaxType);
                            taxListArrayList.add(taxList);
                        }
                        ListTaxGroup listTaxGroup = new ListTaxGroup();
                        listTaxGroup.setTaxGroupRate(String.valueOf(rate));
                        listTaxGroup.setGroupName(strGroupName);
                        listTaxGroup.setTaxGroupId(strTaxGroupId);
                        listTaxGroup.setTaxList(taxListArrayList);
                        listTaxGroupArrayList.add(listTaxGroup);
                    }
                } else {
                    Toast.makeText(ActivityAddItem.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Handler uiHandler1 = new Handler(Looper.getMainLooper());
            uiHandler1.post(new Runnable() {
                @Override
                public void run() {

                    SpinnerCustomAdapter dataAdapter = new SpinnerCustomAdapter(ActivityAddItem.this,
                            listTaxGroupArrayList);
                    //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_taxList.setAdapter(dataAdapter);
                }
            });
        }
    }

    public class AsyncCallCategory extends  AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            productCategoryArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllCategory";
            String METHOD_NAME = "ListAllCategory";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Request.addProperty("CompId", strCompanyId);
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
                        final String CategoryName = jarray.getJSONObject(i).getString("CategoryName");
                        final String CategoryId = jarray.getJSONObject(i).getString("CategoryId");
                        productCategoryArrayList.add(new ProductCategory(CategoryName, CategoryId));
                    }

                    productCategoryArrayList.add(0, new ProductCategory("Select Category", ""));
                    category = new String[productCategoryArrayList.size()];
                    for (int j = 0; j < productCategoryArrayList.size(); j++) {
                        category[j] = productCategoryArrayList.get(j).getServiceName();
                    }
                } else {
                    Toast.makeText(ActivityAddItem.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityAddItem.this,
                            android.R.layout.simple_spinner_item, category);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_category.setAdapter(dataAdapter);
                }
            });
        }
    }

    public class AsyncCallUnit extends  AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            unitPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListUnit";
            String METHOD_NAME = "ListUnit";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Request.addProperty("CompId", strCompanyId);
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
                        final String UnitName = jarray.getJSONObject(i).getString("UnitName");
                        final String UnitId = jarray.getJSONObject(i).getString("UnitId");
                        final String ShortName = jarray.getJSONObject(i).getString("ShortName");
                        unitPojoArrayList.add(new UnitPojo(UnitName, UnitId,ShortName));
                    }

                    unitPojoArrayList.add(0, new UnitPojo("Select Unit","", ""));
                    unit = new String[unitPojoArrayList.size()];
                    for (int j = 0; j < unitPojoArrayList.size(); j++) {
                        unit[j] = unitPojoArrayList.get(j).getUnitName();
                    }
                }
                else {
                    Toast.makeText(ActivityAddItem.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityAddItem.this,
                            android.R.layout.simple_spinner_item, unit);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_unit.setAdapter(dataAdapter);
                }
            });
        }
    }


    private class AsyncCallItemsDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/GetProductDetailsById";
            String METHOD_NAME = "GetProductDetailsById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("pId", id);
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
                                if (jarray.getJSONObject(0).getString("ProductName").equals("0")) {
                                    et_item_name.setText("");
                                } else {
                                    et_item_name.setText(jarray.getJSONObject(0).getString("ProductName"));
                                }
                                if (jarray.getJSONObject(0).getString("ShortName").equals("0")) {
                                    et_short_name.setText("");
                                }
                                else if (jarray.getJSONObject(0).getString("ShortName").equals("")){
                                    et_short_name.setText("");
                                }
                                else {
                                    et_short_name.setText(jarray.getJSONObject(0).getString("ShortName"));
                                }

                                if (jarray.getJSONObject(0).getString("ItemCode").equals("0")) {
                                    et_barcode.setText("");
                                } else {
                                    et_barcode.setText(jarray.getJSONObject(0).getString("ItemCode"));
                                }
                                //et_party_Current_Balance.setText(jarray.getJSONObject(0).getString("PartyCurrentBalance"));
                                Date date = new Date();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                date = simpleDateFormat.parse(jarray.getJSONObject(0).getString("AsOfDate"));
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                et_opening_stock_date.setText(simpleDateFormat1.format(date));

                                if (jarray.getJSONObject(0).getString("HSNCode").equals("0")) {
                                    et_hsn_sac_code.setText("");
                                } else {
                                    et_hsn_sac_code.setText(jarray.getJSONObject(0).getString("HSNCode"));
                                }

                                if (jarray.getJSONObject(0).getString("SalePrice").equals("0")) {
                                    et_sale_price.setText("");
                                } else {
                                    et_sale_price.setText(jarray.getJSONObject(0).getString("SalePrice"));
                                }

                                if (jarray.getJSONObject(0).getString("PurchasePrice").equals("0")) {
                                    et_purchase_price.setText("");
                                } else {
                                    et_purchase_price.setText(jarray.getJSONObject(0).getString("PurchasePrice"));
                                }

                                if (jarray.getJSONObject(0).getString("OpeningStock").equals("0")) {
                                    et_opening_stock.setText("");
                                } else {
                                    et_opening_stock.setText(jarray.getJSONObject(0).getString("OpeningStock"));
                                }
                                if (jarray.getJSONObject(0).getString("MinimumStockQty").equals("0")) {
                                    et_minimum_stack_qty.setText("");
                                } else {
                                    et_minimum_stack_qty.setText(jarray.getJSONObject(0).getString("MinimumStockQty"));
                                }
                                if (jarray.getJSONObject(0).getString("IsIncludeTax").equals("true")) {
                                    rb_inclusive.setChecked(true);
                                    rb_exclusive.setChecked(false);
                                } else {
                                    rb_inclusive.setChecked(false);
                                    rb_exclusive.setChecked(true);
                                }
                                strProductId = jarray.getJSONObject(0).getString("ProductId");
                                strCategoryId = jarray.getJSONObject(0).getString("CategoryId");
                                strUnitId = jarray.getJSONObject(0).getString("UnitId");
                                strTaxName = jarray.getJSONObject(0).getString("TaxGroupName");

                                for (int i = 0; i < productCategoryArrayList.size(); i++) {
                                    if (strCategoryId.equals(productCategoryArrayList.get(i).getServiceId())) {
                                        sp_category.setSelection(i);
                                    }
                                }
                                for (int i = 0; i < unitPojoArrayList.size(); i++) {
                                    if (strUnitId.equals(unitPojoArrayList.get(i).getUnitId())) {
                                        sp_unit.setSelection(i);

                                    }
                                }
                                for (int i = 0; i < listTaxGroupArrayList.size(); i++) {
                                    if (strTaxId.equals(listTaxGroupArrayList.get(i).getGroupName())) {
                                        sp_taxList.setSelection(i);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } else {
                    Toast.makeText(ActivityAddItem.this, "Error From Server", Toast.LENGTH_SHORT).show();
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
    private class AsyncCallDeleteProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteProduct";
            String METHOD_NAME = "DeleteProduct";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("catId", id);
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
                            Toast.makeText(ActivityAddItem.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(ActivityAddItem.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
//            if (MainActivity.isPurchase) {
//                PurchaseActivity.isPurchaseActivityRefreshed = true;
//            } else {
//                PartyListActivity.isRefreshedParty = true;
//            }

        }

    }

}
