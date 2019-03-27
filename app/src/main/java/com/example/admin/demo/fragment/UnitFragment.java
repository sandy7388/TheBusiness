package com.example.admin.demo.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.admin.demo.R;
import com.example.admin.demo.adapter.ProductUnitAdapter;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.UnitPojo;
import com.example.admin.demo.session.SessionManagement;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.HashMap;

public class UnitFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, View.OnFocusChangeListener,
        View.OnTouchListener, ProductUnitAdapter.onUnitClickListener {

    private RelativeLayout rl_fab_tint;
    private SoapPrimitive resultString;
    private AlertDialog.Builder alertDialogBuilderroute;
    private AlertDialog alertDialogroute;
    private ProductUnitAdapter productUnitAdapter;
    private RecyclerView rv_service_product;
    private FloatingActionMenu fab_main;
    private TextView tv_add,tv_cancel,tv_delete;
    private EditText et_unit_name,et_unit_short_name;
    private ProgressBar pb_list_unit;
    private FloatingActionButton fab_add_unit;
    private FloatingActionButton fab_new_conversion;
    private FloatingActionButton fab3;
    private AlertDialog alertDialogItem;
    private AlertDialog.Builder alertDialogBuilderItem;
    private AutoCompleteTextView at_primary_unit,at_secondary_unit,et_conversion_rate;
    private TextView tv_save_conversion,tv_cancel_conversion;
    private boolean isFragmentLoaded=false;
    private boolean atPrimaryFocus,atSecondaryFocus;
    private String[] primaryUnitName,secondaryUnitName;
    private PrimaryUnitAdapter primaryUnitAdapter;
    private SecondaryUnitAdapter secondaryUnitAdapter;
    private String strUnitId1,strUnitName1,strShortName1,strUnitId2,
            strUnitName2,strShortName2,strUnitIdEdit;
    private boolean isEditUnit = false;
    private SessionManagement session;
    private String compid,loginid;

    public UnitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_unit, container, false);
        rv_service_product = view.findViewById(R.id.rv_unit);
        rl_fab_tint = view.findViewById(R.id.rl_fab_tint);
        pb_list_unit = view.findViewById(R.id.pb_list_unit);
        fab_main = (FloatingActionMenu) view.findViewById(R.id.fab_main);
        fab_add_unit = (FloatingActionButton) view.findViewById(R.id.fab_add_unit);
        fab_new_conversion = (FloatingActionButton) view.findViewById(R.id.fab_new_conversion);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
        fab_add_unit.setOnClickListener(this);
        fab_new_conversion.setOnClickListener(this);
        session = new SessionManagement(getContext());
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        scrollFabButton();
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_unit:
                fab_main.close(true);
                isEditUnit = false;
                DialogAddUnit();
                break;
            case R.id.fab_new_conversion:
                fab_main.close(true);
                addConversionDialog();
                break;
            case R.id.tv_save_conversion:
                if (TextUtils.isEmpty(at_primary_unit.getText().toString())){
                    Toast.makeText(getContext(), "Please select base unit", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(at_secondary_unit.getText().toString())){
                    Toast.makeText(getContext(), "Please select secondary unit", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(et_conversion_rate.getText().toString())){
                    Toast.makeText(getContext(), "Please enter conversion rate", Toast.LENGTH_SHORT).show();
                }
                else {
                AsynSaveUnitConversion asynSaveUnitConversion = new AsynSaveUnitConversion();
                asynSaveUnitConversion.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1",
                        et_conversion_rate.getText().toString());
                alertDialogItem.dismiss();
                }
                break;

            case R.id.tv_cancel_conversion:
                et_conversion_rate.setText("");
                at_primary_unit.setText("");
                at_secondary_unit.setText("");
                alertDialogItem.dismiss();
                break;

            case R.id.btn_delete_unit:
                new AsyncCallDeleteUnit().execute(strUnitIdEdit);
                alertDialogroute.dismiss();
                break;
        }
    }

    public void DialogAddUnit() {
        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_unit, null);
        alertDialogBuilderroute = new AlertDialog.Builder(getContext());
        alertDialogBuilderroute.setView(promptsView);
        alertDialogroute = alertDialogBuilderroute.create();
        tv_add = promptsView.findViewById(R.id.btn_add_unit);
        tv_cancel = promptsView.findViewById(R.id.btn_cancel_unit);
        tv_delete = promptsView.findViewById(R.id.btn_delete_unit);
        tv_delete.setOnClickListener(this);
        if (isEditUnit){
            tv_delete.setVisibility(View.VISIBLE);
        }
        else {
            tv_delete.setVisibility(View.GONE);
        }
        et_unit_name = promptsView.findViewById(R.id.et_add_unit);
        et_unit_short_name = promptsView.findViewById(R.id.et_add_short_unit);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_unit_name.getText().toString())) {
                    Toast.makeText(getContext(), "Please Enter Unit", Toast.LENGTH_SHORT).show();
                } else {
                    AsyncCallAddUnit asyncCallWS = new AsyncCallAddUnit();
                    asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            et_unit_name.getText().toString(),
                            et_unit_short_name.getText().toString());
                    alertDialogroute.dismiss();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogroute.dismiss();

            }
        });

        alertDialogroute.setCanceledOnTouchOutside(true);
        alertDialogroute.setCancelable(false);
        alertDialogroute.setCanceledOnTouchOutside(true);
        alertDialogroute.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()){
            case R.id.et_first_unit:
                atPrimaryFocus = hasFocus;
                break;
            case R.id.et_second_unit:
                atSecondaryFocus = hasFocus;
                break;

        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.et_first_unit:
                AsyncAutoPrimaryUnitList asyncAutoPrimaryUnitList = new AsyncAutoPrimaryUnitList();
                asyncAutoPrimaryUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
                break;
            case R.id.et_second_unit:
                AsyncAutoSecondaryUnitList asyncAutoSecondaryUnitList = new AsyncAutoSecondaryUnitList();
                asyncAutoSecondaryUnitList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
                break;
        }
        return false;
    }

    @Override
    public void onUnitClick(String unitId, String shortName, String fullName) {
        isEditUnit = true;
        DialogAddUnit();
        strUnitIdEdit = unitId;
        et_unit_name.setText(fullName);
        et_unit_short_name.setText(shortName);

    }

    private  class AsyncCallAddUnit extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            pb_list_unit.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            String unitName = strings[0];
            String shortName = strings[1];
            String SOAP_ACTION,METHOD_NAME,URL;
            if (isEditUnit){
                SOAP_ACTION = "http://tempuri.org/UpdateUnit";
                METHOD_NAME = "UpdateUnit";
                URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            }
            else {
                SOAP_ACTION = "http://tempuri.org/SaveUnit";
                METHOD_NAME = "SaveUnit";
                URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            }
            String NAMESPACE = "http://tempuri.org/";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                if (isEditUnit){
                    Request.addProperty("UnitId", strUnitIdEdit);
                    Request.addProperty("unitName", unitName);
                    Request.addProperty("shortName", shortName);
                }
                else {
                    Request.addProperty("unitName", unitName);
                    Request.addProperty("shortName", shortName);
                }

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                transport.debug = true;
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (responseJSON.equals(null)) {
                    Toast.makeText(getContext(), "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                } else {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isEditUnit){
                                Toast.makeText(getContext(), "Unit Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Unit Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                            AsyncCallUnitlist asyncCallWS = new AsyncCallUnitlist();
                            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                            alertDialogroute.dismiss();
                        }
                    });

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return unitName;
        }

        @Override
        protected void onPostExecute(String s) {
            pb_list_unit.setVisibility(View.GONE);
        }
    }

    public ArrayList<UnitPojo> getUnit() {
        final ArrayList<UnitPojo> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListUnit";
        String METHOD_NAME = "ListUnit";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            //Request.addProperty("CompId", compid);
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
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            allItems.add(new UnitPojo(UnitName, UnitId,ShortName));
                            if (productUnitAdapter
                                    != null) {
                                productUnitAdapter
                                        .notifyDataSetChanged();
                            }
                        }
                    });


                }


            } else {
                Toast.makeText(getContext(), "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return allItems;
    }


    private class AsyncCallUnitlist extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            //pb_list_unit.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String id = params[0];

            productUnitAdapter = new ProductUnitAdapter(getContext(), getUnit(),UnitFragment.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_service_product.setNestedScrollingEnabled(false);
                    rv_service_product.setHasFixedSize(true);
                    rv_service_product.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_service_product.setAdapter(productUnitAdapter);

                }

            });
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_unit.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded ) {
            Log.d("Visible" ,"Unit Visible");
            AsyncCallUnitlist asyncCallWS = new AsyncCallUnitlist();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isFragmentLoaded = true;
        }
    }

    private void addConversionDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_unit_conversion, null);
        alertDialogBuilderItem = new AlertDialog.Builder(getContext());
        et_conversion_rate = view.findViewById(R.id.et_conversion_rate);
        at_primary_unit = view.findViewById(R.id.et_first_unit);
        at_secondary_unit = view.findViewById(R.id.et_second_unit);
        tv_save_conversion = view.findViewById(R.id.tv_save_conversion);
        tv_cancel_conversion = view.findViewById(R.id.tv_cancel_conversion);
        at_primary_unit.setEnabled(true);
        at_secondary_unit.setEnabled(true);
        at_secondary_unit.setFocusable(true);
        at_primary_unit.setFocusable(true);
        tv_save_conversion.setOnClickListener(this);
        tv_cancel_conversion.setOnClickListener(this);
        at_primary_unit.setOnItemClickListener(this);
        at_secondary_unit.setOnItemClickListener(this);
        at_primary_unit.setOnTouchListener(this);
        at_secondary_unit.setOnTouchListener(this);
        at_secondary_unit.setOnFocusChangeListener(this);
        at_primary_unit.setOnFocusChangeListener(this);
        at_primary_unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (atPrimaryFocus){
                    if (!s.toString().equals("")){
                        atSecondaryFocus = true;
                        at_secondary_unit.setFocusable(true);

                        at_secondary_unit.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (at_primary_unit.getText().toString().equals("")){
                                    atSecondaryFocus = false;
                                    //ll_add_conversion.setEnabled(false);
                                }
                                else
                                    at_secondary_unit.setFocusable(true);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
//                                if (s.toString().equals("")){
//                                    ll_add_conversion.setEnabled(false);
//                                }
                                if (at_primary_unit.getText().toString().equals("")){
                                    atSecondaryFocus = false;
                                }
                                else
                                    at_secondary_unit.setFocusable(true);

                            }
                        });
                    }
                    else{
                        at_secondary_unit.setText("");
                        atSecondaryFocus = false;
                        at_secondary_unit.setFocusable(false);
                        hideKeyboard();
                    }

                }

            }
        });
        alertDialogBuilderItem.setView(view);
        alertDialogItem = alertDialogBuilderItem.create();
        alertDialogItem.setCancelable(false);
        alertDialogItem.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogItem.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialogItem.show();
        alertDialogItem.getWindow().setAttributes(lp);
        alertDialogItem.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

    }

    public void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    // getting customer list
    private class AsyncAutoPrimaryUnitList extends AsyncTask<String, Void, String> {
        final ArrayList<UnitPojo> primaryUnitList = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String SOAP_ACTION = "http://tempuri.org/ListUnit";
            String METHOD_NAME = "ListUnit";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Request.addProperty("CompId", compid);
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

                        primaryUnitList.add(new UnitPojo(UnitName, UnitId,ShortName));
                    }
                    //primaryUnitList.add(0,new UnitPojo("All","-1",""));
                    primaryUnitName = new String[primaryUnitList.size()];
                    for (int j = 0; j < primaryUnitList.size(); j++) {
                        primaryUnitName[j] = primaryUnitList.get(j).getUnitName();
                    }
                } else {
                    Toast.makeText(getContext(), "Error From Server", Toast.LENGTH_SHORT).show();
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
                    primaryUnitAdapter = new PrimaryUnitAdapter(getContext(), primaryUnitList);
                    at_primary_unit.setAdapter(primaryUnitAdapter);
                    if (primaryUnitName.length > 0) {
                        if (!at_primary_unit.getText().toString().equals(""))
                            primaryUnitAdapter.getFilter().filter(null);
                        if (atPrimaryFocus){
                            at_primary_unit.showDropDown();
                        }
                    }

                }
            });

        }
    }

    // getting customer list
    private class AsyncAutoSecondaryUnitList extends AsyncTask<String, Void, String> {
        final ArrayList<UnitPojo> secondaryUnitList = new ArrayList<>();
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            //secondaryUnitList = new ArrayList<>();
            String SOAP_ACTION = "http://tempuri.org/ListUnit";
            String METHOD_NAME = "ListUnit";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                //Request.addProperty("CompId", compid);
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
                        secondaryUnitList.add(new UnitPojo(UnitName, UnitId,ShortName));
                    }
                    secondaryUnitName = new String[secondaryUnitList.size()];
                    for (int j = 0; j < secondaryUnitList.size(); j++) {
                        secondaryUnitName[j] = secondaryUnitList.get(j).getUnitName();
                    }
                } else {
                    Toast.makeText(getContext(), "Error From Server", Toast.LENGTH_SHORT).show();
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
                    secondaryUnitAdapter = new SecondaryUnitAdapter(getActivity(), secondaryUnitList);
                    at_secondary_unit.setAdapter(secondaryUnitAdapter);
                    if (secondaryUnitName.length > 0) {
                        // show all suggestions
                        if (!at_secondary_unit.getText().toString().equals(""))
                            secondaryUnitAdapter.getFilter().filter(null);
                        if (atSecondaryFocus){
                            at_secondary_unit.showDropDown();
                        }

                    }
                }
            });

        }
    }

    private class AsynSaveUnitConversion extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String strRate = params[1];
            saveUnitConversion(strUnitId1,strUnitId2,strRate);
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String saveUnitConversion(final String unitId1, String unitId2, String conversion_rate){
        String SOAP_ACTION = "http://tempuri.org/SaveUnitConversion";
        String METHOD_NAME = "SaveUnitConversion";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("unitId", unitId1);
            Request.addProperty("unit2", unitId2);
            Request.addProperty("rate", conversion_rate);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (responseJSON.equals(null)) {
                Toast.makeText(getContext(), "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            } else {
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AsyncCallUnitlist asyncCallWS = new AsyncCallUnitlist();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                        //clearAllEditText();
                    }
                });
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    public class PrimaryUnitAdapter extends ArrayAdapter<UnitPojo> {
        private ArrayList<UnitPojo> customers, tempCustomer, suggestions;
        private ProductIntefrace callBack;
        private String strUnitId,strUnitName,strShortName,strRouteId,strTripId;
        public PrimaryUnitAdapter(Context context, ArrayList<UnitPojo> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            this.customers = objects;
            this.tempCustomer = new ArrayList<UnitPojo>(objects);
            this.suggestions = new ArrayList<UnitPojo>(objects);
            //callBack = (ProductIntefrace)context;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final UnitPojo customer = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.unit_adapter, parent, false);
            }
            LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
            TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvUnit);
            if (txtCustomer != null){
                txtCustomer.setText(customer.getUnitName());
            }
            ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customer != null) {
                        strUnitId = customer.getUnitId();
                    }
                    if (customer != null) {
                        strUnitName = customer.getUnitName();
                    }
                    if (customer != null) {
                        strShortName = customer.getShortName();
                    }

                    at_primary_unit.setText(strUnitName);
                    strUnitId1 = strUnitId;
                    at_primary_unit.dismissDropDown();
                    hideKeyboard();

                    //callBack.onProductSelected(strUnitId,strUnitName,strShortName,"primary");
                }
            });

            // Now assign alternate color for rows
            if (position % 2 == 0)
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.odd));
            else
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.even));

            return convertView;
        }


        @Override
        public Filter getFilter() {
            return myFilter;
        }

        Filter myFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                UnitPojo customer = (UnitPojo) resultValue;
                return customer.getUnitName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (UnitPojo people : tempCustomer) {
                        if (people.getUnitName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<UnitPojo> c = (ArrayList<UnitPojo>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (UnitPojo cust : c) {
                        add(cust);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }

    public class SecondaryUnitAdapter extends ArrayAdapter<UnitPojo> {
        private ArrayList<UnitPojo> customers, tempCustomer, suggestions;
        private ProductIntefrace callBack;
        private String strUnitId,strUnitName,strShortName,strRouteId,strTripId;
        public SecondaryUnitAdapter(Context context, ArrayList<UnitPojo> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            this.customers = objects;
            this.tempCustomer = new ArrayList<UnitPojo>(objects);
            this.suggestions = new ArrayList<UnitPojo>(objects);
            //callBack = (ProductIntefrace)context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final UnitPojo customer = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.unit_adapter, parent, false);
            }
            LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
            TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvUnit);
            if (txtCustomer != null){
                txtCustomer.setText(customer.getUnitName());
            }
            ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customer != null) {
                        strUnitId = customer.getUnitId();
                    }
                    if (customer != null) {
                        strUnitName = customer.getUnitName();
                    }
                    if (customer != null) {
                        strShortName = customer.getShortName();
                    }
                    strUnitId2 = strUnitId;
                    at_secondary_unit.setText(strUnitName);
                    at_secondary_unit.dismissDropDown();
                    hideKeyboard();
                    //callBack.onProductSelected(strUnitId,strUnitName,strShortName,"secondary");
                    //callBack.EditParty(strRouteId,strTripId);
                }
            });

            // Now assign alternate color for rows
            if (position % 2 == 0)
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.odd));
            else
                convertView.setBackgroundColor(getContext().getResources().getColor(R.color.even));

            return convertView;
        }


        @Override
        public Filter getFilter() {
            return myFilter;
        }

        Filter myFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                UnitPojo customer = (UnitPojo) resultValue;
                return customer.getUnitName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (UnitPojo people : tempCustomer) {
                        if (people.getUnitName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<UnitPojo> c = (ArrayList<UnitPojo>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (UnitPojo cust : c) {
                        add(cust);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }

    public void scrollFabButton(){
        rv_service_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && fab_main.getVisibility() == View.VISIBLE) {
                    fab_main.hideMenu(true);
                } else if (dy > 0 && fab_main.getVisibility() != View.VISIBLE) {
                    fab_main.showMenu(true);
                }
            }
        });
    }


    private class AsyncCallDeleteUnit extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteUnit";
            String METHOD_NAME = "DeleteUnit";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("UnitId", id);
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
                            Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            AsyncCallUnitlist asyncCallWS = new AsyncCallUnitlist();
                            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Error From Server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {


        }

    }
}


