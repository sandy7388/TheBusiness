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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.admin.demo.adapter.SalesPartyListAdapter;
import com.example.admin.demo.activity.AddPartyActivity;
import com.example.admin.demo.functions.Functions;
import com.example.admin.demo.item.ItemMenu1;
import com.example.admin.demo.R;

import org.json.JSONArray;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;


public class PartyMenu1 extends Fragment {
    private static final String[] ITEMS = {"Category 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
    private static final String[] ITEMS1 = {"Category 2", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

    private ArrayAdapter<String> adapter, adapter2;

    private SoapPrimitive resultString;
    MaterialSpinner sp_category_1;
    private RecyclerView rv_partymenu1;
    private SalesPartyListAdapter salesPartyListAdapter;
    private FloatingActionButton floatingbutton_addparty;

    public PartyMenu1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tax_group, container, false);
        sp_category_1 = view.findViewById(R.id.sp_category_1);
        floatingbutton_addparty = view.findViewById(R.id.floatingbutton_addparty);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initSpinnerHintAndFloatingLabel();
        rv_partymenu1 = view.findViewById(R.id.rv_partymenu1);

        AsyncCallWSList asyncCallWS = new AsyncCallWSList();
        asyncCallWS.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "1");
        //salesPartyListAdapter = new SalesPartyListAdapter(getActivity(), getData());
        Functions.setDatatoRecyclerView(rv_partymenu1, salesPartyListAdapter, getActivity(), true);
        rv_partymenu1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && floatingbutton_addparty.getVisibility() == View.VISIBLE) {
                    floatingbutton_addparty.hide();
                } else if (dy > 0 && floatingbutton_addparty.getVisibility() != View.VISIBLE) {
                    floatingbutton_addparty.show();
                }
            }
        });
        floatingbutton_addparty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(getActivity(), AddPartyActivity.class);
                startActivity(intents);

            }
        });
        return view;
    }

    private void initSpinnerHintAndFloatingLabel() {
        sp_category_1.setAdapter(adapter);
        sp_category_1.setPaddingSafe(0, 0, 0, 0);

    }


    private class AsyncCallWSList extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            //salesPartyListAdapter = new SalesPartyListAdapter(getActivity(), getList());
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    rv_partymenu1.setNestedScrollingEnabled(false);
                    rv_partymenu1.setHasFixedSize(true);
                    rv_partymenu1.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_partymenu1.setAdapter(salesPartyListAdapter);

                }

            });


            return id;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public ArrayList<ItemMenu1> getData() {
        ArrayList<ItemMenu1> itemMenu1s = new ArrayList<>();
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Sugriv Yadav"));
        itemMenu1s.add(new ItemMenu1("Prathmesh Deshmukh"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        itemMenu1s.add(new ItemMenu1("Dipak Sonawane"));
        if (salesPartyListAdapter != null) {
            salesPartyListAdapter.notifyDataSetChanged();
        }
        return itemMenu1s;
    }


    public ArrayList<ItemMenu1> getList() {
        final ArrayList<ItemMenu1> allItems = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListAllParty";
        String METHOD_NAME = "ListAllParty";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cRegistration.asmx";
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
                            allItems.add(new ItemMenu1(""));
                            if (salesPartyListAdapter
                                    != null) {
                                salesPartyListAdapter
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
        return allItems;
    }

}