package com.example.admin.demo.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.demo.activity.ActivityAddItem;
import com.example.admin.demo.R;
import com.example.admin.demo.activity.ActivityItemList;
import com.example.admin.demo.adapter.ProductItemsAdapter;
import com.example.admin.demo.model.ProductDetailsPojo;
import com.example.admin.demo.session.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductsItemFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab_add_product;
    private ProductItemsAdapter productItemsAdapter;
    private SoapPrimitive resultString;
    private RecyclerView rv_items;
    private ProgressBar pb_list_product;
    private ArrayList<ProductDetailsPojo> productDetailsPojos;
    private boolean shouldRefreshOnResume = false;
    private String compid;
    private SessionManagement session;
    public ProductsItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_product, container, false);
        fab_add_product = view.findViewById(R.id.fab_add_product);
        rv_items = view.findViewById(R.id.rv_items);
        pb_list_product = view.findViewById(R.id.pb_list_product);
        fab_add_product.setOnClickListener(this);
        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();
        if (user != null){
            //loginid = user.get(SessionManagement.KEY_LOGIN_ID);
            compid = user.get(SessionManagement.KEY_COMPANY_ID);

        }
        AsyncCallProductSList callProductSList = new AsyncCallProductSList();
        callProductSList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
        scrollFabButton();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_product:
                ProductItemsAdapter.isItemsEdit = false;
                startActivity(new Intent(getActivity(), ActivityAddItem.class));
                break;
        }
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
                    final String CategoryName = jsonObject.getString("CategoryName");
                    final String OpeningStock = jsonObject.getString("OpeningStock");
                    final String MinimumStockQty = jsonObject.getString("MinimumStockQty");
                    final String PurchasePrice = jsonObject.getString("PurchasePrice");
                    final String SalePrice = jsonObject.getString("SalePrice");
                    final String ProductId = jsonObject.getString("ProductId");
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProductDetailsPojo productDetailsPojo = new ProductDetailsPojo();
                            productDetailsPojo.setProductName(ProductName);
                            productDetailsPojo.setCategoryName(CategoryName);
                            productDetailsPojo.setOpeningStock(OpeningStock);
                            productDetailsPojo.setMinimumStockQty(MinimumStockQty);
                            productDetailsPojo.setPurchasePrice(PurchasePrice);
                            productDetailsPojo.setSalePrice(SalePrice);
                            productDetailsPojo.setProductId(ProductId);
                            productDetailsPojos.add(productDetailsPojo);
                            if (productItemsAdapter
                                    != null) {
                                productItemsAdapter
                                        .notifyDataSetChanged();
                            }
                        }
                    });
                }

            } else {
                Toast.makeText(getActivity(), "Error From Server", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return productDetailsPojos;
    }

    private class AsyncCallProductSList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            pb_list_product.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String routeId = params[0];
            productItemsAdapter = new ProductItemsAdapter(getProduct(),getContext() );
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_items.setNestedScrollingEnabled(false);
                    rv_items.setHasFixedSize(true);
                    rv_items.setLayoutManager(new LinearLayoutManager(getContext()));
                    rv_items.setAdapter(productItemsAdapter);

                }

            });

            return routeId;
        }

        @Override
        protected void onPostExecute(String result) {
            pb_list_product.setVisibility(View.GONE);
            if (result.equals("success")) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldRefreshOnResume){
            AsyncCallProductSList callProductSList = new AsyncCallProductSList();
            callProductSList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"1");
            shouldRefreshOnResume = false;
        }
    }

    public void scrollFabButton(){
        rv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && fab_add_product.getVisibility() == View.VISIBLE) {
                    fab_add_product.hide();
                } else if (dy > 0 && fab_add_product.getVisibility() != View.VISIBLE) {
                    fab_add_product.show();
                }
            }
        });
    }

}
