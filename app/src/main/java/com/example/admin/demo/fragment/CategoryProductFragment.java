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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.adapter.ProductCategoryAdapter;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.session.SessionManagement;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryProductFragment extends Fragment implements View.OnClickListener,
        ProductCategoryAdapter.onCategoryClickListener {

    private SoapPrimitive resultString;
    private AlertDialog.Builder alertDialogBuilderroute;
    private AlertDialog alertDialogroute;
    private ProductCategoryAdapter productCategoryAdapter;
    private RecyclerView rv_service_product;
    private FloatingActionButton floatingActionButton;
    private TextView tv_add,tv_cancel,tv_delete;
    private EditText et_category_name;
    private ProgressBar pb_list_category;
    private String strCategoryName,strCategoryId;
    private boolean isEditCategory = false;
    private SessionManagement session;
    private String compid;
    public CategoryProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_category, container, false);
        rv_service_product = view.findViewById(R.id.rv_service_product);
        floatingActionButton = view.findViewById(R.id.fab_add_category);
        pb_list_category = view.findViewById(R.id.pb_list_category);
        floatingActionButton.setOnClickListener(this);
        session = new SessionManagement(getContext());
        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            //strCompanyName = user.get(SessionManagement.KEY_COMPANY_NAME);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);
        }
        scrollFabButton();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_category:
                isEditCategory = false;
                DialogAddCategory();
                break;
            case R.id.btn_delete_category:
                new AsyncCallDeleteCategory().execute(strCategoryId);
                alertDialogroute.dismiss();
                break;
        }
    }

    @Override
    public void onCategoryClick(String catId, String catName) {
        DialogAddCategory();
        isEditCategory = true;
        et_category_name.setText(catName);
        strCategoryId = catId;


    }


    private class AsyncCallAddCategory extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_list_category.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String SOAP_ACTION,METHOD_NAME,URL;
            if (isEditCategory){
                SOAP_ACTION = "http://tempuri.org/UpdateCategory";
                METHOD_NAME = "UpdateCategory";
                URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
            }

            else {
                SOAP_ACTION = "http://tempuri.org/SaveCategory";
                METHOD_NAME = "SaveCategory";
                URL = "http://thebusiness.globaltechpie.co.in/cProduct.asmx";
            }

            String NAMESPACE = "http://tempuri.org/";

            try {

                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                if (isEditCategory){
                    Request.addProperty("catId", strCategoryId);
                    Request.addProperty("categoryName", id);
                }
                else {
                    Request.addProperty("categoryName", id);
                }

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
                            if (isEditCategory){
                                Toast.makeText(getContext(), "Category Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Category Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                            AsyncCallCategory asyncCallWS = new AsyncCallCategory();
                            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
                            alertDialogroute.dismiss();
                        }
                    });

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_category.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }


    public void DialogAddCategory() {
        View promptsView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_category, null);
        alertDialogBuilderroute = new AlertDialog.Builder(getContext());
        alertDialogBuilderroute.setView(promptsView);
        alertDialogroute = alertDialogBuilderroute.create();
        tv_add = promptsView.findViewById(R.id.btn_add_category);
        tv_cancel = promptsView.findViewById(R.id.btn_cancel_category);
        tv_delete = promptsView.findViewById(R.id.btn_delete_category);
        et_category_name = promptsView.findViewById(R.id.et_add_category);
        if (isEditCategory){
            tv_delete.setVisibility(View.VISIBLE);
        }
        else {
            tv_delete.setVisibility(View.GONE);
        }
        tv_add.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_category_name.getText().toString())) {
                    Toast.makeText(getContext(), "Please Enter Category", Toast.LENGTH_SHORT).show();
                } else {
                    AsyncCallAddCategory asyncCallWS = new AsyncCallAddCategory();
                    asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, et_category_name.getText().toString());
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

    private class AsyncCallCategory extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pb_list_category.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            productCategoryAdapter = new ProductCategoryAdapter(getCategory(),CategoryProductFragment.this);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_service_product.setNestedScrollingEnabled(false);
                    rv_service_product.setHasFixedSize(true);
                    rv_service_product.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_service_product.setAdapter(productCategoryAdapter);

                }

            });
            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_category.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public ArrayList<ProductCategory> getCategory() {
        final ArrayList<ProductCategory> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListAllCategory";
        String METHOD_NAME = "ListAllCategory";
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
            transport.debug = true;
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String responseJSON = resultString.toString();
            if (!TextUtils.isEmpty(responseJSON)) {
                final JSONArray jarray = new JSONArray(responseJSON);
                for (int i = 0; i < jarray.length(); i++) {
                    final String TripName = jarray.getJSONObject(i).getString("CategoryName");
                    final String TripId = jarray.getJSONObject(i).getString("CategoryId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            allItems.add(new ProductCategory(TripName, TripId));
                                if (productCategoryAdapter
                                        != null) {
                                    productCategoryAdapter
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
            AsyncCallCategory asyncCallWS = new AsyncCallCategory();
            asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
            isFragmentLoaded = true;
        }
    }

    public void scrollFabButton(){
        rv_service_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy > 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });
    }

    private class AsyncCallDeleteCategory extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String SOAP_ACTION = "http://tempuri.org/DeleteCategory";
            String METHOD_NAME = "DeleteCategory";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
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
                            Toast.makeText(getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            AsyncCallCategory asyncCallWS = new AsyncCallCategory();
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


