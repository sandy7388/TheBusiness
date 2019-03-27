package com.example.admin.demo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.TripAdapter;
import com.example.admin.demo.adapter.TripListByRoute;
import com.example.admin.demo.adapter.TripRouteAdapter;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.functions.SharedPreferencesDatabase;
import com.example.admin.demo.item.AllItem;
import com.example.admin.demo.R;
import com.example.admin.demo.model.PartyType;
import com.example.admin.demo.model.RouteList;
import com.example.admin.demo.model.TripList;
import com.example.admin.demo.model.TripListPojo;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AddPartyActivity extends AppCompatActivity implements CallBack, View.OnClickListener,
        AdapterView.OnItemSelectedListener, TripRouteAdapter.onItemClickListener, TripAdapter.OnRouteClickListener {
    private Toolbar toolbar_addParty;
    private LinearLayout llToolbarAppbar;
    private TextView appNameNavBar;
    private Toolbar toolbarMainDrawer;
    private MaterialSpinner sp_route_name;
    private MaterialSpinner spCategoryName;
    private MaterialSpinner spSerial;
    private Button btn_submit, btn_cancle, btn_edit, btn_delete;
    private ArrayAdapter<String> dataAdapter;
    private AlertDialog.Builder alertDialogBuilder, alertDialogBuilderroute;
    private AlertDialog alertDialog, alertDialogroute;
    private String[] tripList;
    private RecyclerView rv_main;
    private ProgressBar pb_rv_list;
    private ArrayAdapter<String> adapter, adapter2;
    private FloatingActionButton fbtn_addsales;
    private TextView tv_receivable, tv_payable, v_seperator;
    private CardView card_payble, card_receivable;
    private TripListByRoute tripListByRoute;
    private EditText et_short_name, et_party_trip_name, et_party_name, et_party_mobile,
            et_party_Current_Balance, et_party_as_of, et_party_seriel_number,
            et_party_route_name, et_party_state, et_party_email_id, et_party_gistn,
            et_party_address, et_party_party_type;
    private CoordinatorLayout coordinator_add_party;
    private SoapPrimitive resultString;
    private String routeid = "", partytypeid = "", tripid = "", compid = "", loginid = "";
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private boolean card_pay = false;
    private boolean card_receive = false;
    private ProgressBar pb_submit;
    private String str_payblestatus = "";
    private TextView tv_trip, tv_route, tv_partyadd, tv_trip_label;
    private LinearLayout ll_party_add, ll_bottom_button;
    private CardView card_add_party;
    private FloatingActionButton floating_button_add_route, floating_button_add_trip;
    private RecyclerView rv_trip_route;
    private ProgressBar pb_list_route;
    private TripRouteAdapter tripRouteAdapter;
    private TripAdapter tripAdapter;
    private SessionManagement session;
    private String routeName, routeId;
    private ArrayList<AllItem> allItems;
    private RelativeLayout ll_route_trip;
    private LinearLayout ll_route_trip_label;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String strDate;
    private int date_Year, date_Month, date_Day;
    private RelativeLayout rl_trip_list;
    private static final int PICK_CONTACT = 1000;
    private Uri uriContact;
    private String contactID;
    private String strPartyBalance, strAsOfDate, strShortName, strMobile,
            strCurrentBalamce, strSerialNumber, strEmail, strGst,
            strAddress, strPartyName, strState;
    private ImageView iv_openContacts;
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    public boolean isRouteTripUpdated = false;

    private Spinner sp_party_type, sp_route_name_party, sp_trip_name;
    private String strPartyType, strRouteName, strTripName, strPartyTypeId, strTripId, strRouteId;
    private ArrayList<PartyType> partyTypeArrayList;
    private ArrayList<RouteList> routeListArrayList;
    private ArrayList<TripList> tripListArrayList;
    private String[] partyTypeArray, routeNameArray, tripNameArray;
    private String strPartyId;

    private TextView btn_add,btn_cancel,btn_deleteTv,btn_add_trip,btn_cancel_trip,btn_delete_trip;
    private EditText et_add_route,et_add_trip;
    private LinearLayout linearLayoutRoute,linearLayoutTrip;

    private String strRouteIdDelete,strTripIdDelete,strTypeDelete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = AddPartyActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (arePermissionsEnabled()) {

            } else {
                requestMultiplePermissions();
            }
        }

        initView();

        session = new SessionManagement(this);
        HashMap<String, String> user = session.getUserDetails();
        if (user != null) {
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        Functions.setToolBar(AddPartyActivity.this, toolbar_addParty, "Add Party", true);
        appNameNavBar.setText("Add Party");
        tv_trip = findViewById(R.id.tv_trip);
        tv_route = findViewById(R.id.tv_route);
        tv_partyadd = findViewById(R.id.tv_partyadd);
        tv_trip_label = findViewById(R.id.tv_trip_label);
        ll_party_add = findViewById(R.id.ll_party_add);
        ll_bottom_button = findViewById(R.id.ll_bottom_button);
        card_add_party = findViewById(R.id.card_add_party);
        ll_route_trip = findViewById(R.id.ll_route_trip);
        rv_trip_route = findViewById(R.id.rv_trip_route);
        pb_list_route = findViewById(R.id.pb_list_route);
        rl_trip_list = findViewById(R.id.rl_trip_list);
        sp_party_type = (Spinner) findViewById(R.id.sp_party_type);
        sp_route_name_party = (Spinner) findViewById(R.id.sp_route_name_party);
        sp_trip_name = (Spinner) findViewById(R.id.sp_trip_name);
        iv_openContacts = findViewById(R.id.iv_openContacts);
        floating_button_add_route = findViewById(R.id.floating_button_add_route);
        floating_button_add_trip = findViewById(R.id.floating_button_add_trip);
        sp_trip_name.setOnItemSelectedListener(this);
        sp_route_name_party.setOnItemSelectedListener(this);
        sp_party_type.setOnItemSelectedListener(this);
        new AsyncCallPartyType().execute("1");
        new AsyncCallRouteName().execute("1");
        if (PurchaseActivity.isPurchase) {

            if (PartyInvoiceActivity.isEditParty) {
                getEditDetails();
            }
            tv_route.setVisibility(View.GONE);
            tv_trip.setVisibility(View.GONE);
            sp_route_name_party.setVisibility(View.GONE);
            sp_trip_name.setVisibility(View.GONE);
            rl_trip_list.setVisibility(View.GONE);
            strTripId = "0";
            strRouteId = "0";

            //str
            cardPayable();
        } else if (PartyInvoiceActivity.isEditParty && PurchaseActivity.isPurchase) {
            tv_route.setVisibility(View.GONE);
            tv_trip.setVisibility(View.GONE);
            sp_route_name_party.setVisibility(View.GONE);
            sp_trip_name.setVisibility(View.GONE);
            rl_trip_list.setVisibility(View.GONE);
            getEditDetails();
            strTripId = "0";
            strRouteId = "0";
            cardPayable();

        } else if (PartyInvoiceActivity.isEditParty) {

            getEditDetails();

        }
        floating_button_add_route.setOnClickListener(this);
        floating_button_add_trip.setOnClickListener(this);
        tv_partyadd.setOnClickListener(this);
        tv_route.setOnClickListener(this);
        tv_trip.setOnClickListener(this);
        card_payble.setOnClickListener(this);
        iv_openContacts.setOnClickListener(this);
        card_receivable.setOnClickListener(this);
        et_party_as_of.setOnClickListener(this);
        et_party_trip_name.setOnClickListener(this);
        et_party_route_name.setOnClickListener(this);
        et_party_party_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_party_mobile.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        // Calender
        calendar = Calendar.getInstance();
        date_Year = calendar.get(Calendar.YEAR);
        date_Month = calendar.get(Calendar.MONTH);
        date_Day = calendar.get(Calendar.DATE);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_party_as_of.setText(simpleDateFormat.format(calendar.getTime()));
        scrollFabButtonAddRoute();
        scrollFabButtonAddTrip();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean arePermissionsEnabled() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                remainingPermissions.add(permission);
            }
        }
        requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
    }

    private void initView() {
        toolbar_addParty = (Toolbar) findViewById(R.id.toolbar_addParty);
        llToolbarAppbar = (LinearLayout) findViewById(R.id.ll_toolbar_appbar);
        View view = toolbar_addParty.getRootView();
        ll_route_trip_label = findViewById(R.id.ll_route_trip_label);
        appNameNavBar = (TextView) view.findViewById(R.id.app_name_nav_bar);
        toolbarMainDrawer = (Toolbar) findViewById(R.id.toolbar_main_drawer);
        et_party_trip_name = (EditText) findViewById(R.id.et_party_trip_name);
        et_party_name = (EditText) findViewById(R.id.et_party_name);
        et_party_mobile = (EditText) findViewById(R.id.et_party_mobile);
        et_party_Current_Balance = (EditText) findViewById(R.id.et_party_Current_Balance);
        et_party_as_of = (EditText) findViewById(R.id.et_party_as_of);
        et_party_seriel_number = (EditText) findViewById(R.id.et_party_seriel_number);
        et_party_route_name = (EditText) findViewById(R.id.et_party_route_name);
        et_party_state = (EditText) findViewById(R.id.et_party_state);
        et_party_email_id = (EditText) findViewById(R.id.et_party_email_id);
        et_party_gistn = (EditText) findViewById(R.id.et_party_gistn);
        et_party_address = (EditText) findViewById(R.id.et_party_address);
        et_party_party_type = (EditText) findViewById(R.id.et_party_party_type);
        et_short_name = (EditText) findViewById(R.id.et_short_name);
        card_payble = (CardView) findViewById(R.id.card_payble);
        card_receivable = (CardView) findViewById(R.id.card_receivable);
        tv_receivable = (TextView) findViewById(R.id.tv_receivable);
        tv_payable = (TextView) findViewById(R.id.tv_payable);
        coordinator_add_party = (CoordinatorLayout) findViewById(R.id.coordinator_add_party);
        v_seperator = findViewById(R.id.v_seperator);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        pb_submit = (ProgressBar) findViewById(R.id.pb_submit);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_edit = findViewById(R.id.btn_edit);
    }


    @Override
    public void AddParty(String str_name, String type, String id) {
        if (type.equals("Trip")) {
            et_party_trip_name.setText(str_name);
            tripid = id;
            //Toast.makeText(this, tripid, Toast.LENGTH_SHORT).show();

        } else if (type.equals("route")) {
            et_party_route_name.setText(str_name);
            routeid = id;
            //Toast.makeText(this, routeid, Toast.LENGTH_SHORT).show();

        } else if (type.equals("partytype")) {
            et_party_party_type.setText(str_name);
            partytypeid = id;
            //Toast.makeText(this, partytypeid, Toast.LENGTH_SHORT).show();

        }
        alertDialog.dismiss();
    }

    @Override
    public void EditParty(String strPartyName, String strPartyId) {

    }

    String call_method_list = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_button_add_route:
                isRouteTripUpdated = false;
                DialogAddRoute(AddPartyActivity.this, "route");
                break;
            case R.id.floating_button_add_trip:
                isRouteTripUpdated = false;
                DialogAddRoute(AddPartyActivity.this, "trip");
                break;
            case R.id.tv_partyadd:
                textViewPartyAdd();
                break;
            case R.id.tv_route:
                textViewRoute();
                break;

            case R.id.tv_trip:
                textViewTrip();
                break;
            case R.id.card_payble:
                cardPayable();
                break;
            case R.id.card_receivable:
                cardReceivable();
                break;

            case R.id.et_party_as_of:
                getDateFrom();
                break;
            case R.id.btn_edit:
                makeEditTextEnabled();
                break;
            case R.id.btn_delete:
                new AsyncCallDeleteParty().execute(strPartyId);
                break;

            case R.id.et_party_party_type:
                //chooseCategory(AddPartyActivity.this, "party_type");
                break;
            case R.id.btn_submit:
                if (PurchaseActivity.isPurchase) {
                    buttonSubmitPurchaseAddParty();
                } else {
                    buttonSubmitAddParty();
                }
                break;
            case R.id.iv_openContacts:
                pickAContactNumber();
                break;
            case R.id.btn_delete_route:
                new AsyncCallDeleteRouteTrip().execute("route","",routeId);
                alertDialogroute.dismiss();
                break;
            case R.id.btn_delete_trip:
                new AsyncCallDeleteRouteTrip().execute("trip",strTripIdDelete,routeId);
                alertDialogroute.dismiss();
                break;
        }

    }


    private void makeEditTextEnabled() {
        sp_trip_name.setEnabled(true);
        sp_route_name_party.setEnabled(true);
        sp_party_type.setEnabled(true);
        et_party_name.setEnabled(true);
        et_short_name.setEnabled(true);
        et_party_mobile.setEnabled(true);
        et_party_Current_Balance.setEnabled(true);
        et_party_as_of.setEnabled(true);
        et_party_seriel_number.setEnabled(true);
        et_party_state.setEnabled(true);
        et_party_email_id.setEnabled(true);
        et_party_gistn.setEnabled(true);
        et_party_address.setEnabled(true);
        card_payble.setEnabled(true);
        card_receivable.setEnabled(true);
        btn_edit.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
    }

    //Todo when button is clicked
    public void pickAContactNumber() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                //Toast.makeText(AddPartyActivity.this, "Number="+num, Toast.LENGTH_LONG).show();
                                et_party_mobile.setText(num);
                            }
                        }
                    }
                    break;
                }
        }
    }

    private void buttonSubmitAddParty() {
        textGetter();
        if (strRouteName.equals("Select Route")) {
            Snackbar.make(coordinator_add_party, "Please Select Route Name", Snackbar.LENGTH_SHORT).show();
        } else if (strTripName.equals("Select Trip")) {
            Snackbar.make(coordinator_add_party, "Please Select Trip Name", Snackbar.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_party_name.getText().toString())) {
            Snackbar.make(coordinator_add_party, "Please Enter Party Name", Snackbar.LENGTH_SHORT).show();

        } else if (strPartyType.equals("Select Party Type")) {
            Snackbar.make(coordinator_add_party, "Please Select Party Type", Snackbar.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(et_party_seriel_number.getText().toString())) {
            Snackbar.make(coordinator_add_party, "Please enter serial number", Snackbar.LENGTH_SHORT).show();

        } else if (card_pay == false && card_receive == false) {
            Snackbar.make(coordinator_add_party, "Please Select Payable or Receivable Type", Snackbar.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String selectedDate = et_party_as_of.getText().toString();
            try {
                date = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);
            AsyncCallAddParty asyncCallWS = new AsyncCallAddParty();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1",
                    strPartyName, strGst,
                    strState, strMobile,
                    strEmail, strAddress,
                    formatedDate, strCurrentBalamce,
                    str_payblestatus, strSerialNumber,
                    strShortName);
        }

    }

    private void buttonSubmitPurchaseAddParty() {
        textGetter();
        if (TextUtils.isEmpty(et_party_name.getText().toString())) {
            Snackbar.make(coordinator_add_party, "Please Enter Party Name", Snackbar.LENGTH_SHORT).show();

        } else if (strPartyType.equals("Select Party Type")) {
            Snackbar.make(coordinator_add_party, "Please Select Party Type", Snackbar.LENGTH_SHORT).show();

        } else if (card_pay == false && card_receive == false) {
            Snackbar.make(coordinator_add_party, "Please Select Payable or Receivable Type", Snackbar.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String selectedDate = et_party_as_of.getText().toString();
            try {
                date = simpleDateFormat.parse(selectedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = simpleDateFormat.format(date);
            AsyncCallAddParty asyncCallWS = new AsyncCallAddParty();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1",
                    strPartyName, strGst,
                    strState, strMobile,
                    strEmail, strAddress,
                    formatedDate, strCurrentBalamce,
                    str_payblestatus, strSerialNumber,
                    strShortName);
            Toast.makeText(AddPartyActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }

    }

    private void cardReceivable() {
        card_receive = true;
        card_pay = false;
        str_payblestatus = tv_receivable.getText().toString();
        card_payble.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        card_receivable.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_payable.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_receivable.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void cardPayable() {
        card_receive = false;
        card_pay = true;
        str_payblestatus = tv_payable.getText().toString();
        card_payble.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        card_receivable.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        tv_payable.setTextColor(getResources().getColor(android.R.color.white));
        tv_receivable.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @SuppressLint("RestrictedApi")
    private void textViewTrip() {
        tv_route.setBackgroundResource(R.color.white);
        tv_partyadd.setBackgroundResource(R.color.white);
        tv_trip.setBackgroundResource(R.color.colorAccent);
        ll_party_add.setVisibility(View.GONE);
        ll_bottom_button.setVisibility(View.GONE);
        card_add_party.setVisibility(View.GONE);
        ll_route_trip.setVisibility(View.VISIBLE);
        floating_button_add_route.setVisibility(View.GONE);
        floating_button_add_trip.setVisibility(View.VISIBLE);
        ll_route_trip_label.setVisibility(View.VISIBLE);
        rv_trip_route.setVisibility(View.INVISIBLE);
        v_seperator.setVisibility(View.VISIBLE);
        tv_trip_label.setVisibility(View.VISIBLE);
        AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "trip", "list");
    }

    @SuppressLint("RestrictedApi")
    private void textViewRoute() {
        tv_route.setBackgroundResource(R.color.colorAccent);
        tv_partyadd.setBackgroundResource(R.color.white);
        tv_trip.setBackgroundResource(R.color.white);
        ll_party_add.setVisibility(View.GONE);
        ll_bottom_button.setVisibility(View.GONE);
        card_add_party.setVisibility(View.GONE);
        floating_button_add_route.setVisibility(View.VISIBLE);
        floating_button_add_trip.setVisibility(View.GONE);
        ll_route_trip.setVisibility(View.VISIBLE);
        rv_trip_route.setVisibility(View.INVISIBLE);
        ll_route_trip_label.setVisibility(View.VISIBLE);
        tv_trip_label.setVisibility(View.INVISIBLE);
        v_seperator.setVisibility(View.VISIBLE);
        AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "route", "list");

    }

    private void textViewPartyAdd() {
        tv_route.setBackgroundResource(R.color.white);
        tv_partyadd.setBackgroundResource(R.color.colorAccent);
        tv_trip.setBackgroundResource(R.color.white);
        ll_party_add.setVisibility(View.VISIBLE);
        ll_bottom_button.setVisibility(View.VISIBLE);
        card_add_party.setVisibility(View.VISIBLE);
        ll_route_trip.setVisibility(View.GONE);
        ll_route_trip_label.setVisibility(View.GONE);
        v_seperator.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()) {
            case R.id.sp_new_trip:
                routeName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < allItems.size(); i++) {
                    if (routeName.equals(allItems.get(i).getName())) {
                        routeId = allItems.get(i).getId();
                    }
                }
                break;

            case R.id.sp_party_type:
                strPartyType = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < partyTypeArrayList.size(); i++) {
                    if (strPartyType.equals(partyTypeArrayList.get(i).getStrPartyType())) {
                        strPartyTypeId = partyTypeArrayList.get(i).getStrPartyId();
                        //Toast.makeText(this, strPartyTypeId, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.sp_route_name_party:
                strRouteName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < routeListArrayList.size(); i++) {
                    if (strRouteName.equals(routeListArrayList.get(i).getStrRouteName())) {
                        strRouteId = routeListArrayList.get(i).getStrRouteId();
                        //Toast.makeText(this, strRouteId, Toast.LENGTH_SHORT).show();
                        new AsyncCallTripName().execute(strRouteId);
                    }
                }
                break;
            case R.id.sp_trip_name:
                strTripName = String.valueOf(spinner.getAdapter().getItem(position));
                for (int i = 0; i < tripListArrayList.size(); i++) {
                    if (strTripName.equals(tripListArrayList.get(i).getTripName())) {
                        strTripId = tripListArrayList.get(i).getTripId();
                        //Toast.makeText(this, strTripId, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(String id, String name,String type) {
        if (type.equals("route")){
            isRouteTripUpdated = true;
            DialogAddRoute(this,"route");
            btn_deleteTv.setVisibility(View.VISIBLE);
            routeId = id;
            et_add_route.setText(name);

        }

    }

    @Override
    public void onClickListener(String tripId, String routeID, String tripName, String type) {
        if (type.equals("trip")){
            isRouteTripUpdated = true;
            DialogAddRoute(this,"trip");
            btn_delete_trip.setVisibility(View.VISIBLE);
            et_add_trip.setText(tripName);
            routeId = routeID;
            for (int i=0;i<allItems.size();i++){
                if (routeId.equals(allItems.get(i).getId())){
                    sp_route_name.setSelection(i);
                }

            }
            strTripIdDelete = tripId;

        }
    }

    private class AsyncCallWSTsklist extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];
            String call_method = params[1];
            call_method_list = params[2];
            if (call_method_list.equals("list")) {
                Handler uiHandlerprogress = new Handler(Looper.getMainLooper());
                uiHandlerprogress.post(new Runnable() {
                    @Override
                    public void run() {
                        pb_list_route.setVisibility(View.VISIBLE);
                    }
                });

                if (call_method.equals("trip")) {
                    tripAdapter = new TripAdapter(AddPartyActivity.this, getTrip("list"), "trip");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_trip_route.setNestedScrollingEnabled(false);
                            rv_trip_route.setHasFixedSize(true);
                            rv_trip_route.setLayoutManager(new LinearLayoutManager(AddPartyActivity.this));
                            rv_trip_route.setAdapter(tripAdapter);

                        }

                    });
                } else if (call_method.equals("route")) {

                    tripRouteAdapter = new TripRouteAdapter(AddPartyActivity.this, getRouteList("list"), "route");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            rv_trip_route.setNestedScrollingEnabled(false);
                            rv_trip_route.setHasFixedSize(true);
                            rv_trip_route.setLayoutManager(new LinearLayoutManager(AddPartyActivity.this));
                            rv_trip_route.setAdapter(tripRouteAdapter);

                        }

                    });
                }
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (call_method_list.equals("list")) {

                rv_trip_route.setVisibility(View.VISIBLE);
                pb_list_route.setVisibility(View.GONE);

            } else {
                pb_rv_list.setVisibility(View.GONE);
            }

            if (result.equals("success")) {
                Toast.makeText(AddPartyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddPartyActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private class AsyncCallAddParty extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String party_name = params[1];
            String gstnumber = params[2];
            String statename = params[3];
            String phone = params[4];
            String email = params[5];
            String address = params[6];
            String asofdate = params[7];
            String currentbalance = params[8];
            String payblestatus = params[9];
            String srnumber = params[10];
            String shortName = params[11];
            addparty(compid, loginid, strRouteId, strTripId, strPartyTypeId, party_name,
                    shortName, gstnumber, statename, phone, email, address,
                    asofdate, currentbalance, payblestatus, srnumber);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            PartyListActivity.isRefreshedParty = true;
            PurchaseActivity.isPurchaseActivityRefreshed = true;
            Functions.hideKeyboard(AddPartyActivity.this);
            if (result.equals("success")) {
                Toast.makeText(AddPartyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddPartyActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncCallAddRoute extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            addRoute(id);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(AddPartyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddPartyActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private class AsyncCallAddTrip extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String tripName = params[0];
            String routeId = params[1];
            addTrip(tripName, routeId);
            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(AddPartyActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(AddPartyActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public ArrayList<TripListPojo> getTrip(final String list) {
        final ArrayList<TripListPojo> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListAllTrip";
        String METHOD_NAME = "ListAllTrip";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("roteId", "-1");
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
                    final String RouteName = jarray.getJSONObject(i).getString("RouteName");
                    final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                    final String TripName = jarray.getJSONObject(i).getString("TripName");
                    final String TripId = jarray.getJSONObject(i).getString("TripId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            allItems.add(new TripListPojo(RouteName, RouteId, TripName, TripId));
                            if (list.equals("list")) {

                                if (tripRouteAdapter
                                        != null) {
                                    tripRouteAdapter
                                            .notifyDataSetChanged();
                                }
                            } else {

                                if (tripListByRoute
                                        != null) {
                                    tripListByRoute
                                            .notifyDataSetChanged();
                                }
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

    public void DialogAddRoute(final Context context, String call_method) {
        View promptsView = LayoutInflater.from(context).inflate(R.layout.dialog_route_trip, null);
        alertDialogBuilderroute = new AlertDialog.Builder(context);
        alertDialogBuilderroute.setView(promptsView);
        alertDialogroute = alertDialogBuilderroute.create();
        btn_add = promptsView.findViewById(R.id.btn_add);
        btn_cancel = promptsView.findViewById(R.id.btn_cancel);
        btn_deleteTv = promptsView.findViewById(R.id.btn_delete_route);
        et_add_route = promptsView.findViewById(R.id.et_add_route);
        btn_add_trip = promptsView.findViewById(R.id.btn_add_trip);
        btn_cancel_trip = promptsView.findViewById(R.id.btn_cancel_trip);
        btn_delete_trip = promptsView.findViewById(R.id.btn_delete_trip);
        et_add_trip = promptsView.findViewById(R.id.et_add_trip);
        linearLayoutRoute = promptsView.findViewById(R.id.ll_route);
        linearLayoutTrip = promptsView.findViewById(R.id.ll_trip);
        sp_route_name = promptsView.findViewById(R.id.sp_new_trip);
        sp_route_name.setOnItemSelectedListener(this);
        btn_deleteTv.setOnClickListener(this);
        btn_delete_trip.setOnClickListener(this);
        if (call_method.equals("route")) {
            linearLayoutRoute.setVisibility(View.VISIBLE);
            linearLayoutTrip.setVisibility(View.GONE);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(et_add_route.getText().toString())) {
                        Toast.makeText(context, "Please Enter Route", Toast.LENGTH_SHORT).show();
                    } else {
                        AsyncCallAddRoute asyncCallWS = new AsyncCallAddRoute();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, et_add_route.getText().toString());
                    }
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialogroute.dismiss();

                }
            });

        }

        if (call_method.equals("trip")) {
            getRouteData();
            linearLayoutRoute.setVisibility(View.GONE);
            linearLayoutTrip.setVisibility(View.VISIBLE);
            btn_add_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(et_add_trip.getText().toString())) {
                        Toast.makeText(context, "Please Enter Trip", Toast.LENGTH_SHORT).show();
                    } else {
                        AsyncCallAddTrip asyncCallWS = new AsyncCallAddTrip();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, et_add_trip.getText().toString(),
                                routeId);
                    }
                }
            });
            btn_cancel_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialogroute.dismiss();

                }
            });
        }

        alertDialogroute.setCanceledOnTouchOutside(true);
        alertDialogroute.setCancelable(false);
        alertDialogroute.setCanceledOnTouchOutside(true);
        alertDialogroute.show();
    }

    public ArrayList<AllItem> getRouteList(final String list) {
        final ArrayList<AllItem> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListRoute";
        String METHOD_NAME = "ListRoute";
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
                    final String RouteName = jarray.getJSONObject(i).getString("RouteName");
                    final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            allItems.add(new AllItem(RouteName, RouteId));
                            if (list.equals("list")) {
                                if (tripRouteAdapter
                                        != null) {
                                    tripRouteAdapter
                                            .notifyDataSetChanged();
                                }
                            } else {
                                if (tripListByRoute
                                        != null) {
                                    tripListByRoute
                                            .notifyDataSetChanged();
                                }
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

    public void getRouteData() {
        allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListRoute";
        String METHOD_NAME = "ListRoute";
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
                    final String RouteName = jarray.getJSONObject(i).getString("RouteName");
                    final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                    allItems.add(new AllItem(RouteName, RouteId));
                }
                tripList = new String[allItems.size()];
                for (int j = 0; j < allItems.size(); j++) {
                    tripList[j] = allItems.get(j).getName(); //create array of name
                }
                //sp_route_name.setAdapter(new ArrayAdapter<String>(PartyListActivity.this, android.R.layout.simple_spinner_item, tripList1));
                dataAdapter = new ArrayAdapter<String>(AddPartyActivity.this,
                        android.R.layout.simple_spinner_item, tripList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_route_name.setAdapter(dataAdapter);


            } else {
                Toast.makeText(this, "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public String addparty(String compId, String loginId, String routeId, String tripId,
                           String partyTypeId, String partyName, String nickName,
                           String gstNumber, String StateName, String Phone,
                           String email, String address, String asOfDate,
                           String currentBalance, String payStatus, String srNo) {
        String SOAP_ACTION, METHOD_NAME;
        if (PartyInvoiceActivity.isEditParty) {
            SOAP_ACTION = "http://tempuri.org/UpdateParty";
            METHOD_NAME = "UpdateParty";
        } else {
            SOAP_ACTION = "http://tempuri.org/SaveParty";
            METHOD_NAME = "SaveParty";
        }

        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("compId", compId);
            Request.addProperty("loginId", loginId);
            Request.addProperty("routeId", routeId);
            Request.addProperty("tripId", tripId);
            Request.addProperty("partyTypeId", partyTypeId);
            Request.addProperty("partyName", partyName);
            Request.addProperty("nickName", nickName);
            Request.addProperty("gstNumber", gstNumber);
            Request.addProperty("StateName", StateName);
            Request.addProperty("Phone", Phone);
            Request.addProperty("email", email);
            Request.addProperty("address", address);
            Request.addProperty("asOfDate", asOfDate);
            Request.addProperty("currentBalance", currentBalance);
            if (payStatus.equals("RECEIVABLE")) {
                Request.addProperty("recieveable", "true");
            } else {
                Request.addProperty("recieveable", "false");
            }
            if (PartyInvoiceActivity.isEditParty) {
                Request.addProperty("partyId", strPartyId);
            }
            Request.addProperty("payStatus", payStatus);
            Request.addProperty("srNo", srNo);
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
                        Toast.makeText(AddPartyActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
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

    public String addRoute(String route) {
        String SOAP_ACTION,METHOD_NAME;
        if (isRouteTripUpdated){
            SOAP_ACTION = "http://tempuri.org/UpdateRoute";
            METHOD_NAME = "UpdateRoute";
        }
        else {
            SOAP_ACTION = "http://tempuri.org/SaveRoute";
            METHOD_NAME = "SaveRoute";
        }

        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            if (isRouteTripUpdated){
                Request.addProperty("routeId", strRouteIdDelete);
                Request.addProperty("routeName", route);
            }
            else {
                Request.addProperty("RouteName", route);
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
                        if (isRouteTripUpdated){
                            Toast.makeText(AddPartyActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddPartyActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        }

                        AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "route", "list");
                        alertDialogroute.dismiss();
                    }
                });
            }
        } catch (Exception ex) {
            return "error";
        }
        return "success";
    }

    public String addTrip(String trip, String routeId) {
        String SOAP_ACTION,METHOD_NAME;
        if (isRouteTripUpdated){
            SOAP_ACTION = "http://tempuri.org/UpdateTrip";
            METHOD_NAME = "UpdateTrip";
        }
        else {
            SOAP_ACTION = "http://tempuri.org/SaveTrip";
            METHOD_NAME = "SaveTrip";
        }

        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";

        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            if (isRouteTripUpdated){
                Request.addProperty("TripId", strTripIdDelete);
                Request.addProperty("RouteId", routeId);
                Request.addProperty("TripName", trip);
            }
            else {
                Request.addProperty("TripName", trip);
                Request.addProperty("RouteId", routeId);
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
                        Toast.makeText(AddPartyActivity.this, "Succesfully Added", Toast.LENGTH_SHORT).show();

                        AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "trip", "list");
                        alertDialogroute.dismiss();
                    }
                });

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
        return "success";
    }

    void clearAllEditText() {
        et_party_name.setText("");
        et_party_gistn.setText("");
        et_party_gistn.setText("");
        et_party_email_id.setText("");
        et_party_mobile.setText("");
        et_short_name.setText("");
        et_party_Current_Balance.setText("");
        et_party_address.setText("");
        et_party_seriel_number.setText("");
        et_party_as_of.setText("");
        et_party_party_type.setText("");
        et_party_trip_name.setText("");
        et_party_route_name.setText("");
        if (tripid.equals("0")) {
            cardPayable();
        } else {
            card_pay = false;
            card_receive = false;
            card_payble.setCardBackgroundColor(getResources().getColor(R.color.white));
            card_receivable.setCardBackgroundColor(getResources().getColor(android.R.color.white));
            tv_payable.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tv_receivable.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
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
                et_party_as_of.setText(strDate);
            }
        }, date_Year, date_Month, date_Day
        );
        datePickerDialog.show();
    }

    public void scrollFabButtonAddRoute() {
        rv_trip_route.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floating_button_add_route.getVisibility() == View.VISIBLE) {
                    floating_button_add_route.hide();
                } else if (dy > 0 && floating_button_add_route.getVisibility() != View.VISIBLE) {
                    floating_button_add_route.show();
                }
            }
        });
    }

    public void scrollFabButtonAddTrip() {
        rv_trip_route.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floating_button_add_trip.getVisibility() == View.VISIBLE) {
                    floating_button_add_trip.hide();
                } else if (dy > 0 && floating_button_add_trip.getVisibility() != View.VISIBLE) {
                    floating_button_add_trip.show();
                }
            }
        });
    }

    private void textGetter() {
        strPartyName = et_party_name.getText().toString();
        strGst = et_party_gistn.getText().toString();
        if (strGst.equals("")) {
            strGst = "0";
        }
        strState = et_party_state.getText().toString();
        if (strState.equals("")) {
            strState = "Maharashtra";
        }
        strMobile = et_party_mobile.getText().toString();
        if (strMobile.equals("")) {
            strMobile = "0";
        }
        strEmail = et_party_email_id.getText().toString();
        if (strEmail.equals("")) {
            strEmail = "0";
        }
        strAddress = et_party_address.getText().toString();
        if (strAddress.equals("")) {
            strAddress = "0";
        }
        strCurrentBalamce = et_party_Current_Balance.getText().toString();
        if (strCurrentBalamce.equals("")) {
            strCurrentBalamce = "0";
        }
        strSerialNumber = et_party_seriel_number.getText().toString();
        if (strSerialNumber.equals("")) {
            strSerialNumber = "0";
        }
        strShortName = et_short_name.getText().toString();
        if (strShortName.equals("")) {
            strShortName = "0";
        }
    }


    private class AsyncCallPartyType extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            partyTypeArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListPartyType";
            String METHOD_NAME = "ListPartyType";
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
                        final String PartyType = jarray.getJSONObject(i).getString("PartyType");
                        final String PartyTypeId = jarray.getJSONObject(i).getString("PartyTypeId");
                        partyTypeArrayList.add(new PartyType(PartyType, PartyTypeId));
                    }
                    partyTypeArrayList.add(0, new PartyType("Select Party Type", ""));
                    partyTypeArray = new String[partyTypeArrayList.size()];
                    for (int j = 0; j < partyTypeArrayList.size(); j++) {
                        partyTypeArray[j] = partyTypeArrayList.get(j).getStrPartyType();
                    }
                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPartyActivity.this,
                            android.R.layout.simple_spinner_item, partyTypeArray);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_party_type.setAdapter(dataAdapter);
                }
            });
        }

    }


    private class AsyncCallRouteName extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            routeListArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListRoute";
            String METHOD_NAME = "ListRoute";
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
                        final String RouteName = jarray.getJSONObject(i).getString("RouteName");
                        final String RouteId = jarray.getJSONObject(i).getString("RouteId");
                        routeListArrayList.add(new RouteList(RouteName, RouteId));
                    }
                    routeListArrayList.add(0, new RouteList("Select Route", "-1"));
                    routeNameArray = new String[routeListArrayList.size()];
                    for (int j = 0; j < routeListArrayList.size(); j++) {
                        routeNameArray[j] = routeListArrayList.get(j).getStrRouteName();
                    }
                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPartyActivity.this,
                            android.R.layout.simple_spinner_item, routeNameArray);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_route_name_party.setAdapter(dataAdapter);
                }
            });
        }

    }

    private class AsyncCallTripName extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            tripListArrayList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListAllTrip";
            String METHOD_NAME = "ListAllTrip";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("roteId", id);
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
                        final String TripName = jarray.getJSONObject(i).getString("TripName");
                        final String TripId = jarray.getJSONObject(i).getString("TripId");
                        tripListArrayList.add(new TripList(TripName, TripId));
                    }
                    tripListArrayList.add(0, new TripList("Select Trip", ""));
                    tripNameArray = new String[tripListArrayList.size()];
                    for (int j = 0; j < tripListArrayList.size(); j++) {
                        tripNameArray[j] = tripListArrayList.get(j).getTripName();
                    }
                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddPartyActivity.this,
                            android.R.layout.simple_spinner_item, tripNameArray);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_trip_name.setAdapter(dataAdapter);
                }
            });
        }

    }


    private class AsyncCallPartyDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/GetPartyDetailsById";
            String METHOD_NAME = "GetPartyDetailsById";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("partyId", id);
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
                                if (jarray.getJSONObject(0).getString("PartyName").equals("0")) {
                                    et_party_name.setText("");
                                } else {
                                    et_party_name.setText(jarray.getJSONObject(0).getString("PartyName"));
                                }
                                if (jarray.getJSONObject(0).getString("NickName").equals("0")) {
                                    et_short_name.setText("");
                                } else {
                                    et_short_name.setText(jarray.getJSONObject(0).getString("NickName"));
                                }

                                if (jarray.getJSONObject(0).getString("PartyPhone").equals("0")) {
                                    et_party_mobile.setText("");
                                } else {
                                    et_party_mobile.setText(jarray.getJSONObject(0).getString("PartyPhone"));
                                }
                                et_party_Current_Balance.setText(jarray.getJSONObject(0).getString("PartyCurrentBalance"));
                                Date date = new Date();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                date = simpleDateFormat.parse(jarray.getJSONObject(0).getString("AsOfDate"));
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                et_party_as_of.setText(simpleDateFormat1.format(date));

                                if (jarray.getJSONObject(0).getString("SerialNumber").equals("0")) {
                                    et_party_seriel_number.setText("");
                                } else {
                                    et_party_seriel_number.setText(jarray.getJSONObject(0).getString("SerialNumber"));
                                }

                                if (jarray.getJSONObject(0).getString("PartyState").equals("0")) {
                                    et_party_state.setText("");
                                } else {
                                    et_party_state.setText(jarray.getJSONObject(0).getString("PartyState"));
                                }

                                if (jarray.getJSONObject(0).getString("PartyEmail").equals("0")) {
                                    et_party_email_id.setText("");
                                } else {
                                    et_party_email_id.setText(jarray.getJSONObject(0).getString("PartyEmail"));
                                }

                                if (jarray.getJSONObject(0).getString("PartyGSTINNumber").equals("0")) {
                                    et_party_gistn.setText("");
                                } else {
                                    et_party_gistn.setText(jarray.getJSONObject(0).getString("PartyGSTINNumber"));
                                }
                                if (jarray.getJSONObject(0).getString("PartyAddress").equals("0")) {
                                    et_party_address.setText("");
                                } else {
                                    et_party_address.setText(jarray.getJSONObject(0).getString("PartyAddress"));
                                }
                                if (jarray.getJSONObject(0).getString("IsReceivable").equals("true")) {
                                    cardReceivable();
                                    str_payblestatus = "RECEIVABLE";
                                } else {
                                    cardPayable();
                                    str_payblestatus = "PAYABLE";
                                }
                                strPartyId = jarray.getJSONObject(0).getString("PartyId");
                                strPartyTypeId = jarray.getJSONObject(0).getString("PartyTypeId");
                                strRouteId = jarray.getJSONObject(0).getString("RouteId");
                                strTripId = jarray.getJSONObject(0).getString("TripId");
                                new AsyncCallTripName().execute(strRouteId);
                                for (int i = 0; i < partyTypeArrayList.size(); i++) {
                                    if (strPartyTypeId.equals(partyTypeArrayList.get(i).getStrPartyId())) {
                                        sp_party_type.setSelection(i);
                                    }
                                }
                                for (int i = 0; i < routeListArrayList.size(); i++) {
                                    if (strRouteId.equals(routeListArrayList.get(i).getStrRouteId())) {
                                        sp_route_name_party.setSelection(i);

                                    }
                                }
                                for (int i = 0; i < tripListArrayList.size(); i++) {
                                    if (strTripId.equals(tripListArrayList.get(i).getTripId())) {
                                        sp_trip_name.setSelection(i);
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
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

    private class AsyncCallDeleteParty extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteParty";
            String METHOD_NAME = "DeleteParty";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("partyId", id);
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
                            Toast.makeText(AddPartyActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            if (MainActivity.isPurchase) {
                                startActivity(new Intent(AddPartyActivity.this, PurchaseActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(AddPartyActivity.this, PartyListActivity.class));
                                finish();
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (MainActivity.isPurchase) {
                PurchaseActivity.isPurchaseActivityRefreshed = true;
            } else {
                PartyListActivity.isRefreshedParty = true;
            }

        }

    }

    private class AsyncCallDeleteRouteTrip extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            final String type = strings[0];
            String tripId = strings[1];
            String routeId = strings[2];
            String SOAP_ACTION,METHOD_NAME;
            if (type.equals("route")){
                SOAP_ACTION = "http://tempuri.org/DeleteRoute";
                METHOD_NAME = "DeleteRoute";
            }
            else {
                SOAP_ACTION = "http://tempuri.org/DeleteTripById";
                METHOD_NAME = "DeleteTripById";
            }

            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                if (type.equals("route")){
                    Request.addProperty("routeId", routeId);
                }
                if (type.equals("trip")){
                    Request.addProperty("RouteId", routeId);
                    Request.addProperty("TripId", tripId);
                }

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
                            if (type.equals("route")){
                                AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
                                asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "route", "list");
                            }
                            if (type.equals("trip")){
                                AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
                                asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1", "trip", "list");
                            }
                        }
                    });

                } else {
                    Toast.makeText(AddPartyActivity.this, "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return type;
        }
    }

    void getEditDetails() {
        strPartyId = getIntent().getStringExtra("PARTY_ID");
        new AsyncCallPartyDetails().execute(strPartyId);
        btn_edit.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.VISIBLE);
        btn_cancle.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);
        sp_trip_name.setEnabled(false);
        sp_route_name_party.setEnabled(false);
        sp_party_type.setEnabled(false);
        et_party_name.setEnabled(false);
        et_short_name.setEnabled(false);
        et_party_mobile.setEnabled(false);
        et_party_Current_Balance.setEnabled(false);
        et_party_as_of.setEnabled(false);
        et_party_seriel_number.setEnabled(false);
        et_party_state.setEnabled(false);
        et_party_email_id.setEnabled(false);
        et_party_gistn.setEnabled(false);
        et_party_address.setEnabled(false);
        card_payble.setEnabled(false);
        card_receivable.setEnabled(false);
    }
}
