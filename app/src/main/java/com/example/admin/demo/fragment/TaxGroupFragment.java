package com.example.admin.demo.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.TaxGroupListAdapter;
import com.example.admin.demo.adapter.TaxGroupListAdapterTest;
import com.example.admin.demo.adapter.TaxRateAdapter;
import com.example.admin.demo.adapter.TaxRateListAdapter;
import com.example.admin.demo.item.TaxRateItem;
import com.example.admin.demo.model.ListTaxGroup;
import com.example.admin.demo.model.ListTaxGroupPojo;
import com.example.admin.demo.model.TaxGroupPojo;
import com.example.admin.demo.model.TaxList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaxGroupFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fb_tax_group;
    private RecyclerView rv_tax_group,rv_tax_rate_group;
    private TaxRateListAdapter taxRateListAdapter;
    private TaxGroupListAdapterTest taxGroupListAdapter;
    private SoapPrimitive resultString;
    private AlertDialog.Builder alertDialogBuilderroute;
    private AlertDialog alertDialogroute;
    private TextView tv_add,tv_cancel;
    private EditText et_add_tax_group;
    private ProgressBar pb_error_common_recyclerView;
    private Spinner sp_tax_type;
    private String[] country = { "IGST", "CGST", "SGST"};
    private String strTaxType;
    private ArrayList<String> stringArrayList;
    private ArrayList<TaxRateItem> taxRateItemArrayList;
    private ArrayList<TaxGroupPojo> taxGroupPojoArrayList;
    private boolean isFragmentLoaded=false;
    private static final String SEPARATOR = ",";
    private StringBuilder csvBuilder;
    private String strTaxId;
    private StringBuilder stringBuilder;
    public TaxGroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax_group, container, false);
        fb_tax_group = view.findViewById(R.id.fb_tax_group);
        rv_tax_group = view.findViewById(R.id.rv_tax_group);
        stringArrayList = new ArrayList<>();
        pb_error_common_recyclerView = view.findViewById(R.id.pb_error_common_recyclerView);
        fb_tax_group.setOnClickListener(this);
        scrollFabButton();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fb_tax_group:
                DialogTaxGroup();
                break;
            case R.id.btn_add_tax_group:
                if (TextUtils.isEmpty(et_add_tax_group.getText().toString())){
                    Toast.makeText(getContext(), "Please enter the group name", Toast.LENGTH_SHORT).show();
                }
                else if (stringArrayList.isEmpty()){
                    Toast.makeText(getContext(), "Please choose minimum one tax rate", Toast.LENGTH_SHORT).show();
                }
                else {
                    AsyncCallAddTaxGroup asyncCallAddTaxGroup = new AsyncCallAddTaxGroup();
                    asyncCallAddTaxGroup.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            et_add_tax_group.getText().toString(),
                            strTaxId);
                }
        }

    }
    public void DialogTaxGroup() {
        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_tax_group, null);
        alertDialogBuilderroute = new AlertDialog.Builder(getContext());
        alertDialogBuilderroute.setView(promptsView);
        alertDialogroute = alertDialogBuilderroute.create();
        tv_add = promptsView.findViewById(R.id.btn_add_tax_group);
        tv_cancel = promptsView.findViewById(R.id.btn_cancel_tax_group);
        et_add_tax_group = promptsView.findViewById(R.id.et_add_tax_group);
        rv_tax_rate_group = promptsView.findViewById(R.id.rv_tax_rate_group);
        tv_add.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        AsyncCallTaxList asyncCallWS = new AsyncCallTaxList();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        alertDialogroute.setCanceledOnTouchOutside(true);
        alertDialogroute.setCancelable(false);
        alertDialogroute.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogroute.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialogroute.show();
        alertDialogroute.getWindow().setAttributes(lp);
        alertDialogroute.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
    }

    private class AsyncCallTaxList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            taxRateListAdapter = new TaxRateListAdapter(getTax(), new TaxRateListAdapter.OnItemCheckListener() {
                @Override
                public void onItemCheck(TaxRateItem item) {
                    stringArrayList.add(item.getStrId());
                    String string = "";
                    string = string + stringArrayList.toString();
                    strTaxId = string.substring(1, string.length()-1);
                    Log.d("List",strTaxId);

                }

                @Override
                public void onItemUncheck(TaxRateItem item) {
                    stringArrayList.remove(item.getStrId());
                    String string = "";
                    string = string + stringArrayList.toString();
                    strTaxId = string.substring(1, string.length()-1);
                    Log.d("List",strTaxId);

                }
            });
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_tax_rate_group.setNestedScrollingEnabled(false);
                    rv_tax_rate_group.setHasFixedSize(true);
                    rv_tax_rate_group.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_tax_rate_group.setAdapter(taxRateListAdapter);

                }

            });
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            //pb_list_category.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public ArrayList<TaxRateItem> getTax() {
        taxRateItemArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
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
            transport.debug = true;
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (!TextUtils.isEmpty(responseJSON)) {
                final JSONArray jarray = new JSONArray(responseJSON);
                for (int i = 0; i < jarray.length(); i++) {
                    final String TaxId = jarray.getJSONObject(i).getString("TaxId");
                    final String TaxRateName = jarray.getJSONObject(i).getString("TaxRateName");
                    final String TaxRate = jarray.getJSONObject(i).getString("TaxRate");
                    final String TaxType = jarray.getJSONObject(i).getString("TaxType");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            taxRateItemArrayList.add(new TaxRateItem(TaxId, TaxRateName,TaxRate,TaxType));
                            if (taxRateListAdapter
                                    != null) {
                                taxRateListAdapter
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
        return taxRateItemArrayList;
    }

    private class AsyncCallAddTaxGroup extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //pb_list_category.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String strTaxName = params[0];
            String strTaxRate = params[1];
            addTaxGroup(strTaxName,strTaxRate);
            return strTaxName;
        }

        @Override
        protected void onPostExecute(String result) {
            //pb_list_category.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void addTaxGroup(String GroupName,String TaxId) {
        String SOAP_ACTION = "http://tempuri.org/SaveTaxGroup";
        String METHOD_NAME = "SaveTaxGroup";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("GroupName", GroupName);
            Request.addProperty("TaxId", TaxId);
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
                        Toast.makeText(getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                        AsyncCallTaxGroupList asyncCallWS = new AsyncCallTaxGroupList();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        alertDialogroute.dismiss();
                    }
                });

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<ListTaxGroup> getTaxGroupNames() {
        //final ArrayList<ListTaxGroupPojo> listTaxGroupPojoArrayList = new ArrayList<>();
        final ArrayList<ListTaxGroup> listTaxGroupArrayList = new ArrayList<>();
        final ArrayList<TaxList> taxListArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
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
                    String strTaxName = "";
                    for (int k =0;k<array.length();k++){
                        JSONObject jsonObject1 = array.getJSONObject(k);
                        final String TaxId = jsonObject1.getString("TaxId");
                        final String TaxType = jsonObject1.getString("TaxType");
                        final String TaxRateName = jsonObject1.getString("TaxRateName");
                        final String TaxRate = jsonObject1.getString("TaxRate");
                        strTaxName = strTaxName +"  "+ TaxRateName;
                        TaxList taxList = new TaxList();
                        taxList.setTaxId(TaxId);
                        taxList.setTaxRateName(TaxRateName);
                        taxList.setTaxRate(TaxRate);
                        taxList.setTaxType(TaxType);
                        taxListArrayList.add(taxList);

                    }
                    Log.d("strTaxName",strTaxName);
                    ListTaxGroup listTaxGroup = new ListTaxGroup();
                    listTaxGroup.setGroupName(strGroupName);
                    listTaxGroup.setTaxGroupId(strTaxGroupId);
                    listTaxGroup.setTaxList(taxListArrayList);
                    listTaxGroup.setTaxRateName(strTaxName);
                    listTaxGroupArrayList.add(listTaxGroup);
                }
            } else {
                Toast.makeText(getContext(), "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listTaxGroupArrayList;
    }

    private class AsyncCallTaxGroupList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            taxGroupListAdapter = new TaxGroupListAdapterTest(getTaxGroupNames(),getContext());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_tax_group.setNestedScrollingEnabled(false);
                    rv_tax_group.setHasFixedSize(true);
                    rv_tax_group.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_tax_group.setAdapter(taxGroupListAdapter);

                }

            });
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            //pb_list_category.setVisibility(View.GONE);
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
            Log.d("Visible" ,"Category Visible");
            AsyncCallTaxGroupList asyncCallWS = new AsyncCallTaxGroupList();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isFragmentLoaded = true;
        }
    }

    public void scrollFabButton(){
        rv_tax_group.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && fb_tax_group.getVisibility() == View.VISIBLE) {
                    fb_tax_group.hide();
                } else if (dy > 0 && fb_tax_group.getVisibility() != View.VISIBLE) {
                    fb_tax_group.show();
                }
            }
        });
    }
}
