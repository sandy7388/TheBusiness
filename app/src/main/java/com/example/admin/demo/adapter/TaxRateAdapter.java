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

import com.example.admin.demo.item.TaxRateItem;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class TaxRateAdapter extends RecyclerView.Adapter<TaxRateAdapter.MyViewHolder> {

    private List<TaxRateItem> taxRateItems;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category;
        public ProgressBar pb_gallery_img;
        public ImageView itemImage;
        public TextView tv_tax_rate_1, tv_tax_rate_2;


        public MyViewHolder(View view) {
            super(view);


            //ll_category = (LinearLayout) view.findViewById(R.id.ll_category);
            tv_tax_rate_1 = view.findViewById(R.id.tv_tax_name);
            tv_tax_rate_2 = view.findViewById(R.id.tv_tax_rate);


        }
    }


    public TaxRateAdapter(Context context, ArrayList<TaxRateItem> taxRateItems) {
        this.context = context;
        this.taxRateItems = taxRateItems;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_rate, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TaxRateItem taxRateItem = taxRateItems.get(position);
        holder.tv_tax_rate_1.setText(taxRateItem.getStrName());
        holder.tv_tax_rate_2.setText(taxRateItem.getStrRate());


    }


    @Override
    public int getItemCount() {
        return taxRateItems.size();
    }


}