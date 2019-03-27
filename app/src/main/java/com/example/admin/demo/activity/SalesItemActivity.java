package com.example.admin.demo.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.NextOrderAdapter;
import com.example.admin.demo.adapter.OrderItemDetailsAdapter;
import com.example.admin.demo.adapter.PartyInvoiceAdapter;
import com.example.admin.demo.adapter.ProductUnitAdapter;
import com.example.admin.demo.adapter.SpinnerCustomAdapter;
import com.example.admin.demo.adapter.TaxAdapter;
import com.example.admin.demo.adapter.UnitAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.OnSalesItemClick;
import com.example.admin.demo.interfaces.ProductAdapterInterface;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.model.ListTaxGroup;
import com.example.admin.demo.model.ListTaxGroupPojo;
import com.example.admin.demo.model.ProductDetailsPojo;
import com.example.admin.demo.model.TaxList;
import com.example.admin.demo.model.TaxListPojo;
import com.example.admin.demo.model.UnitListPojo;
import com.example.admin.demo.model.UnitPojo;
import com.example.admin.demo.model.edit_model.SalesInvoice;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.CustomerAdapter;
import com.example.admin.demo.util.InstantAutoComplete;
import com.example.admin.demo.util.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SalesItemActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, CallBack, ProductIntefrace, ProductAdapterInterface,
        AdapterView.OnItemSelectedListener, OnSalesItemClick {
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

        }
    };
    private ArrayList<String> autocomplete_location;
    private PopupMenu popupMenuType;
    private ArrayList<ProductDetailsPojo> productDetailsPojos;
    private Toolbar toolbar_sale;
    private String[] unitList;
    private ArrayAdapter<String> dataAdapter;
    private ArrayList<UnitPojo> unitPojos;
    private ProductUnitAdapter productUnitAdapter;
    private CoordinatorLayout cl_add_sale_invoice;
    private EditText et_customer_name, et_date, et_party_balance, et_discount_percentage,
            et_discount_amount, et_round_of, et_total_amount, et_received_amount,
            et_balance, et_payment_type, et_referenceNo, et_item_tax_name,
            et_product_name_item, et_qty_item, et_free_qty_item, et_price_per_unit_item,
            et_discount_percentage_item, et_discount_amount_item, et_unit_item, et_cheque_deposit_date;
    private Button btnSaveOrder, btnCancelOrder, btn_cancel_add_item, btn_save_add_item, btnEditOrder, btnDeleteOrder, btn_delete_item;
    private TextView tv_add_sales_item, appNameNavBar, tv_dialog_add_customer,
            tv_am, tv_pm, tv_subtotal_item, tv_select_tax_item, tv_total_amount_item,
            tv_order_item_total_qty, tv_order_item_subtotal;
    private Switch cashOnly;
    private CheckBox cb_round_off;
    private SoapPrimitive resultString;
    private String strPartyId, strPartyName, strPartyBalance, strTaxId, strUnitId,
            strTimeStatus, strTripId, strRouteId, strGroupName, strEditAmount,
            strPreviousAmount, strInvoiceEdit, strIsNextDayOrderGenerated;
    private AlertDialog alertDialog, alertDialogItem;
    private AlertDialog.Builder alertDialogBuilder, alertDialogBuilderItem;
    private ProgressBar pb_rv_customer_list, pb_category_list;
    private RecyclerView rv_customer_list, rv_sales_item_details;
    private DecimalFormat df;
    private TaxAdapter taxAdapter;
    private CardView card_am, card_pm;
    private boolean card_status_am = false;
    private boolean card_status_pm = false;
    private boolean isClicked = false;
    private boolean strCashStatus;
    private ArrayList<UnitListPojo> unitListPojoArrayList;
    TextInputLayout textInputLayout;
    private double percentage, subTotalAmount, discount_amount, taxRate,
            totalAmount, taxAmount, spinnerGroupRate;
    private SessionManagement session;
    private String compid = "", loginid = "", customerNameAutoComplete,
            productNameAutoComplete;
    private String strProductId, strProductName, strPricePerUnit, strInvoiceNo,
            strUnitName, strSpinnerItem, strSpinnerItemId, strSpinnerItemRate,
            strSalesPrice, strPurchasePrice, strSalesInvoiceItemId;
    private boolean etPercentageItemFocus, etDiscountItemFocus, etQtyItem,
            etPercentageFocus, etDiscountFocus, etReceivedFocus, etQtyFocus,
            etCustomerFocus, etPriceFocus;
    private LinearLayout ll_order_item_recyclerview, ll_order_item_details,
            ll_item_sub_total, ll_edit_delete, ll_save_cancel;
    private OrderItemDetailsAdapter adapter;
    private ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList;
    private ArrayList<SalesInvoice> salesInvoiceArrayList;
    private ImageView iv_order_item_up_arrow, iv_order_item_down_arrow,
            iv_items_close_button;
    private boolean isDetailsSaved = true;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private InstantAutoComplete autoCompleteTextView, autoCompleteTextViewProduct;
    private String[] customerName, productName;
    private ArrayList<CustomerDetailsPojo> customerDetailsPojoArrayList;
    private ArrayList<TaxListPojo> taxListPojoArrayList;
    private CustomerAdapter customerAdapter = null;
    private ProductAdapter productAdapter = null;
    private UnitAdapter unitAdapter;
    private Spinner sp_unit_list, sp_tax_group;
    private ArrayList<ListTaxGroupPojo> listTaxGroupPojoArrayList;
    private ArrayList<ListTaxGroup> listTaxGroupArrayList;
    private LinearLayout ll_cheque_deposit_date;
    private DatePickerDialog chequeDateDialog, payentDateDialog;
    private String strPaymentDate, strChequeDate, strChequeDepositDate, strOrderStatus;
    private int date_Year_cheque, date_Month_cheque, date_Day_cheque,
            date_Year_payment, date_Month_payment, date_Day_payment;
    private Calendar calendar_cheque, calendar_payment;
    private boolean isNextDayOrderAvailable = false;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item);
        initView();
        df = new DecimalFormat("#0.00");
        if (MainActivity.isCashSale) {
            Functions.setToolBar(SalesItemActivity.this, toolbar_sale, "Cash Sale", true);
            appNameNavBar.setText("Cash Sale");
            strCashStatus = true;
            textInputLayout.setHint("Received Amount");
            etCustomerFocus = false;
            cashOnly.setClickable(false);
            autoCompleteTextView.setText("Cash Sale");
            autoCompleteTextView.setEnabled(false);
            cashOnly.setChecked(true);
            et_party_balance.setText("0");
            strPartyId = "0";
            strTripId = "0";
            strRouteId = "0";
            et_balance.setText("0");
            ll_cheque_deposit_date.setVisibility(View.GONE);

        } else {
            if (MainActivity.isSale) {
                textInputLayout.setHint("Received Amount");
                ll_cheque_deposit_date.setVisibility(View.VISIBLE);


            } else if (MainActivity.isPurchase) {
                cashOnly.setVisibility(View.GONE);
                textInputLayout.setHint("Paid Amount");
                ll_cheque_deposit_date.setVisibility(View.GONE);

            }
            Intent intent = getIntent();
            if (intent != null) {
                strPartyName = intent.getStringExtra("PARTY_NAME");
                strPartyId = intent.getStringExtra("PARTY_ID");
                strPartyBalance = intent.getStringExtra("PARTY_BALANCE");
                Functions.setToolBar(SalesItemActivity.this, toolbar_sale, "Purchase Order", true);
                appNameNavBar.setText("Mr/Ms. " + strPartyName);
                strTripId = intent.getStringExtra("TRIP_ID");
                strRouteId = intent.getStringExtra("ROUTE_ID");
                autoCompleteTextView.setText(strPartyName);
                et_party_balance.setText(strPartyBalance);
            }

        }

        if (PartyInvoiceAdapter.isEditClicked || NextOrderAdapter.isEditClicked) {
            btnDeleteOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setVisibility(View.GONE);
            btnEditOrder.setVisibility(View.VISIBLE);
            btnSaveOrder.setVisibility(View.GONE);
            strPartyName = getIntent().getStringExtra("PARTY_NAME");
            strPartyBalance = getIntent().getStringExtra("PARTY_BALANCE");
            strPreviousAmount = getIntent().getStringExtra("PREVIOUS_AMOUNT");
            strInvoiceEdit = getIntent().getStringExtra("SALES_NO");
            strInvoiceNo = getIntent().getStringExtra("INVOICE_NO");
            strPartyId = getIntent().getStringExtra("PARTY_ID");
            strTripId = getIntent().getStringExtra("TRIP_ID");
            strRouteId = getIntent().getStringExtra("ROUTE_ID");
            autoCompleteTextView.setEnabled(false);
            et_date.setEnabled(false);
            et_party_balance.setEnabled(false);
            et_discount_amount.setEnabled(false);
            et_discount_percentage.setEnabled(false);
            et_round_of.setEnabled(false);
            et_received_amount.setEnabled(false);
            et_total_amount.setEnabled(false);
            et_balance.setEnabled(false);
            et_cheque_deposit_date.setEnabled(false);
            et_referenceNo.setEnabled(false);
            ll_order_item_details.setEnabled(false);
            cb_round_off.setEnabled(false);
            et_payment_type.setEnabled(false);
            isClicked = false;
            tv_add_sales_item.setVisibility(View.GONE);
            new AsyncGetDetailsByInvoice().execute(strInvoiceEdit);

        } else {
            generateInvoiceNo();

        }
        session = new SessionManagement(this);
        // Getting saved details of session
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
        // Calender
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        if (PartyInvoiceActivity.isNextDayOrder) {
            calendar.add(Calendar.DATE, 1);
            date_Day = calendar.get(Calendar.DATE);
            strOrderStatus = "Open Orders";
            textInputLayout.setHint("Received Amount");
            Date dt = new Date();
            dt = calendar.getTime();
            Log.d("Nextdate", dt + "");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String todayAsString = df.format(dt);
            Log.d("Nextdate", todayAsString + "");
            new AsyncNextDayOrderDetails().execute(strPartyId, todayAsString);
        } else {
            date_Day = calendar.get(Calendar.DATE);
            strOrderStatus = "Closed Orders";
            textInputLayout.setHint("Received Amount");
            //Toast.makeText(this, strOrderStatus +"", Toast.LENGTH_SHORT).show();

        }

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_date.setText(simpleDateFormat.format(calendar.getTime()));
        autocomplete_location = new ArrayList<>();
        taxListPojoArrayList = new ArrayList<>();
        if (ll_cheque_deposit_date.getVisibility() == View.VISIBLE) {
            et_cheque_deposit_date.setText(simpleDateFormat.format(calendar.getTime()));
        }

        cb_round_off.setChecked(true);

    }

    // Initialization of all views
    private void initView() {
        textInputLayout = (TextInputLayout) findViewById(R.id.text_input_layout);
        toolbar_sale = (Toolbar) findViewById(R.id.toolbar_sale);
        tv_add_sales_item = (TextView) findViewById(R.id.tv_add_sales_item);
        View view = toolbar_sale.getRootView();
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        et_customer_name = findViewById(R.id.et_customer_name);
        et_date = findViewById(R.id.et_date);
        autoCompleteTextView = findViewById(R.id.autoText);
        tv_order_item_total_qty = findViewById(R.id.tv_order_item_total_qty);
        tv_order_item_subtotal = findViewById(R.id.tv_sales_subtotal);
        ll_order_item_details = findViewById(R.id.ll_order_item_details);
        ll_edit_delete = findViewById(R.id.ll_edit_delete);
        pb_category_list = findViewById(R.id.pb_category_list);
        ll_order_item_recyclerview = findViewById(R.id.ll_order_item_recyclerview);
        ll_item_sub_total = findViewById(R.id.ll_item_sub_total);
        et_party_balance = findViewById(R.id.et_party_balance);
        et_discount_percentage = findViewById(R.id.et_discount_percentage);
        et_discount_amount = findViewById(R.id.et_discount_amount);
        et_round_of = findViewById(R.id.et_round_of);
        ll_cheque_deposit_date = findViewById(R.id.ll_cheque_deposit_date);
        iv_order_item_up_arrow = findViewById(R.id.iv_order_item_up_arrow);
        iv_order_item_down_arrow = findViewById(R.id.iv_order_item_down_arrow);
        rv_sales_item_details = findViewById(R.id.rv_sales_item_details);
        et_total_amount = findViewById(R.id.et_total_amount);
        btnEditOrder = findViewById(R.id.btnEditOrder);
        btnDeleteOrder = findViewById(R.id.btnDeleteOrder);
        et_received_amount = findViewById(R.id.et_received_amount);
        et_cheque_deposit_date = findViewById(R.id.et_cheque_deposit_date);
        et_balance = findViewById(R.id.et_balance);
        et_referenceNo = findViewById(R.id.et_referenceNo);
        et_payment_type = findViewById(R.id.et_payment_type);
        btnSaveOrder = findViewById(R.id.btnSaveOrder);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        cashOnly = findViewById(R.id.cashOnly);
        cl_add_sale_invoice = findViewById(R.id.cl_add_sale_invoice);
        cb_round_off = findViewById(R.id.cb_round_off);
        tv_add_sales_item.setOnClickListener(this);
        et_payment_type.setOnClickListener(this);
        cashOnly.setOnCheckedChangeListener(this);
        et_date.setOnClickListener(this);
        btnSaveOrder.setOnClickListener(this);
        ll_order_item_details.setOnClickListener(this);
        et_cheque_deposit_date.setOnClickListener(this);
        btnCancelOrder.setOnClickListener(this);
        btnDeleteOrder.setOnClickListener(this);
        btnEditOrder.setOnClickListener(this);
        checkFocusChangeListener();
        edtPercentageWatcher();

        AsyncAutoCustomerList asyncAutoCustomerList = new AsyncAutoCustomerList();
        asyncAutoCustomerList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerNameAutoComplete = parent.getAdapter().getItem(position).toString();
                for (int j = 0; j < customerDetailsPojoArrayList.size(); j++) {
                    if (customerNameAutoComplete.equals(customerDetailsPojoArrayList.get(j).getPartyName())) {
                        strPartyId = customerDetailsPojoArrayList.get(j).getPartyId();
                        strPartyBalance = customerDetailsPojoArrayList.get(j).getPartyCurrentBalance();
                        et_party_balance.setText(strPartyBalance);
                        strRouteId = customerDetailsPojoArrayList.get(j).getRouteId();
                        strTripId = customerDetailsPojoArrayList.get(j).getTripId();
                        //Toast.makeText(SalesItemActivity.this, strPartyId + " " + customerNameAutoComplete, Toast.LENGTH_SHORT).show();
                        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (count == 0) {
                                    et_party_balance.setText("");
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    }
                }
            }
        });
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etCustomerFocus = hasFocus;
            }
        });
        Functions.hideKeyboard(this);
        cb_round_off.setOnCheckedChangeListener(this);
    }

    // Generation of random invoice number
    private void generateInvoiceNo() {
        if (isDetailsSaved) {
            Random rand = new Random();
            int randInt = rand.nextInt(999999) + 1;
            strInvoiceNo = "INV" + String.valueOf(randInt);
            isDetailsSaved = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_sales_item:
                isClicked = true;
                if (TextUtils.isEmpty(autoCompleteTextView.getText().toString())) {
                    Toast.makeText(this, "Please first select the customer", Toast.LENGTH_SHORT).show();
                } else {
                    addItemsDialog();
                }
                break;

            case R.id.btnDeleteOrder:
                new AsyncCallDeleteSales().execute(strInvoiceEdit);
                PartyInvoiceAdapter.isEditClicked = false;
                PartyInvoiceActivity.isRefreshed = true;
                break;
            case R.id.btnEditOrder:
                autoCompleteTextView.setEnabled(true);
                et_date.setEnabled(true);
                et_party_balance.setEnabled(true);
                et_discount_amount.setEnabled(true);
                et_discount_percentage.setEnabled(true);
                et_round_of.setEnabled(true);
                et_received_amount.setEnabled(true);
                et_total_amount.setEnabled(true);
                et_balance.setEnabled(true);
                et_cheque_deposit_date.setEnabled(true);
                et_referenceNo.setEnabled(true);
                ll_order_item_details.setEnabled(true);
                tv_add_sales_item.setVisibility(View.VISIBLE);
                et_payment_type.setEnabled(true);
                cb_round_off.setEnabled(true);
                isClicked = true;
                btnEditOrder.setVisibility(View.GONE);
                btnSaveOrder.setVisibility(View.VISIBLE);

                break;
            case R.id.et_payment_type:
                paymentType();
                break;
            case R.id.tv_dialog_add_customer:
                et_customer_name.setFocusable(true);
                et_customer_name.setFocusableInTouchMode(true);
                alertDialog.dismiss();
                break;
            case R.id.et_item_tax_name:
                //chooseCategory("tax");
                break;
            case R.id.card_am:
                cardAM();
                break;
            case R.id.card_pm:
                cardPM();
                break;
            case R.id.iv_items_close_button:
                AsyncCallOrderItemListList callOrderItemListList = new AsyncCallOrderItemListList();
                callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
                alertDialogItem.dismiss();
                break;
            case R.id.btn_delete_item:
                new AsyncCallDeleteSaleItem().execute(strSalesInvoiceItemId);
                alertDialogItem.dismiss();
                break;
            case R.id.btnSaveOrder:
                submitSalesInvoice();
                break;
            case R.id.btn_save_and_new_item:
                saveSalesInvoiceItem();
                break;
            case R.id.btn_add_item:
                saveSalesInvoiceItem();
                alertDialogItem.dismiss();
                break;
            case R.id.et_date:
                getPaymentDate();
                break;
            case R.id.et_cheque_deposit_date:
                getChequeDate();
                break;
            case R.id.ll_order_item_details:
                if (ll_order_item_recyclerview.getVisibility() == View.GONE) {
                    ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                    iv_order_item_down_arrow.setVisibility(View.GONE);
                    iv_order_item_up_arrow.setVisibility(View.VISIBLE);
                    callOrderItemListList = new AsyncCallOrderItemListList();
                    callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
                    etPercentageFocus = true;
                } else {
                    iv_order_item_down_arrow.setVisibility(View.VISIBLE);
                    iv_order_item_up_arrow.setVisibility(View.GONE);
                    ll_order_item_recyclerview.setVisibility(View.GONE);
                }
                break;
            case R.id.btnCancelOrder:
                clearAllEditText();
                break;
        }
    }

    // save ordered items
    private void saveSalesInvoiceItem() {
        if (TextUtils.isEmpty(autoCompleteTextViewProduct.getText().toString())) {
            Toast.makeText(this, "Please Select Product", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please Select Product", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_qty_item.getText().toString())) {
            Toast.makeText(this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please Enter Quantity", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_price_per_unit_item.getText().toString())) {
            Toast.makeText(this, "Please Enter price per unit", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please Enter price per unit", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_discount_amount_item.getText().toString())) {
            Toast.makeText(this, "Please Enter discount amount", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please Enter discount amount", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_discount_percentage_item.getText().toString())) {
            Toast.makeText(this, "Please Enter percentage", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please Enter percentage", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(strSpinnerItem)) {
            Toast.makeText(this, "Please select amount of tax", Toast.LENGTH_SHORT).show();
            //Snackbar.make(cl_add_sale_invoice, "Please select amount of tax", Snackbar.LENGTH_SHORT).show();

        } else if (strUnitName == null && sp_unit_list.getSelectedItem() == null) {
            Toast.makeText(this, "Please select unit", Toast.LENGTH_SHORT).show();

        } else {
            AsyncCallSalesInvoiceItem callSalesInvoiceItem = new AsyncCallSalesInvoiceItem();
            callSalesInvoiceItem.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                    et_price_per_unit_item.getText().toString(),
                    et_qty_item.getText().toString(),
                    et_discount_amount_item.getText().toString(),
                    tv_subtotal_item.getText().toString(),
                    tv_select_tax_item.getText().toString(),
                    tv_total_amount_item.getText().toString(),
                    et_free_qty_item.getText().toString());


        }
    }

    // save sales invoice
    private void submitSalesInvoice() {
        if (TextUtils.isEmpty(autoCompleteTextView.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Select Customer", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_date.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Select Date", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_party_balance.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Enter party balance", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_discount_percentage.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Enter percentage", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_discount_amount.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Enter amount", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_total_amount.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Enter Total Amount", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_balance.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please enter balance", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_payment_type.getText().toString())) {
            Snackbar.make(cl_add_sale_invoice, "Please Select payment type", Snackbar.LENGTH_SHORT).show();

        } else {
            if (TextUtils.isEmpty(et_received_amount.getText().toString())) {
                et_received_amount.setText("0");

            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date date1 = new Date();
            Date date = new Date();
            String selectedDate = null;
            if (PartyInvoiceActivity.isNextDayOrder) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                selectedDate = et_date.getText().toString();
            } else {
                selectedDate = et_date.getText().toString();

            }
            String chequeDate = et_cheque_deposit_date.getText().toString();
            try {
                date = format.parse(selectedDate);
                date1 = format.parse(chequeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatedDate = simpleDateFormat.format(date);
            String strChqDate = simpleDateFormat.format(date1);
            AsyncCallSalesInvoice callSalesInvoice = new AsyncCallSalesInvoice();
            callSalesInvoice.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "1",
                    formatedDate,
                    et_total_amount.getText().toString(),
                    et_received_amount.getText().toString(),
                    et_balance.getText().toString(),
                    et_discount_amount.getText().toString(),
                    et_discount_amount.getText().toString(),
                    et_payment_type.getText().toString(),
                    et_referenceNo.getText().toString(),
                    strChqDate);
        }
    }

    // Select the payment type whether payment type is cheque or cash
    void paymentType() {
        popupMenuType = new PopupMenu(SalesItemActivity.this, et_payment_type);
        popupMenuType.getMenuInflater().inflate(R.menu.type_layout, popupMenuType.getMenu());
        popupMenuType.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Cash")) {
                    et_payment_type.setText(item.getTitle());
                }
                if (item.getTitle().equals("Cheque")) {
                    et_payment_type.setText(item.getTitle());
                }
                return true;
            }
        });
        popupMenuType.show();
    }

    // Override method for switch button
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == cashOnly) {
            if (isChecked) {
                cashOnly.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.switch_track_checked_true_color), PorterDuff.Mode.SRC_IN);
                autoCompleteTextView.setText("Cash Sale");
                strCashStatus = true;
            } else {
                autoCompleteTextView.setText("");
                cashOnly.getTrackDrawable().setColorFilter(ContextCompat.getColor(this, R.color.switch_track_checked_false_color), PorterDuff.Mode.SRC_IN);
                strCashStatus = false;
            }
        }
        if (buttonView == cb_round_off) {
            if (isChecked) {
                if (MainActivity.isCashSale) {
                    double round_off = Double.parseDouble(df.format(Math.round(subTotalAmount) - subTotalAmount));
                    et_round_of.setText(round_off + "");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_balance.setText("0");
                    et_received_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));

                } else {
                    double round_off = Double.parseDouble(df.format(Math.round(subTotalAmount) - subTotalAmount));
                    et_round_of.setText(round_off + "");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_balance.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_received_amount.setText("0");
                }

            } else {
                if (MainActivity.isCashSale) {
                    et_round_of.setText("0.00");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount)));
                    et_balance.setText("0");
                    et_received_amount.setText(String.valueOf(df.format(subTotalAmount)));
                } else {
                    et_round_of.setText("0.00");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount)));
                    et_balance.setText(String.valueOf(df.format(subTotalAmount)));
                    et_received_amount.setText("0");
                }

            }
        }
    }

    // Override method of interface for getting tax and party details
    @Override
    public void AddParty(String str_name, String type, String id) {
        strPartyId = id;
        strPartyBalance = type;
        strPartyName = str_name;
        autoCompleteTextView.setText(strPartyName);
        et_party_balance.setText(strPartyBalance);
        //Toast.makeText(this, strPartyName + strPartyId + strPartyBalance + "", Toast.LENGTH_SHORT).show();
        autoCompleteTextView.dismissDropDown();
        Functions.hideKeyboard(SalesItemActivity.this);
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {
        if (strPartyName.equals("tax")) {
            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
            discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
            taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
            tv_select_tax_item.setText(df.format(taxAmount));
            totalAmount = subTotalAmount - discount_amount + taxAmount;
            tv_total_amount_item.setText(df.format(totalAmount));
        } else {
            strTripId = strPartyId;
            strRouteId = strPartyName;
            Toast.makeText(this, strTripId + strRouteId + "", Toast.LENGTH_SHORT).show();
        }
    }

    // Dialog for adding item details
    void addItemsDialog() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.activity_add_item, null);
        alertDialogBuilderItem = new AlertDialog.Builder(this);
        et_item_tax_name = promptsView.findViewById(R.id.et_item_tax_name);
        tv_am = promptsView.findViewById(R.id.tv_am);
        tv_pm = promptsView.findViewById(R.id.tv_pm);
        card_am = promptsView.findViewById(R.id.card_am);
        card_pm = promptsView.findViewById(R.id.card_pm);
        et_unit_item = promptsView.findViewById(R.id.et_unit_item);
        sp_unit_list = promptsView.findViewById(R.id.sp_unit_list);
        autoCompleteTextViewProduct = promptsView.findViewById(R.id.autoCompleteTextViewProduct);
        iv_items_close_button = promptsView.findViewById(R.id.iv_items_close_button);
        et_product_name_item = promptsView.findViewById(R.id.et_product_name_item);
        btn_cancel_add_item = promptsView.findViewById(R.id.btn_add_item);
        btn_delete_item = promptsView.findViewById(R.id.btn_delete_item);
        et_qty_item = promptsView.findViewById(R.id.et_qty_item);
        et_free_qty_item = promptsView.findViewById(R.id.et_free_qty_item);
        et_price_per_unit_item = promptsView.findViewById(R.id.et_price_per_unit_item);
        tv_subtotal_item = promptsView.findViewById(R.id.tv_subtotal_item);
        sp_tax_group = promptsView.findViewById(R.id.sp_tax_group);
        if (PartyInvoiceAdapter.isEditClicked || NextOrderAdapter.isEditClicked) {
            //autoCompleteTextViewProduct.setEnabled(false);
            btn_delete_item.setVisibility(View.VISIBLE);
        } else {
            btn_delete_item.setVisibility(View.GONE);
            // autoCompleteTextViewProduct.setEnabled(true);
        }
        if (MainActivity.isPMOrder){
            cardPM();
        }
        else {
            cardAM();
        }
        et_discount_percentage_item = promptsView.findViewById(R.id.et_discount_percentage_item);
        et_discount_amount_item = promptsView.findViewById(R.id.et_discount_amount_item);
        tv_select_tax_item = promptsView.findViewById(R.id.tv_select_tax_item);
        tv_total_amount_item = promptsView.findViewById(R.id.tv_total_amount_item);
        btn_save_add_item = promptsView.findViewById(R.id.btn_save_and_new_item);
        et_product_name_item.setOnClickListener(this);
        iv_items_close_button.setOnClickListener(this);
        sp_unit_list.setOnItemSelectedListener(this);
        sp_tax_group.setOnItemSelectedListener(this);
        et_item_tax_name.setOnClickListener(this);
        et_unit_item.setOnClickListener(this);
        card_pm.setOnClickListener(this);
        card_am.setOnClickListener(this);
        autoCompleteTextViewProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etPercentageItemFocus = true;
                }
            }
        });

        btn_save_add_item.setOnClickListener(this);
        btn_cancel_add_item.setOnClickListener(this);
        btn_delete_item.setOnClickListener(this);
        try {
            AsyncAutoProductList asyncAutoProductList = new AsyncAutoProductList();
            asyncAutoProductList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strPartyId);

            AsyncCallTaxGroupList asyncCallTaxGroupList = new AsyncCallTaxGroupList();
            asyncCallTaxGroupList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        autoCompleteTextViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productNameAutoComplete = parent.getAdapter().getItem(position).toString();
                for (int j = 0; j < productDetailsPojos.size(); j++) {
                    if (productNameAutoComplete.equals(productDetailsPojos.get(j).getProductName())) {
                        autoCompleteTextViewProduct.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (!s.toString().isEmpty()) {
                                    filter(s.toString());
                                }
                            }
                        });
                    }
                }
                etPercentageItemFocus = true;
                etQtyFocus = true;
            }
        });
        et_discount_percentage_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPercentageItemFocus) {
                    et_discount_amount_item.removeTextChangedListener(generalTextWatcher);
                    if (!s.toString().equals("")) {
                        if (count == 0) {
                            et_discount_amount_item.setText("0");
                            tv_select_tax_item.setText("0");
                            tv_total_amount_item.setText("0");
                            et_discount_amount_item.addTextChangedListener(generalTextWatcher);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPercentageItemFocus) {
                    try {
                        et_discount_amount_item.removeTextChangedListener(generalTextWatcher);
                        if (!s.toString().equals("")) {
                            percentage = Double.parseDouble(s.toString());
                            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                            discount_amount = (percentage * subTotalAmount) / 100;
                            et_discount_amount_item.setText(String.valueOf(df.format(discount_amount)));
                            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                            discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
                            taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
                            tv_select_tax_item.setText(df.format(taxAmount));
                            totalAmount = subTotalAmount - discount_amount + taxAmount;
                            tv_total_amount_item.setText(df.format(totalAmount));
                            et_discount_amount_item.addTextChangedListener(generalTextWatcher);

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        et_discount_amount_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etDiscountItemFocus) {
                    et_discount_percentage_item.removeTextChangedListener(generalTextWatcher);
                    if (!s.toString().equals("")) {
                        if (count == 0) {
                            et_discount_percentage_item.setText("0");
                            tv_select_tax_item.setText("0");
                            tv_total_amount_item.setText("0");
                            et_discount_percentage_item.addTextChangedListener(generalTextWatcher);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etDiscountItemFocus) {
                    try {
                        et_discount_percentage_item.removeTextChangedListener(generalTextWatcher);
                        if (!s.toString().equals("")) {
                            discount_amount = Double.parseDouble(s.toString());
                            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                            if (subTotalAmount <= 0) {
                                Toast.makeText(SalesItemActivity.this, "Please select product first", Toast.LENGTH_SHORT).show();
                            } else {
                                if (discount_amount > 0) {
                                    percentage = (discount_amount * 100) / subTotalAmount;
                                    et_discount_percentage_item.setText(String.valueOf(df.format(percentage)));
                                    subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                                    discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
                                    taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
                                    tv_select_tax_item.setText(df.format(taxAmount));
                                    totalAmount = subTotalAmount - discount_amount + taxAmount;
                                    tv_total_amount_item.setText(df.format(totalAmount));
                                }

                            }

                            et_discount_percentage_item.addTextChangedListener(generalTextWatcher);

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        et_qty_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etQtyFocus) {
                    if (count == 0) {
                        tv_select_tax_item.setText("0.00");
                        tv_total_amount_item.setText("0.00");
                        tv_subtotal_item.setText("0.00");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etQtyFocus) {
                    if (!s.toString().equals("")) {
                        double qty = Double.parseDouble(s.toString());
                        double pricePerUnit = 0;
                        if (!et_price_per_unit_item.getText().toString().equals("")) {
                            pricePerUnit = Double.parseDouble(et_price_per_unit_item.getText().toString());
//                            et_discount_percentage_item.setText("0.00");
//                            et_discount_amount_item.setText("0.00");
                        }
                        tv_subtotal_item.setText(String.valueOf(df.format(qty * pricePerUnit)));
                        if (!tv_subtotal_item.getText().toString().equals("")) {
                            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                        }
                        if (!et_discount_amount_item.getText().toString().equals("")) {
                            discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
                        }
                        taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
                        tv_select_tax_item.setText(df.format(taxAmount));
                        totalAmount = subTotalAmount - discount_amount + taxAmount;
                        tv_total_amount_item.setText(df.format(totalAmount));
                    }

                }

            }
        });
        et_qty_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etQtyFocus = hasFocus;
                etPriceFocus = false;
            }
        });
        et_price_per_unit_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPriceFocus) {
                    if (count == 0) {
                        tv_select_tax_item.setText("0.00");
                        tv_total_amount_item.setText("0.00");
                        tv_subtotal_item.setText("0.00");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPriceFocus) {
                    if (!s.toString().equals("")) {
                        double pricePerUnit = Double.parseDouble(s.toString());
                        double qty = 0;
                        if (!et_qty_item.getText().toString().equals("")) {
                            qty = Double.parseDouble(et_qty_item.getText().toString());
//                            et_discount_percentage_item.setText("0.00");
//                            et_discount_amount_item.setText("0.00");
                        }
                        tv_subtotal_item.setText(String.valueOf(df.format(qty * pricePerUnit)));
                        if (!tv_subtotal_item.getText().toString().equals("")) {
                            subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                        }
                        if (!et_discount_amount_item.getText().toString().equals("")) {
                            discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
                        }
                        taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
                        tv_select_tax_item.setText(df.format(taxAmount));
                        totalAmount = subTotalAmount - discount_amount + taxAmount;
                        tv_total_amount_item.setText(df.format(totalAmount));
                    }

                }

            }
        });
        et_price_per_unit_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etPriceFocus = hasFocus;
                etQtyFocus = false;
            }
        });
        et_discount_amount_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etDiscountItemFocus = hasFocus;
                etPercentageItemFocus = false;
            }
        });
        et_discount_percentage_item.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etPercentageItemFocus = hasFocus;
                etDiscountItemFocus = false;
            }
        });

        alertDialogBuilderItem.setView(promptsView);
        alertDialogItem = alertDialogBuilderItem.create();
        alertDialogItem.setCancelable(true);
        alertDialogItem.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogItem.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialogItem.show();
        alertDialogItem.getWindow().setAttributes(lp);
        alertDialogItem.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    // Selecting status as AM
    private void cardAM() {
        card_status_pm = false;
        card_status_am = true;
        strTimeStatus = tv_am.getText().toString();
        card_am.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        card_pm.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        tv_am.setTextColor(getResources().getColor(android.R.color.white));
        tv_pm.setTextColor(getResources().getColor(R.color.colorPrimary));
        //Toast.makeText(this, strTimeStatus, Toast.LENGTH_SHORT).show();
    }

    // Selecting status as PM
    private void cardPM() {
        card_status_pm = true;
        card_status_am = false;
        strTimeStatus = tv_pm.getText().toString();
        card_am.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_pm.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_am.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_pm.setTextColor(getResources().getColor(android.R.color.white));
        //Toast.makeText(this, strTimeStatus, Toast.LENGTH_SHORT).show();
    }

    // Interface override method for selecting product
    @Override
    public void onProductSelected(String productId, String productName,
                                  String pricePerUnit, String taxId) {
        strProductId = productId;
        strProductName = productName;
        strSalesPrice = pricePerUnit;
        strTaxId = taxId;

        //Toast.makeText(this, strTaxId+"", Toast.LENGTH_SHORT).show();
        autoCompleteTextViewProduct.setText(strProductName);
        if (et_discount_amount_item.getText().toString().equals("")) {
            et_discount_amount_item.setText("0.00");
        }
        if (et_discount_percentage_item.getText().toString().equals("")) {
            et_discount_percentage_item.setText("0.00");
        }
        autoCompleteTextViewProduct.dismissDropDown();
        et_qty_item.requestFocus();
        AsyncUnitList asyncUnitList = new AsyncUnitList();
        asyncUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strProductId, strPartyId);

        AsyncCallTaxGroupList asyncCallTaxGroupList = new AsyncCallTaxGroupList();
        asyncCallTaxGroupList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void UnitDetails(String unitId, String unitName) {
        strUnitId = unitId;
        strPurchasePrice = unitName;

    }


    // Save sales invoice server call
    public String saveSalesInvoice(String compId, String loginId, String routeId, String tripId,
                                   String partyId, String invNo, String invDate,
                                   String invAmount, String paidAmount, String pendingAmt,
                                   String TaxAmount, String imvoiceSubTotal,
                                   String otherCharges, String discountAmt,
                                   String paymentType, String referenceNo,
                                   String checqueDeposite, String InvStatus) {
        String SOAP_ACTION, METHOD_NAME;
        if (PartyInvoiceAdapter.isEditClicked || NextOrderAdapter.isEditClicked) {
            SOAP_ACTION = "http://tempuri.org/UpdateSalesInvoice";
            METHOD_NAME = "UpdateSalesInvoice";
        } else if (isNextDayOrderAvailable) {
            SOAP_ACTION = "http://tempuri.org/UpdateSalesInvoice";
            METHOD_NAME = "UpdateSalesInvoice";
        } else {
            SOAP_ACTION = "http://tempuri.org/SaveSalesInvoice";
            METHOD_NAME = "SaveSalesInvoice";
        }
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("compId", compId);
            Request.addProperty("loginId", loginId);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
            Request.addProperty("partyId", partyId);
            Request.addProperty("invNo", invNo);
            Request.addProperty("invDate", invDate);
            Request.addProperty("invAmount", invAmount);
            Request.addProperty("paidAmount", paidAmount);
            Request.addProperty("pendingAmt", pendingAmt);
            Request.addProperty("TaxAmount", TaxAmount);
            Request.addProperty("remarks", "");
            Request.addProperty("imvoiceSubTotal", imvoiceSubTotal);
            Request.addProperty("otherCharges", otherCharges);
            Request.addProperty("discountAmt", discountAmt);
            Request.addProperty("isCashSale", strCashStatus);
            Request.addProperty("paymentType", paymentType);
            Request.addProperty("referenceNo", referenceNo);
            Request.addProperty("checqueDeposite", checqueDeposite);
            Request.addProperty("InvStatus", InvStatus);
            if (PartyInvoiceAdapter.isEditClicked || NextOrderAdapter.isEditClicked) {
                Request.addProperty("previousBalance", strPreviousAmount);
                Request.addProperty("salesInvoiceId", strInvoiceEdit);
            } else if (isNextDayOrderAvailable) {
                Request.addProperty("previousBalance", strPreviousAmount);
                Request.addProperty("salesInvoiceId", strInvoiceEdit);
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
                        Toast.makeText(SalesItemActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        Functions.hideKeyboard(SalesItemActivity.this);
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

    // clear all dynamic_field after saving items to server
    private void clearAllItemEditText() {
        et_qty_item.removeTextChangedListener(generalTextWatcher);
        et_discount_percentage_item.removeTextChangedListener(generalTextWatcher);
        et_discount_amount_item.removeTextChangedListener(generalTextWatcher);
        autoCompleteTextViewProduct.setText("");
        et_qty_item.setText("");
        et_free_qty_item.setText("0");
        et_price_per_unit_item.setText("");
        tv_subtotal_item.setText("0.00");
        et_discount_percentage_item.setText("0.00");
        et_discount_amount_item.setText("0.00");
        //et_item_tax_name.setText("");
        et_unit_item.setText("");
        tv_select_tax_item.setText("0.00");
        tv_total_amount_item.setText("0.00");
        et_qty_item.addTextChangedListener(generalTextWatcher);
        et_discount_percentage_item.addTextChangedListener(generalTextWatcher);
        et_discount_amount_item.addTextChangedListener(generalTextWatcher);

    }

    // clear all dynamic_field after saving to server
    private void clearAllEditText() {
        et_received_amount.removeTextChangedListener(generalTextWatcher);
        et_discount_percentage.removeTextChangedListener(generalTextWatcher);
        et_discount_amount.removeTextChangedListener(generalTextWatcher);

        strInvoiceNo = "0";
        if (invoiceItemDetailsArrayList != null) {
            invoiceItemDetailsArrayList.clear();
            AsyncCallOrderItemListList callOrderItemListList = new AsyncCallOrderItemListList();
            callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
        }
        if (MainActivity.isCashSale) {
            autoCompleteTextView.setText("Cash Sale");
            et_party_balance.setText("0");
            strPartyId = "0";
            strTripId = "0";
            strRouteId = "0";
            et_balance.setText("0");
        } else {
            autoCompleteTextView.setText("");
            et_party_balance.setText("");
            et_balance.setText("");
        }
        et_round_of.setText("");
        et_total_amount.setText("");
        et_received_amount.setText("");
        et_discount_percentage.setText("");
        et_discount_amount.setText("");
        et_payment_type.setText("Cash");
        et_referenceNo.setText("");
        tv_order_item_subtotal.setText("");
        tv_order_item_total_qty.setText("");
        ll_item_sub_total.setVisibility(View.VISIBLE);
        ll_order_item_recyclerview.setVisibility(View.VISIBLE);
        autoCompleteTextView.dismissDropDown();
        et_received_amount.addTextChangedListener(generalTextWatcher);
        et_discount_percentage.addTextChangedListener(generalTextWatcher);
        et_discount_amount.addTextChangedListener(generalTextWatcher);
    }

    // TextWatcher for edit text
    void edtPercentageWatcher() {

        try {
            et_discount_percentage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etPercentageFocus) {
                        et_discount_amount.removeTextChangedListener(generalTextWatcher);
                        if (!s.toString().equals("")) {
                            if (count == 0) {
                                et_discount_amount.setText("");
                                et_total_amount.setText("");
                                et_received_amount.setText("");
                                et_discount_amount.addTextChangedListener(generalTextWatcher);
                            }
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (etPercentageFocus) {
                        try {
                            if (!s.toString().equals("")) {
                                et_received_amount.removeTextChangedListener(generalTextWatcher);
                                et_discount_amount.removeTextChangedListener(generalTextWatcher);
                                percentage = Double.parseDouble(s.toString());
                                subTotalAmount = Double.parseDouble(tv_order_item_subtotal.getText().toString());
                                discount_amount = (percentage * subTotalAmount) / 100;
                                et_discount_amount.setText(String.valueOf(df.format(discount_amount)));
                                totalAmount = subTotalAmount - discount_amount;
                                if (MainActivity.isCashSale) {
                                    et_total_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_received_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_balance.setText("0");
                                } else {
                                    et_total_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_received_amount.setText("");
                                    et_balance.setText(String.valueOf(df.format(totalAmount)));
                                }

                                et_discount_amount.addTextChangedListener(generalTextWatcher);
                                et_received_amount.addTextChangedListener(generalTextWatcher);
                            }


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            et_discount_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etDiscountFocus) {
                        et_discount_percentage.removeTextChangedListener(generalTextWatcher);
                        if (!s.toString().equals("")) {
                            if (count == 0) {
                                et_discount_percentage.setText("");
                                et_total_amount.setText("");
                                et_received_amount.setText("");
                                et_discount_percentage.addTextChangedListener(generalTextWatcher);
                            }
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (etDiscountFocus) {
                        try {
                            if (!s.toString().equals("")) {
                                et_received_amount.removeTextChangedListener(generalTextWatcher);
                                et_discount_percentage.removeTextChangedListener(generalTextWatcher);
                                discount_amount = Double.parseDouble(s.toString());
                                subTotalAmount = Double.parseDouble(tv_order_item_subtotal.getText().toString());
                                if (discount_amount >= 0) {
                                    percentage = (discount_amount * 100) / subTotalAmount;
                                }
                                Log.d("percentage", String.valueOf(percentage));
                                et_discount_percentage.setText(String.valueOf(df.format(percentage)));
                                totalAmount = subTotalAmount - discount_amount;
                                if (MainActivity.isCashSale) {
                                    et_total_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_received_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_balance.setText("0");
                                } else {
                                    et_total_amount.setText(String.valueOf(df.format(totalAmount)));
                                    et_received_amount.setText("");
                                    et_balance.setText(String.valueOf(df.format(totalAmount)));
                                }
                                et_discount_percentage.addTextChangedListener(generalTextWatcher);
                                et_received_amount.addTextChangedListener(generalTextWatcher);
                            }


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            et_received_amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (etReceivedFocus) {
                        if (!s.toString().equals("")) {
                            double received;
                            received = Double.parseDouble(s.toString());
                            totalAmount = Double.parseDouble(et_total_amount.getText().toString());
                            et_balance.setText(String.valueOf(df.format(totalAmount - received)));
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Focus change listener for edit text
    void checkFocusChangeListener() {
        et_discount_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etDiscountFocus = hasFocus;
                etPercentageFocus = false;
                etReceivedFocus = false;
            }
        });
        et_discount_percentage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etPercentageFocus = hasFocus;
                etDiscountFocus = false;
                etReceivedFocus = false;

            }
        });
        et_received_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etReceivedFocus = hasFocus;
                etDiscountFocus = false;
                etPercentageFocus = false;
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_unit_list:
                strUnitName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < unitListPojoArrayList.size(); i++) {
                    if (strUnitName.equals(unitListPojoArrayList.get(i).getStrUnitName())) {
                        strUnitId = unitListPojoArrayList.get(i).getStrUnitId();
                        double salePrice, purchasePrice, conversionrate, calculation, qty = 0;
                        conversionrate = Double.parseDouble(unitListPojoArrayList.get(i).getStrConversionRate());
                        if (conversionrate == 0.00) {
                            conversionrate = 1;
                        }
                        salePrice = Double.parseDouble(strSalesPrice);
                        purchasePrice = Double.parseDouble(strPurchasePrice);
                        if (MainActivity.isCashSale || MainActivity.isSale || PartyInvoiceActivity.isNextDayOrder || NextOrderAdapter.isEditClicked) {
                            calculation = salePrice / conversionrate;
                            et_price_per_unit_item.setText(df.format(calculation));
                            if (et_qty_item.getText().toString().equals("")) {
                                qty = 0;
                            } else {
                                qty = Double.parseDouble(et_qty_item.getText().toString());
                            }
                            tv_subtotal_item.setText(df.format(calculation * qty));
                        }
                        if (MainActivity.isPurchase) {
                            calculation = purchasePrice / conversionrate;
                            if (et_qty_item.getText().toString().equals("")) {
                                qty = 0;
                            } else {
                                qty = Double.parseDouble(et_qty_item.getText().toString());
                            }
                            et_price_per_unit_item.setText(df.format(calculation));
                            tv_subtotal_item.setText(df.format(calculation * qty));
                        }
                    }
                }
                break;
            case R.id.sp_tax_group:
                strSpinnerItem = ((TextView) view.findViewById(R.id.tv_spinner_group_name)).getText().toString();
                strSpinnerItemRate = ((TextView) view.findViewById(R.id.tv_spinner_group_rate)).getText().toString();
                spinnerGroupRate = Double.parseDouble(strSpinnerItemRate);
                for (int i = 0; i < listTaxGroupArrayList.size(); i++) {
                    if (strSpinnerItem.equals(listTaxGroupArrayList.get(i).getGroupName())) {
                        strSpinnerItemId = listTaxGroupArrayList.get(i).getTaxGroupId();
                    }
                }
                subTotalAmount = Double.parseDouble(tv_subtotal_item.getText().toString());
                discount_amount = Double.parseDouble(et_discount_amount_item.getText().toString());
                taxAmount = (spinnerGroupRate * (subTotalAmount - discount_amount)) / 100;
                tv_select_tax_item.setText(df.format(taxAmount));
                totalAmount = subTotalAmount - discount_amount + taxAmount;
                tv_total_amount_item.setText(df.format(totalAmount));
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSalesItemClick(String id) {

        if (isClicked) {
            strSalesInvoiceItemId = id;
            addItemsDialog();
            new AsyncGetItemDetailsByInvoice().execute(id);
        } else {
            Toast.makeText(this, "Please click on edit button to make changes in this item", Toast.LENGTH_SHORT).show();
        }

    }

    // sales invoice async call
    private class AsyncCallSalesInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String invDate = params[1];
            String invAmount = params[2];
            String paidAmount = params[3];
            String pendingAmt = params[4];
            String discountAmt = params[5];
            String imvoiceSubTotal = params[6];
            String paymentType = params[7];
            String referenceNo = params[8];
            String chequeDate = params[9];

            if (!paidAmount.equals("0")) {
                strOrderStatus = "Closed Orders";
            }


            saveSalesInvoice(compid, loginid, strRouteId, strTripId, strPartyId, strInvoiceNo,
                    invDate, invAmount, paidAmount, pendingAmt, "30", imvoiceSubTotal,
                    "20", discountAmt, paymentType, referenceNo, chequeDate, strOrderStatus);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            isDetailsSaved = true;
            strInvoiceNo = "";
            generateInvoiceNo();
            //PartyInvoiceActivity.isRefreshed = true;
            if (MainActivity.isCashSale) {
                startActivity(new Intent(SalesItemActivity.this, MainActivity.class));
                finish();
            } else if (MainActivity.isSale) {
                Intent intentSales = new Intent(SalesItemActivity.this, PartyInvoiceActivity.class);
                intentSales.putExtra("PARTY_ID", strPartyId);
                intentSales.putExtra("PARTY_NAME", strPartyName);
                intentSales.putExtra("PARTY_BALANCE", strPartyBalance);
                intentSales.putExtra("TRIP_ID", strTripId);
                intentSales.putExtra("ROUTE_ID", strRouteId);
                MainActivity.isSale = true;
                PartyInvoiceActivity.isRefreshed = true;

                finish();
            } else if (MainActivity.isPurchase) {
                Intent intentPurchase = new Intent(SalesItemActivity.this, PartyInvoiceActivity.class);
                intentPurchase.putExtra("PARTY_ID", strPartyId);
                intentPurchase.putExtra("PARTY_NAME", strPartyName);
                intentPurchase.putExtra("PARTY_BALANCE", strPartyBalance);
                intentPurchase.putExtra("TRIP_ID", strTripId);
                intentPurchase.putExtra("ROUTE_ID", strRouteId);
                MainActivity.isPurchase = true;
                PartyInvoiceActivity.isRefreshed = true;
                //startActivity(intentPurchase);
                finish();
            } else if (PartyInvoiceActivity.isNextDayOrder || NextOrderAdapter.isEditClicked) {
                Intent intentPurchase = new Intent(SalesItemActivity.this, PartyInvoiceActivity.class);
                intentPurchase.putExtra("PARTY_ID", strPartyId);
                intentPurchase.putExtra("PARTY_NAME", strPartyName);
                intentPurchase.putExtra("PARTY_BALANCE", strPartyBalance);
                intentPurchase.putExtra("TRIP_ID", strTripId);
                intentPurchase.putExtra("ROUTE_ID", strRouteId);
                //MainActivity.isPurchase = true;
                PartyInvoiceActivity.isRefreshed = true;
                //startActivity(intentPurchase);
                finish();
            }
            if (result.equals("success")) {
                //startActivity(new Intent(SalesItemActivity.this,PartyListActivity.class));
                PartyInvoiceActivity.isRefreshed = true;
                Toast.makeText(SalesItemActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(SalesItemActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // sales item invoice async call
    private class AsyncCallSalesInvoiceItem extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_category_list.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String salesInvoiceId = "0";
            String id = params[0];
            String pricePerQty = params[1];
            String itemQty = params[2];
            String disCountAmt = params[3];
            String subtotal = params[4];
            String taxAmount = params[5];
            String itemTotalAmount = params[6];
            String freeQty = params[7];
            String SOAP_ACTION = "http://tempuri.org/SaveSalesInvoiceItem";
            String METHOD_NAME = "SaveSalesInvoiceItem";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("salesInvoiceId", salesInvoiceId);
                Request.addProperty("invNo", strInvoiceNo);
                Request.addProperty("productId", strProductId);
                Request.addProperty("pricePerQty", pricePerQty);
                Request.addProperty("itemQty", itemQty);
                Request.addProperty("freeQty", freeQty);
                Request.addProperty("unitName", strUnitName);
                Request.addProperty("taxId", "0");
                Request.addProperty("itemTotalAmount", itemTotalAmount);
                Request.addProperty("taxAmount", taxAmount);
                Request.addProperty("subtotal", subtotal);
                Request.addProperty("disCountAmt", disCountAmt);
                Request.addProperty("partyId", Integer.parseInt(strPartyId));
                Request.addProperty("TaxGroupName", strSpinnerItem);
                Request.addProperty("time", strTimeStatus);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return resultString.toString();
        }



        @Override
        protected void onPostExecute(String result) {
            pb_category_list.setVisibility(View.GONE);
            if (result.equals(null)) {
                Toast.makeText(SalesItemActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else if (result.equals("-2")) {
                Toast.makeText(SalesItemActivity.this, "Duplicate values", Toast.LENGTH_SHORT).show();
            } else {
                new AsyncCallOrderItemListList().execute("1", strInvoiceNo);
                etPercentageFocus = true;
                Toast.makeText(SalesItemActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                clearAllItemEditText();
            }
        }
    }

    // attaching order items to adapter
    private class AsyncCallOrderItemListList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            String strInvoiceNo = params[1];

            invoiceItemDetailsArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListSalesInvoiceItem";
            String METHOD_NAME = "ListSalesInvoiceItem";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("invNo", strInvoiceNo);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (!TextUtils.isEmpty(result)) {
                    final JSONArray jarray = new JSONArray(result);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jsonObject = jarray.getJSONObject(i);
                        final String ProductName = jsonObject.getString("ProductName");
                        final String ItemPrice = jsonObject.getString("ItemPrice");
                        final String ItemQty = jsonObject.getString("ItemQty");
                        final String ItemSubTotalAmount = jsonObject.getString("ItemSubTotalAmount");
                        final String UnitName = jsonObject.getString("UnitName");
                        final String TaxAmount = jsonObject.getString("TaxAmount");
                        final String ItemDiscountAmount = jsonObject.getString("ItemDiscountAmount");
                        final String SalesInvoiceItemId = jsonObject.getString("SalesInvoiceItemId");
                        InvoiceItemDetails invoiceItemDetails = new InvoiceItemDetails();
                        invoiceItemDetails.setProductName(ProductName);
                        invoiceItemDetails.setItemPrice(ItemPrice);
                        invoiceItemDetails.setItemQty(ItemQty);
                        invoiceItemDetails.setItemSubTotalAmount(ItemSubTotalAmount);
                        invoiceItemDetails.setUnitName(UnitName);
                        invoiceItemDetails.setTaxAmount(TaxAmount);
                        invoiceItemDetails.setItemDiscountAmount(ItemDiscountAmount);
                        invoiceItemDetails.setSalesInvoiceItemId(SalesInvoiceItemId);
                        invoiceItemDetailsArrayList.add(invoiceItemDetails);
                        if (adapter
                                != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }

                adapter = new OrderItemDetailsAdapter(invoiceItemDetailsArrayList, SalesItemActivity.this);
                rv_sales_item_details.setHasFixedSize(false);
                rv_sales_item_details.setLayoutManager(new LinearLayoutManager(SalesItemActivity.this));
                rv_sales_item_details.setNestedScrollingEnabled(true);
                rv_sales_item_details.setAdapter(adapter);
                double total = 0, qty = 0, taxAmount = 0, discountAmount = 0;
                for (int j = 0; j < invoiceItemDetailsArrayList.size(); j++) {
                    total = total + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemSubTotalAmount());
                    qty = qty + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                    taxAmount = taxAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                    discountAmount = discountAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemDiscountAmount());
                    tv_order_item_subtotal.setText(df.format(total));
                    ll_item_sub_total.setVisibility(View.VISIBLE);
                    ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                    tv_order_item_total_qty.setText(df.format(qty));
                    et_discount_percentage.setText(df.format(0.00));
                }
                if (invoiceItemDetailsArrayList.size() == 3) {
                    final float scale = getResources().getDisplayMetrics().density;
                    int pixels = (int) (450 * scale + 0.5f);
                    int pixels1 = (int) (390 * scale + 0.5f);
                    ll_order_item_recyclerview.getLayoutParams().height = pixels;
                    rv_sales_item_details.getLayoutParams().height = pixels1;
                }

                if (MainActivity.isCashSale) {
                    double round_off = Double.parseDouble(df.format(Math.round(subTotalAmount) - subTotalAmount));
                    et_round_of.setText(round_off + "");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_balance.setText("0");
                    et_received_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));

                } else {
                    double round_off = Double.parseDouble(df.format(Math.round(subTotalAmount) - subTotalAmount));
                    et_round_of.setText(round_off + "");
                    et_total_amount.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_balance.setText(String.valueOf(df.format(subTotalAmount + round_off)));
                    et_received_amount.setText("0");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    // getting customer list
    private class AsyncAutoCustomerList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
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
                        if (MainActivity.isPurchase) {
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
                        } else if (MainActivity.isSale) {
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
                    }
                    customerName = new String[customerDetailsPojoArrayList.size()];
                    for (int j = 0; j < customerDetailsPojoArrayList.size(); j++) {
                        customerName[j] = customerDetailsPojoArrayList.get(j).getPartyName();
                    }

                }
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
                    autoCompleteTextView.setThreshold(1);
                    customerAdapter = new CustomerAdapter(SalesItemActivity.this, customerDetailsPojoArrayList);
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

    // getting customer list
    private class AsyncAutoProductList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            productDetailsPojos = new ArrayList<>();
            unitListPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListProductWithCustomer";
            String METHOD_NAME = "ListProductWithCustomer";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("partyId", id);
                Request.addProperty("time", strTimeStatus);
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
                    JSONObject productList = new JSONObject(responseJSON);
                    JSONArray productArray = productList.getJSONArray("ProductList");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jsonObject = productArray.getJSONObject(i);
                        final String ProductName = jsonObject.getString("ProductName");
                        final String ProductId = jsonObject.getString("ProductId");
                        final String PricePerUnit = jsonObject.getString("PricePerUnit");
                        final String TaxId = jsonObject.getString("TaxId");
                        final String TaxGroupName = jsonObject.getString("TaxGroupName");
                        final String UnitId = jsonObject.getString("UnitId");
                        final String PurchasePrice = jsonObject.getString("PurchasePrice");
                        final String SalePrice = jsonObject.getString("SalePrice");
                        ProductDetailsPojo productDetailsPojo = new ProductDetailsPojo();
                        productDetailsPojo.setProductName(ProductName);
                        productDetailsPojo.setProductId(ProductId);
                        productDetailsPojo.setPricePerUnit(PricePerUnit);
                        productDetailsPojo.setTaxId(TaxId);
                        productDetailsPojo.setTaxGroupName(TaxGroupName);
                        productDetailsPojo.setUnitId(UnitId);
                        productDetailsPojo.setSalePrice(SalePrice);
                        productDetailsPojo.setPurchasePrice(PurchasePrice);
                        productDetailsPojos.add(productDetailsPojo);
                    }
                    productName = new String[productDetailsPojos.size()];
                    for (int j = 0; j < productDetailsPojos.size(); j++) {
                        productName[j] = productDetailsPojos.get(j).getProductName();
                    }
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    autoCompleteTextViewProduct.setThreshold(1);
                    productAdapter = new ProductAdapter(SalesItemActivity.this, productDetailsPojos);
                    autoCompleteTextViewProduct.setAdapter(productAdapter);
                    autoCompleteTextViewProduct.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (productName.length > 0 || productName != null) {
                                // show all suggestions
                                if (!autoCompleteTextViewProduct.getText().toString().equals(""))
                                    productAdapter.getFilter().filter(null);
                                autoCompleteTextViewProduct.showDropDown();
                            }
                            return false;
                        }
                    });
                }
            });

        }
    }

    // Getting tax list from server
    private class AsyncUnitList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String productId = params[0];
            String partyId = params[1];
            unitListPojoArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListProductWithCustomer";
            String METHOD_NAME = "ListProductWithCustomer";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("partyId", Integer.parseInt(partyId));
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
                    JSONObject productList = new JSONObject(responseJSON);
                    JSONArray productArray = productList.getJSONArray("ProductList");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jsonObject = productArray.getJSONObject(i);
                        final String ProductId = jsonObject.getString("ProductId");
                        JSONArray jsonArray = jsonObject.getJSONArray("ListUnit");
                        if (ProductId.equals(productId)) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object = jsonArray.getJSONObject(j);
                                final String strUnitId = object.getString("UnitId");
                                final String strUnitName = object.getString("UnitName");
                                final String strConversionRate = object.getString("ConversionRate");
                                unitListPojoArrayList.add(new UnitListPojo(strUnitId, strUnitName,
                                        strConversionRate));
                            }
                            unitList = new String[unitListPojoArrayList.size()];
                            for (int j = 0; j < unitListPojoArrayList.size(); j++) {
                                unitList[j] = unitListPojoArrayList.get(j).getStrUnitName();
                            }
                        }
                    }
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return productId;
        }

        @Override
        protected void onPostExecute(String result) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    dataAdapter = new ArrayAdapter<String>(SalesItemActivity.this,
                            android.R.layout.simple_spinner_item, unitList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_unit_list.setAdapter(dataAdapter);
                    Log.d("unitList", unitList.length + "");
                    if (unitList.length == 1) {
                        sp_unit_list.setSelection(0);
                    } else {
                        if (MainActivity.isSale || MainActivity.isCashSale || PartyInvoiceActivity.isNextDayOrder || NextOrderAdapter.isEditClicked) {
                            sp_unit_list.setSelection(1);
                        } else if (MainActivity.isPurchase) {
                            sp_unit_list.setSelection(0);
                        }
                    }
                }
            });

        }
    }

    public class AsyncCallTaxGroupList extends AsyncTask<String, Void, String> {

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
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object = jsonArray.getJSONObject(j);
                        final String strTaxGroupId = object.getString("TaxGroupId");
                        final String strGroupName = object.getString("GroupName");
                        JSONArray array = object.getJSONArray("TaxList");
                        double rate = 0;
                        for (int k = 0; k < array.length(); k++) {
                            JSONObject jsonObject1 = array.getJSONObject(k);
                            final String TaxId = jsonObject1.getString("TaxId");
                            final String TaxType = jsonObject1.getString("TaxType");
                            final String TaxRateName = jsonObject1.getString("TaxRateName");
                            final String TaxRate = jsonObject1.getString("TaxRate");
                            rate = rate + Double.parseDouble(TaxRate);
                            TaxList taxList = new TaxList();
                            taxList.setTaxId(TaxId);
                            taxList.setTaxRateName(TaxRateName);
                            taxList.setTaxRate(TaxRate);
                            taxList.setTaxType(TaxType);
                            taxListArrayList.add(taxList);
                        }
                        Log.d("rate", rate + "");
                        ListTaxGroup listTaxGroup = new ListTaxGroup();
                        listTaxGroup.setTaxGroupRate(String.valueOf(rate));
                        listTaxGroup.setGroupName(strGroupName);
                        listTaxGroup.setTaxGroupId(strTaxGroupId);
                        listTaxGroup.setTaxList(taxListArrayList);
                        listTaxGroupArrayList.add(listTaxGroup);
                    }
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    SpinnerCustomAdapter dataAdapter = new SpinnerCustomAdapter(SalesItemActivity.this,
                            listTaxGroupArrayList);
                    sp_tax_group.setAdapter(dataAdapter);
                    if (strTaxId != null) {
                        for (int i = 0; i < listTaxGroupArrayList.size(); i++) {
                            if (strTaxId.equals(listTaxGroupArrayList.get(i).getGroupName())) {
                                sp_tax_group.setSelection(i);
                            }
                        }
                    }


                }
            });
        }
    }

    // getting datepicker dialog
    private void getPaymentDate() {
        calendar_payment = Calendar.getInstance();
        date_Year_payment = calendar_payment.get(Calendar.YEAR);
        date_Month_payment = calendar_payment.get(Calendar.MONTH);
        date_Day_payment = calendar_payment.get(Calendar.DATE);
        DatePickerDialog payentDateDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strPaymentDate =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                        +  mFormat.format(Double.valueOf(dayOfMonth));

                et_date.setText(strPaymentDate);
            }
        }, date_Year_payment, date_Month_payment, date_Day_payment
        );
        payentDateDialog.show();


    }

    void getChequeDate() {
        calendar_cheque = Calendar.getInstance();
        date_Year_cheque = calendar_cheque.get(Calendar.YEAR);
        date_Month_cheque = calendar_cheque.get(Calendar.MONTH);
        date_Day_cheque = calendar_cheque.get(Calendar.DATE);
        DatePickerDialog chequeDateDialog = new DatePickerDialog(
                this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                strChequeDate =  year + "-" +  mFormat.format(Double.valueOf(month)) + "-"
                        +  mFormat.format(Double.valueOf(dayOfMonth));
                et_cheque_deposit_date.setText(strChequeDate);
            }
        }, date_Year_cheque, date_Month_cheque, date_Day_cheque
        );
        chequeDateDialog.show();

    }


    public class AsyncGetDetailsByInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //pb_category_list.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String invoiceId = strings[0];
            invoiceItemDetailsArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/GetSalesDetails";
            String METHOD_NAME = "GetSalesDetails";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("invoiceId", invoiceId);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            if (!TextUtils.isEmpty(s)) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    final String PartyName = jsonObject.getString("PartyName");
                    final String InvoiceDate = jsonObject.getString("InvoiceDate");
                    final String DiscountAmount = jsonObject.getString("DiscountAmount");
                    final String InvoicePendingAmount = jsonObject.getString("InvoicePendingAmount");
                    final String IsCashSale = jsonObject.getString("IsCashSale");
                    final String InvoicePaidAmount = jsonObject.getString("InvoicePaidAmount");
                    final String InvoiceSubTotal = jsonObject.getString("InvoiceSubTotal");
                    final String InvoiceAmount = jsonObject.getString("InvoiceAmount");
                    final String PartyId = jsonObject.getString("PartyId");
                    JSONArray jsonArray = jsonObject.getJSONArray("ListInvoiceItems");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        final String ProductName = object.getString("ProductName");
                        final String ItemPrice = object.getString("ItemPrice");
                        final String ItemQty = object.getString("ItemQty");
                        final String ItemSubTotalAmount = object.getString("ItemSubTotalAmount");
                        final String UnitName = object.getString("UnitName");
                        final String TaxAmount = object.getString("TaxAmount");
                        final String ItemDiscountAmount = object.getString("ItemDiscountAmount");
                        final String SalesInvoiceItemId = object.getString("SalesInvoiceItemId");
                        InvoiceItemDetails invoiceItemDetails = new InvoiceItemDetails();
                        invoiceItemDetails.setProductName(ProductName);
                        invoiceItemDetails.setItemPrice(ItemPrice);
                        invoiceItemDetails.setItemQty(ItemQty);
                        invoiceItemDetails.setItemSubTotalAmount(ItemSubTotalAmount);
                        invoiceItemDetails.setUnitName(UnitName);
                        invoiceItemDetails.setTaxAmount(TaxAmount);
                        invoiceItemDetails.setItemDiscountAmount(ItemDiscountAmount);
                        invoiceItemDetails.setSalesInvoiceItemId(SalesInvoiceItemId);
                        invoiceItemDetailsArrayList.add(invoiceItemDetails);
                        if (adapter
                                != null) {
                            adapter
                                    .notifyDataSetChanged();
                        }
                    }

                    autoCompleteTextView.setText(PartyName);

                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String formatedDate = null;
                    try {
                        date = simpleDateFormat.parse(InvoiceDate);
                        formatedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    et_date.setText(formatedDate);
                    et_party_balance.setText(strPartyBalance);
                    et_discount_amount.setText(DiscountAmount);

                    et_received_amount.setText(InvoicePaidAmount);
                    et_balance.setText(InvoicePendingAmount);
                    strPartyId = PartyId;
                    appNameNavBar.setText("Mr/Ms. " + PartyName);
                    if (IsCashSale.equals("true")) {
                        cashOnly.setChecked(true);
                    } else {
                        cashOnly.setChecked(false);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                ll_item_sub_total.setVisibility(View.VISIBLE);
                adapter = new OrderItemDetailsAdapter(invoiceItemDetailsArrayList, SalesItemActivity.this);
                rv_sales_item_details.setHasFixedSize(false);
                rv_sales_item_details.setLayoutManager(new LinearLayoutManager(SalesItemActivity.this));
                rv_sales_item_details.setNestedScrollingEnabled(true);
                rv_sales_item_details.setAdapter(adapter);

                double total = 0, qty = 0, taxAmount = 0, discountAmount = 0;
                for (int j = 0; j < invoiceItemDetailsArrayList.size(); j++) {
                    total = total + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemSubTotalAmount());
                    qty = qty + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                    taxAmount = taxAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                    discountAmount = discountAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemDiscountAmount());
                    tv_order_item_subtotal.setText(df.format(total));
                    ll_item_sub_total.setVisibility(View.VISIBLE);
                    ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                    tv_order_item_total_qty.setText(df.format(qty));
                    et_discount_percentage.setText(df.format(0.00));

                    Log.d("total", total + "");
                }
                et_total_amount.setText(total + "");
                if (invoiceItemDetailsArrayList.size() >= 3) {
                    final float scale = getResources().getDisplayMetrics().density;
                    int pixels = (int) (450 * scale + 0.5f);
                    int pixels1 = (int) (390 * scale + 0.5f);
                    ll_order_item_recyclerview.getLayoutParams().height = pixels;
                    rv_sales_item_details.getLayoutParams().height = pixels1;
                }
            } else {
                //pb_category_list.setVisibility(View.GONE);
                Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public class AsyncCallDeleteSales extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String strSaleId = strings[0];

            final ArrayList<TaxList> taxListArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/DeleteSalesInvoice";
            String METHOD_NAME = "DeleteSalesInvoice";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("salesId", strSaleId);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    finish();
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            PartyInvoiceActivity.isRefreshed = true;
            PartyInvoiceAdapter.isEditClicked = false;
        }
    }


    public class AsyncGetItemDetailsByInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {

            String invoiceId = strings[0];
            invoiceItemDetailsArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/GetSalesInvoiceItemById";
            String METHOD_NAME = "GetSalesInvoiceItemById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("invoiceItemId", invoiceId);
                Request.addProperty("partyId", strPartyId);
                Request.addProperty("time", strTimeStatus);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return resultString.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                if (!TextUtils.isEmpty(s)) {
                    JSONArray jsonArray = new JSONArray(s);
                    final JSONObject jsonObject = jsonArray.getJSONObject(0);
                    final String ProductName = jsonObject.getString("ProductName");
                    final String PurchasePrice = jsonObject.getString("PurchasePrice");
                    final String SalePrice = jsonObject.getString("SalePrice");
                    final String ItemQty = jsonObject.getString("ItemQty");
                    final String ItemTotalAmount = jsonObject.getString("ItemTotalAmount");
                    final String ItemSubTotalAmount = jsonObject.getString("ItemSubTotalAmount");
                    final String ItemDiscountAmount = jsonObject.getString("ItemDiscountAmount");
                    final String FreeQuantity = jsonObject.getString("FreeQuantity");
                    final String TaxGroupName = jsonObject.getString("TaxGroupName");
                    final String ItemMasterId = jsonObject.getString("ItemMasterId");
                    final String AmPmSlot = jsonObject.getString("AmPmSlot");
                    autoCompleteTextViewProduct.setText(ProductName);
                    et_qty_item.setText(ItemQty);
                    et_free_qty_item.setText(FreeQuantity);
                    tv_subtotal_item.setText(ItemSubTotalAmount);
                    et_discount_amount_item.setText(ItemDiscountAmount);
                    tv_total_amount_item.setText(ItemTotalAmount);
                    strPurchasePrice = PurchasePrice;
                    strSalesPrice = SalePrice;
                    strProductId = ItemMasterId;
                    strTimeStatus = AmPmSlot;
                    if (AmPmSlot.equals("AM")){
                        cardAM();
                    }
                    else {
                        cardPM();
                    }
                    AsyncUnitList asyncUnitList = new AsyncUnitList();
                    asyncUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ItemMasterId, strPartyId);
                    if (TaxGroupName != null) {
                        for (int i = 0; i < listTaxGroupArrayList.size(); i++) {
                            if (TaxGroupName.equals(listTaxGroupArrayList.get(i).getGroupName())) {
                                sp_tax_group.setSelection(i);
                            }
                        }
                    }

                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ll_order_item_recyclerview.setVisibility(View.VISIBLE);
            ll_item_sub_total.setVisibility(View.VISIBLE);
            adapter = new OrderItemDetailsAdapter(invoiceItemDetailsArrayList, SalesItemActivity.this);
            rv_sales_item_details.setHasFixedSize(false);
            rv_sales_item_details.setLayoutManager(new LinearLayoutManager(SalesItemActivity.this));
            rv_sales_item_details.setNestedScrollingEnabled(true);
            rv_sales_item_details.setAdapter(adapter);

            double total = 0, qty = 0, taxAmount = 0, discountAmount = 0;
            for (int j = 0; j < invoiceItemDetailsArrayList.size(); j++) {
                total = total + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemSubTotalAmount());
                qty = qty + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                taxAmount = taxAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                discountAmount = discountAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemDiscountAmount());
                tv_order_item_subtotal.setText(df.format(total));
                ll_item_sub_total.setVisibility(View.VISIBLE);
                ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                tv_order_item_total_qty.setText(df.format(qty));
                et_discount_percentage.setText(df.format(0.00));
            }
            if (invoiceItemDetailsArrayList.size() >= 3) {
                final float scale = getResources().getDisplayMetrics().density;
                int pixels = (int) (450 * scale + 0.5f);
                int pixels1 = (int) (390 * scale + 0.5f);
                ll_order_item_recyclerview.getLayoutParams().height = pixels;
                rv_sales_item_details.getLayoutParams().height = pixels1;
            }
        }
    }

    public class AsyncCallDeleteSaleItem extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String strSaleId = strings[0];
            String SOAP_ACTION = "http://tempuri.org/DeleteSalesInvoiceItem";
            String METHOD_NAME = "DeleteSalesInvoiceItem";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("salesItemId", strSaleId);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    AsyncCallOrderItemListList callOrderItemListList = new AsyncCallOrderItemListList();
                    callOrderItemListList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", strInvoiceNo);
                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //PartyInvoiceActivity.isRefreshed = true;
        }
    }

    public class AsyncNextDayOrderDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {

            String partyId = strings[0];
            String date = strings[1];
            invoiceItemDetailsArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/GetPartySalesDetailsByDate";
            String METHOD_NAME = "GetPartySalesDetailsByDate";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("partyId", partyId);
                Request.addProperty("invDate", date);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (!TextUtils.isEmpty(responseJSON)) {
                    final JSONObject jsonObject = new JSONObject(responseJSON);
                    if (jsonObject.getString("Orders").equals("true")) {
                        isNextDayOrderAvailable = true;
                        strIsNextDayOrderGenerated = jsonObject.getString("Orders");
                        final String PartyName = jsonObject.getString("PartyName");
                        final String InvoiceDate = jsonObject.getString("InvoiceDate");
                        final String DiscountAmount = jsonObject.getString("DiscountAmount");
                        final String InvoicePendingAmount = jsonObject.getString("InvoicePendingAmount");
                        final String IsCashSale = jsonObject.getString("IsCashSale");
                        final String InvoicePaidAmount = jsonObject.getString("InvoicePaidAmount");
                        final String InvoiceSubTotal = jsonObject.getString("InvoiceSubTotal");
                        final String InvoiceNo = jsonObject.getString("InvoiceNo");
                        final String PartyId = jsonObject.getString("PartyId");
                        final String InvoiceAmount = jsonObject.getString("InvoiceAmount");
                        JSONArray jsonArray = jsonObject.getJSONArray("ListInvoiceItems");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            final String ProductName = object.getString("ProductName");
                            final String ItemPrice = object.getString("ItemPrice");
                            final String ItemQty = object.getString("ItemQty");
                            final String ItemSubTotalAmount = object.getString("ItemTotalAmount");
                            final String UnitName = object.getString("UnitName");
                            final String TaxAmount = object.getString("TaxAmount");
                            final String ItemDiscountAmount = object.getString("ItemDiscountAmount");
                            final String SalesInvoiceItemId = object.getString("SalesInvoiceItemId");
                            InvoiceItemDetails invoiceItemDetails = new InvoiceItemDetails();
                            invoiceItemDetails.setProductName(ProductName);
                            invoiceItemDetails.setItemPrice(ItemPrice);
                            invoiceItemDetails.setItemQty(ItemQty);
                            invoiceItemDetails.setItemSubTotalAmount(ItemSubTotalAmount);
                            invoiceItemDetails.setUnitName(UnitName);
                            invoiceItemDetails.setTaxAmount(TaxAmount);
                            invoiceItemDetails.setItemDiscountAmount(ItemDiscountAmount);
                            invoiceItemDetails.setSalesInvoiceItemId(SalesInvoiceItemId);
                            invoiceItemDetailsArrayList.add(invoiceItemDetails);
                            if (adapter
                                    != null) {
                                adapter
                                        .notifyDataSetChanged();
                            }
                            strInvoiceEdit = SalesInvoiceItemId;
                        }
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                autoCompleteTextView.setText(PartyName);

                                Date date = new Date();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                String formatedDate = null;
                                try {
                                    date = simpleDateFormat.parse(InvoiceDate);
                                    formatedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                strInvoiceNo = InvoiceNo;
                                et_date.setText(formatedDate);
                                et_party_balance.setText(strPartyBalance);
                                et_discount_amount.setText(DiscountAmount);
                                et_total_amount.setText(InvoiceAmount);
                                et_received_amount.setText(InvoicePaidAmount);
                                et_balance.setText(InvoicePendingAmount);
                                strPartyId = PartyId;
                                strPreviousAmount = InvoicePendingAmount;
                                appNameNavBar.setText("Mr/Ms. " + PartyName);
                                if (IsCashSale.equals("true")) {
                                    cashOnly.setChecked(true);
                                } else {
                                    cashOnly.setChecked(false);
                                }
                                btnDeleteOrder.setVisibility(View.VISIBLE);
                                btnCancelOrder.setVisibility(View.GONE);
                                btnEditOrder.setVisibility(View.VISIBLE);
                                btnSaveOrder.setVisibility(View.GONE);
                                autoCompleteTextView.setEnabled(false);
                                et_date.setEnabled(false);
                                et_party_balance.setEnabled(false);
                                et_discount_amount.setEnabled(false);
                                et_discount_percentage.setEnabled(false);
                                et_round_of.setEnabled(false);
                                et_received_amount.setEnabled(false);
                                et_total_amount.setEnabled(false);
                                et_balance.setEnabled(false);
                                et_cheque_deposit_date.setEnabled(false);
                                et_referenceNo.setEnabled(false);
                                ll_order_item_details.setEnabled(false);
                                cb_round_off.setEnabled(false);
                                et_payment_type.setEnabled(false);
                                isClicked = false;
                                tv_add_sales_item.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        isNextDayOrderAvailable = false;
                        btnDeleteOrder.setVisibility(View.GONE);
                        btnCancelOrder.setVisibility(View.VISIBLE);
                        btnEditOrder.setVisibility(View.GONE);
                        btnSaveOrder.setVisibility(View.VISIBLE);
                        autoCompleteTextView.setEnabled(true);
                        et_date.setEnabled(true);
                        et_party_balance.setEnabled(true);
                        et_discount_amount.setEnabled(true);
                        et_discount_percentage.setEnabled(true);
                        et_round_of.setEnabled(true);
                        et_received_amount.setEnabled(true);
                        et_total_amount.setEnabled(true);
                        et_balance.setEnabled(true);
                        et_cheque_deposit_date.setEnabled(true);
                        et_referenceNo.setEnabled(true);
                        ll_order_item_details.setEnabled(true);
                        cb_round_off.setEnabled(true);
                        et_payment_type.setEnabled(true);
                        isClicked = true;
                        tv_add_sales_item.setVisibility(View.VISIBLE);
                    }


                } else {
                    Toast.makeText(SalesItemActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return partyId;
        }

        @Override
        protected void onPostExecute(String s) {
            ll_order_item_recyclerview.setVisibility(View.VISIBLE);
            //ll_item_sub_total.setVisibility(View.VISIBLE);
            adapter = new OrderItemDetailsAdapter(invoiceItemDetailsArrayList, SalesItemActivity.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_sales_item_details.setHasFixedSize(false);
                    rv_sales_item_details.setLayoutManager(new LinearLayoutManager(SalesItemActivity.this));
                    rv_sales_item_details.setNestedScrollingEnabled(true);
                    rv_sales_item_details.setAdapter(adapter);

                    double total = 0, qty = 0, taxAmount = 0, discountAmount = 0;
                    for (int j = 0; j < invoiceItemDetailsArrayList.size(); j++) {
                        total = total + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemSubTotalAmount());
                        qty = qty + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                        taxAmount = taxAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                        discountAmount = discountAmount + Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemDiscountAmount());
                        tv_order_item_subtotal.setText(df.format(total));
                        ll_item_sub_total.setVisibility(View.VISIBLE);
                        ll_order_item_recyclerview.setVisibility(View.VISIBLE);
                        tv_order_item_total_qty.setText(df.format(qty));
                        et_discount_percentage.setText(df.format(0.00));
                    }
                    if (invoiceItemDetailsArrayList.size() >= 3) {
                        final float scale = getResources().getDisplayMetrics().density;
                        int pixels = (int) (450 * scale + 0.5f);
                        int pixels1 = (int) (390 * scale + 0.5f);
                        ll_order_item_recyclerview.getLayoutParams().height = pixels;
                        rv_sales_item_details.getLayoutParams().height = pixels1;
                    }

                }

            });
        }
    }

    void filter(String text) {
        if (!productDetailsPojos.isEmpty()) {
            List<ProductDetailsPojo> temp = new ArrayList();
            for (ProductDetailsPojo partyDetailsPojo : productDetailsPojos) {
                String name = partyDetailsPojo.getProductName().toLowerCase();
                if (name.contains(text)) {
                    temp.add(partyDetailsPojo);
                }
            }
            productAdapter.updateList(temp);
        }

    }
}
