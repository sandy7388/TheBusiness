package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.model.ListTaxGroup;
import com.example.admin.demo.model.ListTaxGroupPojo;
import com.example.admin.demo.model.TaxGroupPojo;
import com.example.admin.demo.model.TaxList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaxGroupListAdapterTest extends RecyclerView.Adapter<TaxGroupListAdapterTest.MyViewHolder> {

    private List<ListTaxGroup> taxGroupPojoList;
    private Context context;

    public TaxGroupListAdapterTest(List<ListTaxGroup> taxGroupPojoList, Context context) {
        this.taxGroupPojoList = taxGroupPojoList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_group_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ListTaxGroup listTaxGroup1 = taxGroupPojoList.get(position);
        holder.tv_tax_group.setText(listTaxGroup1.getGroupName());
        holder.tv_tax_rate.setText(listTaxGroup1.getTaxRateName());
    }
    @Override
    public int getItemCount() {
        return taxGroupPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_tax_rate, tv_tax_group;


        public MyViewHolder(View view) {
            super(view);
            tv_tax_rate = view.findViewById(R.id.tv_tax_rate);
            tv_tax_group = view.findViewById(R.id.tv_tax_group);

        }
    }
}