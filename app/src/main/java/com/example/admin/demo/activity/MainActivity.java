package com.example.admin.demo.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.admin.demo.adapter.CustomDrawerAdapter;
import com.example.admin.demo.adapter.expandable.CustomExpandableListAdapter;
import com.example.admin.demo.adapter.expandable.ExpandableListDataPump;
import com.example.admin.demo.functions.SharedPreferencesDatabase;
import com.example.admin.demo.item.DrawerItem;
import com.example.admin.demo.R;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.session.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private boolean back_status = false;
    private static final int PERIOD = 2000;
    private long lastPressedTime;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private CustomDrawerAdapter customDrawerAdapter;
    private static ArrayList<DrawerItem> drawerData;
    private DrawerLayout drawer;
    //private ListView mDrawerList;
    private CoordinatorLayout coordinatorLayout_drawer;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ImageView fab_all_menu,imageVieweCloseSheet;
    private RelativeLayout ll_layout;
    private RelativeLayout outer;
    private SessionManagement session;
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private TextView textViewUserName,textViewCloseSheet,textViewCompanyName,tv_manage_company;
    private LinearLayout ll_bottom_sale,ll_bottom_add_item,ll_bottom_add_party,
                        ll_bottom_payment,ll_bottom_expenses,ll_swipe_purchase,
            ll_swipe_sale_req,ll_swipe_sale_order, ll_swipe_payment,
            ll_swipe_report,ll_swipe_accounts,ll_swipe_party,
            ll_swipe_items, ll_swipe_expenses;
    public static boolean isSale = false;
    public static boolean isPurchase = false;
    public static boolean isCashSale = false;
    public static boolean isPMOrder = false;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = MainActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }
        sharedPreferencesDatabase = new SharedPreferencesDatabase(this);
        sharedPreferencesDatabase.createDatabase();
        outer = (RelativeLayout)findViewById(R.id.include_nav_drawer);
        textViewUserName = outer.findViewById(R.id.user_name);
        textViewCompanyName =  outer.findViewById(R.id.company_name);
        tv_manage_company = outer.findViewById(R.id.tv_manage_company);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordinatorLayout_drawer = findViewById(R.id.coordinatorLayout_drawer);
        ll_layout = findViewById(R.id.ll_layout);
        fab_all_menu = findViewById(R.id.fab_all_menu);
        ll_bottom_sale = findViewById(R.id.ll_bottom_sale);
        ll_bottom_add_item = findViewById(R.id.ll_bottom_add_item);
        ll_bottom_add_party = findViewById(R.id.ll_bottom_add_party);
        ll_bottom_payment = findViewById(R.id.ll_bottom_payment);
        ll_bottom_expenses = findViewById(R.id.ll_bottom_expenses);
        ll_swipe_party = findViewById(R.id.ll_swipe_party);
        ll_swipe_items = findViewById(R.id.ll_swipe_items);
        ll_swipe_expenses = findViewById(R.id.ll_swipe_expenses);
        ll_swipe_report = findViewById(R.id.ll_swipe_report);
        ll_swipe_accounts = findViewById(R.id.ll_swipe_accounts);
        ll_swipe_payment = findViewById(R.id.ll_swipe_payment);
        ll_swipe_sale_order = findViewById(R.id.ll_swipe_sale_order);
        ll_swipe_sale_req = findViewById(R.id.ll_swipe_sale_req);
        ll_swipe_purchase = findViewById(R.id.ll_swipe_purchase);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupClickListener(this);
        expandableListView.setOnChildClickListener(this);
        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            String name = user.get(SessionManagement.KEY_USER_NAME);
            String company = user.get(SessionManagement.KEY_COMPANY_NAME);
            if (company != null){
                textViewCompanyName.setText(company);
            }
            else {
                textViewCompanyName.setText("Company Name");

            }
            textViewUserName.setText(name);
        }
        drawer.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        addFragment(new FragmentHome(), getString(R.string.app_name));
        back_status = true;
        View bottomSheet = findViewById(R.id.bottom_sheet_registration);
        imageVieweCloseSheet = findViewById(R.id.iv_close);
        textViewCloseSheet = findViewById(R.id.tv_close);
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
        ll_layout.setOnClickListener(this);
        fab_all_menu.setOnClickListener(this);
        imageVieweCloseSheet.setOnClickListener(this);
        textViewCloseSheet.setOnClickListener(this);
        ll_bottom_sale.setOnClickListener(this);
        ll_bottom_add_item.setOnClickListener(this);
        ll_bottom_add_party.setOnClickListener(this);
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
        ll_swipe_report.setOnClickListener(this);
        tv_manage_company.setOnClickListener(this);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_all_menu:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    //If state is in collapse mode expand it
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab_all_menu.setVisibility(View.GONE);
                }
                else{
                    //else if state is expanded collapse it
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab_all_menu.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.ll_layout:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    fab_all_menu.setVisibility(View.GONE);
                }

                break;

            case R.id.tv_close:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab_all_menu.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_bottom_sale:
                Intent intent = new Intent(this,SalesItemActivity.class);
                isCashSale = true;
                startActivity(intent);
                break;
            case R.id.ll_bottom_add_item:
                startActivity(new Intent(MainActivity.this, NextDayOrderActivity.class));
                back_status = false;
                break;
            case R.id.ll_bottom_add_party:
                Intent intents = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intents);
                back_status = false;
                break;
            case R.id.ll_bottom_payment:
                PartyInvoiceActivity.checkParty = false;
                startActivity(new Intent(MainActivity.this,PaymentListActivity.class));
                break;
            case R.id.ll_bottom_expenses:
                startActivity(new Intent(MainActivity.this,PartyListActivity.class));
                break;

            case R.id.ll_swipe_items:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, ActivityItemList.class));
                back_status = false;
                break;
            case R.id.ll_swipe_expenses:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, ExpenseListActivity.class));
                back_status = false;
                break;
            case R.id.ll_swipe_report:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
                back_status = false;

            case R.id.ll_swipe_sale_req:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, NextDayOrderActivity.class));
                back_status = false;
                break;

            case R.id.ll_swipe_sale_order:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent1 = new Intent(MainActivity.this, PartyListActivity.class);
                isSale = true ;
                PartyInvoiceActivity.isRefreshed = false;
                startActivity(intent1);
                back_status = false;
                break;
            case R.id.ll_swipe_purchase:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intentpurchase = new Intent(MainActivity.this, PurchaseActivity.class);
                PurchaseActivity.isPurchaseActivityRefreshed = false;
                startActivity(intentpurchase);
                back_status = false;
                break;

            case R.id.ll_swipe_accounts:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, BankListActivity.class));
                back_status = false;
                break;

            case R.id.ll_swipe_party:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                startActivity(new Intent(MainActivity.this, PartyListActivity.class));
                back_status = false;
                break;
            case R.id.ll_swipe_payment:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                PartyInvoiceActivity.checkParty = false;
                startActivity(new Intent(MainActivity.this, PaymentListActivity.class));
                back_status = false;
                break;

            case R.id.tv_manage_company:
                startActivity(new Intent(this,CompanyListActivity.class));
                drawer.closeDrawer(Gravity.LEFT);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void addFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    if (back_status) {
                        if (event.getDownTime() - lastPressedTime < PERIOD) {
                            finish();
                        } else {
                            Snackbar.make(coordinatorLayout_drawer, "Are you Sure wants to exit!", Snackbar.LENGTH_SHORT).setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            }).show();
                            lastPressedTime = event.getEventTime();
                        }
                    } else {
                        addFragment(new FragmentHome(), "Home");
                        back_status = true;
                    }
            }
            return true;
        }
        return false;

    }

    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                Intent intents = new Intent(MainActivity.this, PartyListActivity.class);
                isSale = false ;
                isPMOrder = false;
                PartyInvoiceActivity.isRefreshed = false;
                startActivity(intents);
                back_status = false;
                break;
            case 1:
                Intent intentitems = new Intent(MainActivity.this, ActivityItemList.class);
                startActivity(intentitems);
                back_status = false;
                break;

            case 2:
                Intent intentpurchase = new Intent(MainActivity.this, PurchaseActivity.class);
                PurchaseActivity.isPurchaseActivityRefreshed = false;
                isPMOrder = false;
                startActivity(intentpurchase);
                back_status = false;
                break;

            case 3:
                Intent intentnext = new Intent(MainActivity.this, NextDayOrderActivity.class);
                //PurchaseActivity.isPurchaseActivityRefreshed = false;
                startActivity(intentnext);
                back_status = false;
                break;

            case 4:
                Intent intentbulk = new Intent(MainActivity.this, BulkOrderActivity.class);
                //PurchaseActivity.isPurchaseActivityRefreshed = false;
                startActivity(intentbulk);
                back_status = false;
                break;

            case 5:
                Intent intent1 = new Intent(MainActivity.this, PartyListActivity.class);
                isSale = true ;
                PartyInvoiceActivity.isRefreshed = false;
                isPMOrder = false;
                startActivity(intent1);
                back_status = false;
                break;

            case 6:
                Intent intent2 = new Intent(MainActivity.this, PartyListActivity.class);
                isSale = false;
                PartyInvoiceActivity.isRefreshed = false;
                isPMOrder = true;
                startActivity(intent2);
                back_status = false;
                break;

            //addFragment(new PartyMenu1());
            case 7:
                Intent intentpayment = new Intent(MainActivity.this, PaymentListActivity.class);
                startActivity(intentpayment);
                back_status = false;
                break;

            case 8:
                Intent intenttax = new Intent(MainActivity.this, TaxActivity.class);
                startActivity(intenttax);
                // addFragment(new FragmentHouse(), "House");
                back_status = false;
                break;
            case 9:
                Intent intentexpense = new Intent(MainActivity.this, ExpenseListActivity.class);
                startActivity(intentexpense);
                // addFragment(new FragmentHouse(), "House");
                back_status = false;
                break;
            case 10:
                Intent reportIntent = new Intent(MainActivity.this, ReportActivity.class);
                back_status = false;
                break;
            case 11:

                Intent intent3 = new Intent(MainActivity.this, BankListActivity.class);
                startActivity(intent3);

                // addFragment(new FragmentHouse(), "House");
                back_status = false;
                break;

            case 12:
                Intent manageUser = new Intent(MainActivity.this, ManageUserActivity.class);
                startActivity(manageUser);
                back_status = false;
                break;
            case 13:
                logoutConfirmation();
                back_status = false;
                break;
            case 14:
                startActivity(new Intent(this, ScanActivity.class));
                back_status = false;
                break;
        }
        if (position != 10){
            drawer.closeDrawer(Gravity.LEFT);
        }

    }

    public void logoutConfirmation(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure, You want to make a decision ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        session.logoutUser();
                        finish();
                        //dialog.cancel();
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


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        String strChild = expandableListDetail.get(
                expandableListTitle.get(groupPosition)).get(
                childPosition);

        if (strChild.equals("Vehicle Report")){
            startActivity(new Intent(this,VehicleLoadActivity.class));
        }
        else if (strChild.equals("Payment Report"))
        {
            startActivity(new Intent(this,CollectionReportActivity.class));
        }
        else if (strChild.equals("Next Day Order Report"))
        {
            startActivity(new Intent(this,NextDayOrderReport.class));
        }
        else if (strChild.equals("Sales Report"))
        {
            startActivity(new Intent(this,SalesReportActivity.class));
        }
        drawer.closeDrawer(Gravity.LEFT);
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        selectItem(groupPosition);
        return false;
    }
}
