package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.OnItemCheckListener;
import com.example.admin.demo.item.TaxRateItem;

import java.util.ArrayList;
import java.util.List;

public class TaxRateListAdapter extends RecyclerView.Adapter<TaxRateListAdapter.MyViewHolder> {

    private List<TaxRateItem> taxRateItems;
    private Context context;

    private OnItemCheckListener onItemCheckListener;

    public TaxRateListAdapter(List<TaxRateItem> taxRateItems, OnItemCheckListener onItemCheckListener) {
        this.taxRateItems = taxRateItems;
        this.onItemCheckListener = onItemCheckListener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TaxRateItem taxRateItem = taxRateItems.get(position);
        holder.tv_tax_name.setText(taxRateItem.getStrName());
        holder.tv_tax_rate.setText(taxRateItem.getStrRate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyViewHolder) holder).cb_selected.setChecked(
                        !((MyViewHolder) holder).cb_selected.isChecked());
                if (((MyViewHolder) holder).cb_selected.isChecked()) {
                    onItemCheckListener.onItemCheck(taxRateItem);
                } else {
                    onItemCheckListener.onItemUncheck(taxRateItem);
                }
            }
        });

        holder.cb_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((MyViewHolder) holder).cb_selected.setChecked(
//                        !((MyViewHolder) holder).cb_selected.isChecked());
                if (((MyViewHolder) holder).cb_selected.isChecked()) {
                    onItemCheckListener.onItemCheck(taxRateItem);
                } else {
                    onItemCheckListener.onItemUncheck(taxRateItem);
                }
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_tax_name, tv_tax_rate;
        private CheckBox cb_selected;

        public MyViewHolder(View view) {
            super(view);
            tv_tax_name = view.findViewById(R.id.tv_tax_name);
            tv_tax_rate = view.findViewById(R.id.tv_tax_rate);
            cb_selected = view.findViewById(R.id.cb_selected);
            cb_selected.setClickable(false);
        }
        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_rate_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return taxRateItems.size();
    }

    public interface OnItemCheckListener {
        void onItemCheck(TaxRateItem item);
        void onItemUncheck(TaxRateItem item);
    }
}