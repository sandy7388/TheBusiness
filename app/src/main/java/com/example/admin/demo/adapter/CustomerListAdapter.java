package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.PartyInvoiceActivity;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.interfaces.EditParty;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.PartyDetailsPojo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder> {

    private ArrayList<CustomerDetailsPojo> customerDetailsPojoList;
    private Context context;
    private CallBack callBack;

    public CustomerListAdapter(ArrayList<CustomerDetailsPojo> customerDetailsPojoList,
                               Context context) {
        this.customerDetailsPojoList = customerDetailsPojoList;
        this.context = context;
        callBack = (CallBack)context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_customer_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CustomerDetailsPojo partyDetailsPojo = customerDetailsPojoList.get(position);
        holder.tv_customer_name.setText(partyDetailsPojo.getPartyName());
        holder.tv_customer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPartyId,strPartyName,strPartyBalance,strPartyNickName,strRouteId,strTripId;
                strPartyId = customerDetailsPojoList.get(position).getPartyId();
                strPartyNickName = customerDetailsPojoList.get(position).getNickName();
                strPartyName = customerDetailsPojoList.get(position).getPartyName();
                strPartyBalance = customerDetailsPojoList.get(position).getPartyCurrentBalance();
                strRouteId = customerDetailsPojoList.get(position).getRouteId();
                strTripId = customerDetailsPojoList.get(position).getTripId();
                callBack.AddParty(strPartyName,strPartyBalance,strPartyId);
                callBack.EditParty(strRouteId,strTripId);
            }
        });

    }


    @Override
    public int getItemCount() {
        return customerDetailsPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_customer_name;
        public MyViewHolder(View view) {
            super(view);
            tv_customer_name = view.findViewById(R.id.tv_customer_name);

        }
    }



}