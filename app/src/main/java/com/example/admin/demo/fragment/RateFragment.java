package com.example.admin.demo.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.adapter.TaxRateAdapter;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.item.TaxRateItem;
import com.example.admin.demo.R;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class RateFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private FloatingActionButton fab_add_tax_rate;
    private RecyclerView rv_tax_rate;
    private TaxRateAdapter taxRateAdapter;
    private SoapPrimitive resultString;
    private AlertDialog.Builder alertDialogBuilderroute;
    private AlertDialog alertDialogroute;
    private TextView tv_add,tv_cancel;
    private EditText et_tax_name,et_tax_rate;
    private ProgressBar pb_list_category;
    private Spinner sp_tax_type;
    private String[] country = { "IGST", "CGST", "SGST"};
    private String strTaxType;
    public RateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_tax_rate, container, false);
        fab_add_tax_rate = view.findViewById(R.id.fab_add_tax_rate);
        rv_tax_rate = view.findViewById(R.id.rv_tax_rate);
        pb_list_category = view.findViewById(R.id.pb_list_category);
        Functions.setDatatoRecyclerView(rv_tax_rate, taxRateAdapter, getActivity(),true);
        fab_add_tax_rate.setOnClickListener(this);
        scrollFabButton();
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_tax_rate:
                DialogTaxCategory();
                break;
        }

    }

    public void DialogTaxCategory() {
        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_tax, null);
        alertDialogBuilderroute = new AlertDialog.Builder(getContext());
        alertDialogBuilderroute.setView(promptsView);
        alertDialogroute = alertDialogBuilderroute.create();
        tv_add = promptsView.findViewById(R.id.btn_add_tax);
        tv_cancel = promptsView.findViewById(R.id.btn_cancel_tax);
        et_tax_name = promptsView.findViewById(R.id.et_add_tax_name);
        et_tax_rate = promptsView.findViewById(R.id.et_add_tax_rate);
        sp_tax_type = promptsView.findViewById(R.id.sp_tax_type);
        sp_tax_type.setOnItemSelectedListener(this);
        tv_add.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_tax_type.setAdapter(aa);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_tax_name.getText().toString())) {
                    Toast.makeText(getContext(), "Please Enter Tax Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_tax_rate.getText().toString())) {
                    Toast.makeText(getContext(), "Please Enter Tax Rate", Toast.LENGTH_SHORT).show();
                }else if (sp_tax_type.getSelectedItem() ==null) {
                    Toast.makeText(getContext(), "Please Select Tax Type", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AsyncCallAddTax asyncCallAddTax = new AsyncCallAddTax();
                    asyncCallAddTax.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            et_tax_name.getText().toString(),
                            et_tax_rate.getText().toString(),
                            strTaxType);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getContext(),country[position] , Toast.LENGTH_LONG).show();
        strTaxType = country[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class AsyncCallAddTax extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //pb_list_category.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String strTaxName = params[0];
            String strTaxRate = params[1];
            String strTaxType = params[2];
            addTax(strTaxName,strTaxRate,strTaxType);
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

    public void addTax(String taxName,String rate,String type) {
        String SOAP_ACTION = "http://tempuri.org/SaveTaxRate";
        String METHOD_NAME = "SaveTaxRate";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("taxName", taxName);
            Request.addProperty("rate", rate);
            Request.addProperty("type", type);
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

                        AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
                        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                        alertDialogroute.dismiss();
                    }
                });

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class AsyncCallWSTsklist extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            taxRateAdapter = new TaxRateAdapter(getContext(), getTax());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_tax_rate.setNestedScrollingEnabled(false);
                    rv_tax_rate.setHasFixedSize(true);
                    rv_tax_rate.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_tax_rate.setAdapter(taxRateAdapter);

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
        final ArrayList<TaxRateItem> allItems = new ArrayList<>();
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
                            allItems.add(new TaxRateItem(TaxId, TaxRateName,TaxRate,TaxType));
                            if (taxRateAdapter
                                    != null) {
                                taxRateAdapter
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

    private boolean isFragmentLoaded=false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFragmentLoaded ) {
            Log.d("Visible" ,"Category Visible");
            AsyncCallWSTsklist asyncCallWS = new AsyncCallWSTsklist();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isFragmentLoaded = true;
        }
    }

    public void scrollFabButton(){
        rv_tax_rate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && fab_add_tax_rate.getVisibility() == View.VISIBLE) {
                    fab_add_tax_rate.hide();
                } else if (dy > 0 && fab_add_tax_rate.getVisibility() != View.VISIBLE) {
                    fab_add_tax_rate.show();
                }
            }
        });
    }
}


