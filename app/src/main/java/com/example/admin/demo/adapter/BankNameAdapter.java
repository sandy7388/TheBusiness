package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.model.TaxListPojo;

import java.util.ArrayList;

public class BankNameAdapter extends RecyclerView.Adapter<BankNameAdapter.MyViewHolder> {

    private ArrayList<TaxListPojo> taxListPojoArrayList;
    private Context context;
    private CallBack callBack;

    public BankNameAdapter(ArrayList<TaxListPojo> taxListPojoArrayList, Context context) {
        this.taxListPojoArrayList = taxListPojoArrayList;
        this.context = context;
        callBack = (CallBack)context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_tax_name;
        private LinearLayout ll_TaxName;

        public MyViewHolder(View view) {
            super(view);
            tv_tax_name = view.findViewById(R.id.tv_tax_name);
            ll_TaxName = view.findViewById(R.id.ll_TaxName);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TaxListPojo taxListPojo = taxListPojoArrayList.get(position);
        holder.tv_tax_name.setText(taxListPojo.getStrTaxName());
        holder.ll_TaxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTaxName = taxListPojoArrayList.get(position).getStrTaxName();
                String strTaxId = taxListPojoArrayList.get(position).getStrTaxId();
                String strTaxRate = taxListPojoArrayList.get(position).getStrTaxRate();
                callBack.EditParty("tax",strTaxRate);
                callBack.AddParty(strTaxName,"tax",strTaxId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taxListPojoArrayList.size();
    }


}