package com.example.admin.demo.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.fragment.UnitFragment;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.UnitConversionPojo;
import com.example.admin.demo.model.UnitPojo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ProductUnitAdapter extends RecyclerView.Adapter<ProductUnitAdapter.ProductUnitHolder> {
    private SoapPrimitive resultString;
    private Context context;
    private ArrayList<UnitPojo> unitPojoArrayList;
    private UnitConversionListAdapter unitConversionListAdapter;
    private String strUnitId;
    private onUnitClickListener onUnitClickListener;
    private UnitFragment unitFragment;

    public ProductUnitAdapter(Context context, ArrayList<UnitPojo> unitPojoArrayList, UnitFragment unitFragment) {
        this.context = context;
        this.unitPojoArrayList = unitPojoArrayList;
        this.unitFragment = unitFragment;
        onUnitClickListener = (onUnitClickListener)unitFragment;

    }

    public ProductUnitAdapter(Context context, ArrayList<UnitPojo> unitPojoArrayList) {
        this.context = context;
        this.unitPojoArrayList = unitPojoArrayList;
    }


    @Override
    public ProductUnitHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_product_unit, viewGroup, false);
        return new ProductUnitHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductUnitHolder holder, final int i) {
        UnitPojo unitPojo = unitPojoArrayList.get(i);
        holder.tv_category_name.setText(unitPojo.getUnitName());
        holder.tv_short_name.setText(unitPojo.getShortName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (holder.ll_hide_show.getVisibility() == View.GONE){
                        holder.ll_hide_show.setVisibility(View.VISIBLE);
                        strUnitId = unitPojoArrayList.get(i).getUnitId();
                        getUnitList(strUnitId,holder);
                    }
                    else {
                        holder.ll_hide_show.setVisibility(View.GONE);
                    }
                }catch (NetworkOnMainThreadException n)
                {
                    n.printStackTrace();
                }
            }
        });
        holder.iv_edit_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnitClickListener.onUnitClick(unitPojoArrayList.get(i).getUnitId(),
                        unitPojoArrayList.get(i).getShortName(),
                        unitPojoArrayList.get(i).getUnitName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitPojoArrayList.size();
    }

    public class ProductUnitHolder extends RecyclerView.ViewHolder {
        private TextView tv_category_name,tv_short_name,iv_edit_unit;
        private RecyclerView rv_unit_conversion_list;
        private LinearLayout ll_conversion_list,ll_hide_show;
        public ProductUnitHolder( View itemView) {
            super(itemView);
            ll_conversion_list = itemView.findViewById(R.id.ll_conversion_list);
            tv_category_name = itemView.findViewById(R.id.name_product_unit);
            rv_unit_conversion_list = itemView.findViewById(R.id.rv_unit_conversion_list);
            iv_edit_unit = itemView.findViewById(R.id.iv_edit_unit);
            tv_short_name = itemView.findViewById(R.id.name_unit_short);
            ll_hide_show = itemView.findViewById(R.id.ll_hide_show);
        }
    }

    public void getUnitList(final String strUnitId, final ProductUnitHolder holder){
        final ArrayList<UnitConversionPojo> unitConversionPojoArrayList = new ArrayList<>();
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String SOAP_ACTION = "http://tempuri.org/ListUnitConversionByUnit";
                String METHOD_NAME = "ListUnitConversionByUnit";
                String NAMESPACE = "http://tempuri.org/";
                String URL = "http://thebusiness.globaltechpie.co.in/cMasterData.asmx";
                try {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                    Request.addProperty("unitId", strUnitId);
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
                            final String MainUnitId = jsonObject.getString("MainUnitId");
                            final String MainUnitName = jsonObject.getString("MainUnitName");
                            final String ConversionRate = jsonObject.getString("ConversionRate");
                            final String ConversionId = jsonObject.getString("ConversionId");
                            final String ConversionUnitName = jsonObject.getString("ConversionUnitName");
                            final String ConversionUnitId = jsonObject.getString("ConversionUnitId");
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    UnitConversionPojo unitConversionPojo = new UnitConversionPojo();
                                    unitConversionPojo.setConversionRate(ConversionRate);
                                    unitConversionPojo.setConversionId(ConversionId);
                                    unitConversionPojo.setMainUnitId(MainUnitId);
                                    unitConversionPojo.setMainUnitName(MainUnitName);
                                    unitConversionPojo.setConversionUnitName(ConversionUnitName);
                                    unitConversionPojo.setConversionUnitId(ConversionUnitId);
                                    unitConversionPojoArrayList.add(unitConversionPojo);
                                    if (unitConversionListAdapter
                                            != null) {
                                        unitConversionListAdapter
                                                .notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(context, "Error From Server", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        holder.rv_unit_conversion_list.setLayoutManager(linearLayout);
                        holder.rv_unit_conversion_list.setHasFixedSize(true);
                        unitConversionListAdapter = new UnitConversionListAdapter(unitConversionPojoArrayList,context);
                        holder.rv_unit_conversion_list.setAdapter(unitConversionListAdapter);
                    }
                });

            }
        };

        asyncTask.execute(strUnitId);
    }

    public interface onUnitClickListener{
        void onUnitClick(String unitId,String shortName,String fullName);
    }
}
