package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.item.TaxRateItem;
import com.example.admin.demo.model.TaxGroupPojo;

import java.util.ArrayList;
import java.util.List;

public class TaxGroupListAdapter extends RecyclerView.Adapter<TaxGroupListAdapter.MyViewHolder> {

    private List<TaxGroupPojo> taxGroupPojoList;
    private Context context;

    public TaxGroupListAdapter(List<TaxGroupPojo> taxGroupPojoList, Context context) {
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
        final TaxGroupPojo taxGroupPojo = taxGroupPojoList.get(position);
        holder.tv_tax_rate.setText(taxGroupPojo.getTaxRateName());
        holder.tv_tax_group.setText(taxGroupPojo.getGroupName());


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