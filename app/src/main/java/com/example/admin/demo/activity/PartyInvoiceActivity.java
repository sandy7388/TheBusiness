package com.example.admin.demo.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.demo.R;
import com.example.admin.demo.adapter.NextOrderAdapter;
import com.example.admin.demo.adapter.PartyInvoiceAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.model.PartyInvoicePojo;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.session.SessionManagement;
import com.example.admin.demo.util.Common;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PartyInvoiceActivity extends AppCompatActivity implements
        View.OnClickListener,PartyDetails {
    private FloatingActionButton fb_invoice_party;
    private RecyclerView rv_party_invoice;
    private TextView tv_balance_invoice,appNameNavBar,tv_invoice_customer_name,textViewCloseSheet;
    private Toolbar toolbar_invoice;
    private ProgressBar pb_list_party_invoice;
    private SoapPrimitive resultString;
    private ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList;
    private ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList;
    private PartyInvoiceAdapter partyInvoiceAdapter;
    private String strPartyName,strPartyId,strInvoiceId,strPartyBalance,strTripId,
            strRouteId,strNickName,strCompanyName,compid;
    private DecimalFormat df;
    private BottomSheetBehavior mBottomSheetBehavior;
    public static boolean isRefreshed = false;
    public static boolean checkParty = false;
    public static boolean isNextDayOrder = false;
    private ImageView fab_all_menu,imageVieweCloseSheet;
    private LinearLayout ll_bottom_sale,ll_bottom_nxt_day_order,ll_bottom_purchase,
            ll_bottom_payment,ll_bottom_expenses,ll_swipe_purchase,
            ll_swipe_sale_req,ll_swipe_sale_order, ll_swipe_payment,
            ll_swipe_report,ll_swipe_accounts,ll_swipe_party,
            ll_swipe_items, ll_swipe_expenses;
    private RelativeLayout ll_layout;
    private boolean back_status = false;
    public static boolean isEditParty = false;
    private double total;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    private
    Button mScan, mPrint, mDisc;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_invoice);
        fb_invoice_party = findViewById(R.id.fb_invoice_party);
        rv_party_invoice = findViewById(R.id.rv_party_invoice);
        tv_balance_invoice = findViewById(R.id.tv_balance_invoice);
        toolbar_invoice = findViewById(R.id.toolbar_invoice);
        pb_list_party_invoice = findViewById(R.id.pb_list_party_invoice);
        tv_invoice_customer_name = findViewById(R.id.tv_invoice_customer_name);
        ll_layout = findViewById(R.id.ll_layout);
        fab_all_menu = findViewById(R.id.fab_all_menu);
        ll_bottom_sale = findViewById(R.id.ll_bottom_sale);
        ll_bottom_nxt_day_order = findViewById(R.id.ll_bottom_nxt_day_order);
        ll_bottom_purchase = findViewById(R.id.ll_bottom_purchase);
        ll_bottom_payment = findViewById(R.id.ll_bottom_payment);
        ll_bottom_expenses = findViewById(R.id.ll_bottom_expenses);
        ll_swipe_party = findViewById(R.id.ll_swipe_party);
        ll_swipe_items = findViewById(R.id.ll_swipe_items);
        ll_swipe_expenses = findViewById(R.id.ll_swipe_expenses);
        ll_swipe_accounts = findViewById(R.id.ll_swipe_accounts);
        ll_swipe_payment = findViewById(R.id.ll_swipe_payment);
        ll_swipe_sale_order = findViewById(R.id.ll_swipe_sale_order);
        ll_swipe_sale_req = findViewById(R.id.ll_swipe_sale_req);
        ll_swipe_purchase = findViewById(R.id.ll_swipe_purchase);
        imageVieweCloseSheet = findViewById(R.id.iv_close);
        textViewCloseSheet = findViewById(R.id.tv_close);
        fb_invoice_party.setOnClickListener(this);
        ll_layout.setOnClickListener(this);
        fab_all_menu.setOnClickListener(this);
        imageVieweCloseSheet.setOnClickListener(this);
        textViewCloseSheet.setOnClickListener(this);
        ll_bottom_sale.setOnClickListener(this);
        ll_bottom_nxt_day_order.setOnClickListener(this);
        ll_bottom_purchase.setOnClickListener(this);
        ll_bottom_payment.setOnClickListener(this);
        ll_bottom_expenses.setOnClickListener(this);
        ll_swipe_items.setOnClickListener(this);
        ll_swipe_party.setOnClickListener(this);
        ll_swipe_expenses.setOnClickListener(this);
        ll_swipe_accounts.setOnClickListener(this);
        ll_swipe_payment.setOnClickListener(this);
        ll_swipe_sale_order.setOnClickListener(this);
        ll_swipe_sale_req.setOnClickListener(this);
        ll_swipe_purchase.setOnClickListener(this);
        View view = toolbar_invoice.getRootView();
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            strCompanyName = user.get(SessionManagement.KEY_COMPANY_NAME);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
        df = new DecimalFormat("#0.00");
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        if (MainActivity.isSale){

            ll_bottom_sale.setVisibility(View.VISIBLE);
            ll_bottom_purchase.setVisibility(View.GONE);

        }
        else if (MainActivity.isPurchase){

            ll_bottom_sale.setVisibility(View.GONE);
            ll_bottom_purchase.setVisibility(View.VISIBLE);

        }
        Functions.setToolBar(PartyInvoiceActivity.this, toolbar_invoice, "Purchase List", true);
        Intent intent = getIntent();
        if (intent != null){
            if (intent.getStringExtra("NICK_NAME") != null){
                appNameNavBar.setText("Mr/Ms. " + intent.getStringExtra("NICK_NAME"));
            }
            strPartyName = intent.getStringExtra("PARTY_NAME");
            strPartyId= intent.getStringExtra("PARTY_ID");
            strPartyBalance = intent.getStringExtra("PARTY_BALANCE");
            double partyBalance = Double.parseDouble(strPartyBalance);
            strTripId = intent.getStringExtra("TRIP_ID");
            strRouteId = intent.getStringExtra("ROUTE_ID");
            appNameNavBar.setText("Mr/Ms. " + strPartyName);
            tv_balance_invoice.setText("Rs." + df.format(partyBalance));
            AsyncCallInvoiceList callInvoiceList = new AsyncCallInvoiceList();
            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strPartyId);
        }

        View bottomSheet = findViewById(R.id.bottom_sheet_registration);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fab_all_menu.setVisibility(View.VISIBLE);
                        //fab_all_menu.show();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        fab_all_menu.setVisibility(View.GONE);
                        //fab_all_menu.hide();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        fab_all_menu.setVisibility(View.GONE);
                        //fab_all_menu.hide();
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fab_all_menu.setVisibility(View.GONE);
                        //fab_all_menu.hide();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        fab_all_menu.setVisibility(View.VISIBLE);
                        //fab_all_menu.show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_invoice_party:
                    Intent intent = new Intent(this, SalesItemActivity.class);
                    intent.putExtra("PARTY_ID", strPartyId);
                    intent.putExtra("PARTY_NAME", strPartyName);
                    intent.putExtra("PARTY_BALANCE", strPartyBalance);
                    intent.putExtra("TRIP_ID", strTripId);
                    intent.putExtra("ROUTE_ID", strRouteId);
                if (MainActivity.isPurchase) {
                    MainActivity.isPurchase = true;
                    startActivity(intent);
                }
                else {
                    MainActivity.isSale = true;
                    startActivity(intent);
                }
                break;
            case R.id.fab_all_menu:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    //If state is in collapse mode expand it
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab_all_menu.setVisibility(View.GONE);
                    fb_invoice_party.hide();
                }
                else{
                    //else if state is expanded collapse it
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab_all_menu.setVisibility(View.VISIBLE);
                    fb_invoice_party.show();
                }
                break;

            case R.id.ll_layout:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab_all_menu.setVisibility(View.GONE);
                    fb_invoice_party.hide();
                }

                break;

            case R.id.tv_close:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab_all_menu.setVisibility(View.VISIBLE);
                fb_invoice_party.show();
                break;

            case R.id.ll_bottom_sale:
                Intent intentSales = new Intent(this,SalesItemActivity.class);
                intentSales.putExtra("PARTY_ID", strPartyId);
                intentSales.putExtra("PARTY_NAME", strPartyName);
                intentSales.putExtra("PARTY_BALANCE", strPartyBalance);
                intentSales.putExtra("TRIP_ID", strTripId);
                intentSales.putExtra("ROUTE_ID", strRouteId);
                MainActivity.isSale = true;
                if (MainActivity.isPMOrder){
                    MainActivity.isPMOrder = true;
                }
                else {
                    MainActivity.isPMOrder = false;
                }

                MainActivity.isPurchase = false;
                MainActivity.isCashSale = false;
                PartyInvoiceAdapter.isEditClicked = false;
                NextOrderAdapter.isEditClicked = false;
                isNextDayOrder = false;
                startActivity(intentSales);
                //finish();
                break;
            case R.id.ll_bottom_nxt_day_order:
                Intent nextDayIntent = new Intent(this,SalesItemActivity.class);
                nextDayIntent.putExtra("PARTY_ID", strPartyId);
                nextDayIntent.putExtra("PARTY_NAME", strPartyName);
                nextDayIntent.putExtra("PARTY_BALANCE", strPartyBalance);
                nextDayIntent.putExtra("TRIP_ID", strTripId);
                nextDayIntent.putExtra("ROUTE_ID", strRouteId);
                MainActivity.isSale = false;
                MainActivity.isPMOrder = false;
                MainActivity.isPurchase = false;
                MainActivity.isCashSale = false;
                PartyInvoiceAdapter.isEditClicked = false;
                NextOrderAdapter.isEditClicked = false;
                isNextDayOrder = true;
                startActivity(nextDayIntent);
                back_status = false;
                //finish();
                break;
            case R.id.ll_bottom_purchase:
                Intent intentPurchase = new Intent(this,SalesItemActivity.class);
                intentPurchase.putExtra("PARTY_ID", strPartyId);
                intentPurchase.putExtra("PARTY_NAME", strPartyName);
                intentPurchase.putExtra("PARTY_BALANCE", strPartyBalance);
                intentPurchase.putExtra("TRIP_ID", strTripId);
                intentPurchase.putExtra("ROUTE_ID", strRouteId);
                MainActivity.isSale = false;
                MainActivity.isPMOrder = false;
                MainActivity.isPurchase = true;
                MainActivity.isCashSale = false;
                PartyInvoiceAdapter.isEditClicked = false;
                NextOrderAdapter.isEditClicked = false;
                isNextDayOrder = false;
                startActivity(intentPurchase);
                back_status = false;
                //finish();
                break;
            case R.id.ll_bottom_payment:
                Intent intentPayment = new Intent(this,PaymentActivity.class);
                intentPayment.putExtra("PARTY_ID", strPartyId);
                intentPayment.putExtra("PARTY_NAME", strPartyName);
                intentPayment.putExtra("PARTY_BALANCE", strPartyBalance);
                intentPayment.putExtra("TRIP_ID", strTripId);
                intentPayment.putExtra("ROUTE_ID", strRouteId);
                checkParty = true;
                MainActivity.isPMOrder = false;
                startActivity(intentPayment);
                break;
            case R.id.ll_bottom_expenses:
                Intent intentPaymentOut = new Intent(this,PaymentActivity.class);
                intentPaymentOut.putExtra("PARTY_ID", strPartyId);
                intentPaymentOut.putExtra("PARTY_NAME", strPartyName);
                intentPaymentOut.putExtra("PARTY_BALANCE", strPartyBalance);
                intentPaymentOut.putExtra("TRIP_ID", strTripId);
                intentPaymentOut.putExtra("ROUTE_ID", strRouteId);
                checkParty = true;
                MainActivity.isPMOrder = false;
                startActivity(intentPaymentOut);
                break;

            case R.id.ll_swipe_items:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(this, ActivityItemList.class));
                back_status = false;
                break;
            case R.id.ll_swipe_expenses:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(this, ExpenseListActivity.class));
                back_status = false;
                break;

            case R.id.ll_swipe_sale_req:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //startActivity(new Intent(MainActivity.this, BankListActivity.class));
                back_status = false;
                break;

            case R.id.ll_swipe_sale_order:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent1 = new Intent(this, PartyListActivity.class);
                MainActivity.isSale = true ;
                PartyInvoiceActivity.isRefreshed = false;
                startActivity(intent1);
                back_status = false;
                break;
            case R.id.ll_swipe_purchase:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intentpurchase = new Intent(this, PurchaseActivity.class);
                PurchaseActivity.isPurchaseActivityRefreshed = false;
                startActivity(intentpurchase);
                back_status = false;
                break;

            case R.id.ll_swipe_accounts:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(this, BankListActivity.class));
                back_status = false;
                break;

            case R.id.ll_swipe_party:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(this, PartyListActivity.class));
                back_status = false;
                break;
            case R.id.ll_swipe_payment:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(this, PaymentListActivity.class));
                back_status = false;
                break;
        }
    }

    @Override
    public void PartyId(String id) {
        strInvoiceId = id;
    }

    public class AsyncCallInvoiceList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_list_party_invoice.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];


            return params[1];
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_party_invoice.setVisibility(View.GONE);
            partyInvoiceAdapter = new PartyInvoiceAdapter(getProduct(result),PartyInvoiceActivity.this );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_party_invoice.setNestedScrollingEnabled(false);
                    rv_party_invoice.setHasFixedSize(true);
                    rv_party_invoice.setLayoutManager(new LinearLayoutManager(PartyInvoiceActivity.this));
                    rv_party_invoice.setAdapter(partyInvoiceAdapter);

                }

            });
            double total = 0;
            if (partyInvoicePojoArrayList.size()>0){

                total = total + Double.parseDouble(partyInvoicePojoArrayList.get(0).getPartyCurrentBalance());
                strPartyBalance = total+"";
                tv_balance_invoice.setText("Rs." + df.format(total));
            }

            if (result.equals("success")) {
                Toast.makeText(PartyInvoiceActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(PartyInvoiceActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
    public ArrayList<PartyInvoicePojo> getProduct(String strPartyId){
        partyInvoicePojoArrayList = new ArrayList<>();
        invoiceItemDetailsArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/PartyAllSalesList";
        String METHOD_NAME = "PartyAllSalesList";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("partyId", strPartyId);
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
                final JSONObject object = new JSONObject(responseJSON);
                invoiceItemDetailsArrayList = new ArrayList<>();
                JSONArray jsonArray = object.getJSONArray("ListInvoice");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    final String SalesInvoiceId = jsonObject.getString("SalesInvoiceId");
                    final String InvoiceNo = jsonObject.getString("InvoiceNo");
                    final String InvoiceDate = jsonObject.getString("InvoiceDate");
                    final String InvoiceAmount = jsonObject.getString("InvoiceAmount");
                    final String InvoicePaidAmount = jsonObject.getString("InvoicePaidAmount");
                    final String InvoicePendingAmount = jsonObject.getString("InvoicePendingAmount");
                    final String RouteId = jsonObject.getString("RouteId");
                    final String TripId = jsonObject.getString("TripId");
                    final String PartyCurrentBalance = jsonObject.getString("PartyCurrentBalance");
                    final String InvoiceStatus = jsonObject.getString("InvoiceStatus");
                    final String PartyName = jsonObject.getString("PartyName");
                    invoiceItemDetailsArrayList = new ArrayList<>();
                    JSONArray array = jsonObject.getJSONArray("ListInvoiceItems");
                    for (int j=0;j<array.length();j++){
                        JSONObject jsonObject1 = array.getJSONObject(j);
                        final String ProductName = jsonObject1.getString("ProductName");
                        final String ItemPrice = jsonObject1.getString("ItemPrice");
                        final String ItemQty = jsonObject1.getString("ItemQty");
                        final String ItemSubTotalAmount = jsonObject1.getString("ItemSubTotalAmount");
                        final String TaxAmount = jsonObject1.getString("TaxAmount");
                        invoiceItemDetailsArrayList.add(new InvoiceItemDetails(ItemSubTotalAmount,ProductName,
                                TaxAmount,ItemPrice,ItemQty,Double.parseDouble(ItemPrice) * Double.parseDouble(ItemQty)));

                    }
                    partyInvoicePojoArrayList.add(new PartyInvoicePojo(InvoiceDate,RouteId,InvoicePaidAmount,
                            TripId,InvoicePendingAmount,InvoiceAmount,InvoiceNo,SalesInvoiceId,
                            InvoiceStatus,PartyName,PartyCurrentBalance,invoiceItemDetailsArrayList));

                    if (partyInvoiceAdapter
                            != null) {
                        partyInvoiceAdapter
                                .notifyDataSetChanged();
                    }

                }

            } else {
                Toast.makeText(PartyInvoiceActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return partyInvoicePojoArrayList;


    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        if (isRefreshed){
            Toast.makeText(this, "Activity refreshed", Toast.LENGTH_SHORT).show();
            AsyncCallInvoiceList callInvoiceList = new AsyncCallInvoiceList();
            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strPartyId);
            PartyInvoiceAdapter.isEditClicked = false;
            isRefreshed = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            switch (event.getAction()){
                case KeyEvent.ACTION_DOWN:
                    isRefreshed = true;
                    PartyInvoiceAdapter.isEditClicked = false;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.party_invoice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_party:
                Intent intent = new Intent(this,AddPartyActivity.class);
                isEditParty = true;
                if (MainActivity.isPurchase){
                    PurchaseActivity.isPurchase = true;
                }
                else {
                    PurchaseActivity.isPurchase = false;
                }
                intent.putExtra("PARTY_ID",strPartyId);
                startActivity(intent);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void printData(final String name, final String no,final String date,
                          final String balance, final ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList){
        Thread t = new Thread() {
            public void run() {
                try {
                    ScanActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> mPairedDevices = ScanActivity.mBluetoothAdapter.getBondedDevices();
                    if (mPairedDevices.size() > 0) {
                        OutputStream os = ScanActivity.mBluetoothSocket
                                .getOutputStream();
                        String BILL = "";

                        BILL = "          Company Name  : "+strCompanyName + "\n"
                                +"           Party Name    : "+name +   "\n"
                                +"            Invoice No.  : "+ no+     "\n "
                                +"           Invoice Date : "+ date     +" \n"
                                +"             Party Balance:"+ balance  +"\n";
                        BILL = BILL
                                + "-----------------------------------------------\n";


                        BILL = BILL + String.format("%1$-21s %2$6s %3$6s %4$7s", "Item", "Qty", "Rate", "Total");
                        BILL = BILL + "\n";
                        BILL = BILL
                                + "-----------------------------------------------";

                        for (int i=0;i<invoiceItemDetailsArrayList.size();i++){
                            String name;
                            double total=0,qty=0,price=0;
                            name = invoiceItemDetailsArrayList.get(i).getProductName();
                            qty = Double.parseDouble(invoiceItemDetailsArrayList.get(i).getItemQty());
                            price = Double.parseDouble(invoiceItemDetailsArrayList.get(i).getItemPrice());
                            total = (qty) * (price);
                            BILL = BILL + "\n " + String.format("%1$-21s %2$6s %3$6s %4$7s", name, df.format(qty), df.format(price), df.format(total));
                        }

                        BILL = BILL
                                + "\n-----------------------------------------------";
                        BILL = BILL + "\n\n ";

                        double price = 0,qty = 0,tax = 0,total = 0;
                        for (int j = 0; j<invoiceItemDetailsArrayList.size();j++){
                            price = price+(invoiceItemDetailsArrayList.get(j).getTotal());
                            qty = qty+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                            tax = tax+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                            total = total + (price *qty) + tax;

                        }

                        BILL = BILL + "                   Total Qty:" + "      " + df.format(qty) + "\n";
                        BILL = BILL + "                   Total Tax:  " + "     " + df.format(tax) + "\n";
                        BILL = BILL + "                   Total Value:" + "     " + df.format(price) + "\n";

                        BILL = BILL
                                + "-----------------------------------------------\n";
                        BILL = BILL + "\n\n ";
                        os.write(BILL.getBytes());
                        //This is printer specific code you can comment ==== > Start

                        // Setting height
                        int gs = 29;
                        os.write(Common.intToByteArray(gs));
                        int h = 104;
                        os.write(Common.intToByteArray(h));
                        int n = 162;
                        os.write(Common.intToByteArray(n));

                        // Setting Width
                        int gs_width = 29;
                        os.write(Common.intToByteArray(gs_width));
                        int w = 119;
                        os.write(Common.intToByteArray(w));
                        int n_width = 2;
                        os.write(Common.intToByteArray(n_width));
                    } else {
                        Toast.makeText(PartyInvoiceActivity.this, "No device Connected", Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.e("ScanActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }


    public void logoutConfirmation(final String salesId, final String status){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure, You want to convert to sale");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AsyncCallConvertSale().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,salesId,status);

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private class AsyncCallConvertSale extends AsyncTask<String, Void, String> {

        String salesId,status;
        int position;

        @Override
        protected String doInBackground(String... params) {
            salesId = params[0];
            status = params[1];
            String SOAP_ACTION = "http://tempuri.org/UpdateSalesStatus";
            String METHOD_NAME = "UpdateSalesStatus";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("salesId", salesId);
                Request.addProperty("status", status);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (responseJSON.equals(null)) {
                    Toast.makeText(PartyInvoiceActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                } else {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PartyInvoiceActivity.this, "Successfully converted to sale", Toast.LENGTH_SHORT).show();
                            AsyncCallInvoiceList callInvoiceList = new AsyncCallInvoiceList();
                            callInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",strPartyId);
                        }
                    });

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error";

            }

            return "success";
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("success")) {

                Toast.makeText(PartyInvoiceActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(PartyInvoiceActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
