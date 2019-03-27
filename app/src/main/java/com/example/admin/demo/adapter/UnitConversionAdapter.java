package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.model.TaxListPojo;
import com.example.admin.demo.model.UnitConversionPojo;

import java.util.ArrayList;

public class UnitConversionAdapter extends RecyclerView.Adapter<UnitConversionAdapter.MyViewHolder> {

    private ArrayList<UnitConversionPojo> unitConversionPojos;
    private Context context;
    private CallBack callBack;
    private int lastSelectedPosition = -1;

    public UnitConversionAdapter(ArrayList<UnitConversionPojo> unitConversionPojos, Context context) {
        this.unitConversionPojos = unitConversionPojos;
        this.context = context;
        callBack = (CallBack)context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_conversion_rate,tv_secondary_unit;
        private LinearLayout ll_unit_conversion;
        private RadioButton rb_primary_unit;

        public MyViewHolder(View view) {
            super(view);
            tv_secondary_unit = view.findViewById(R.id.tv_secondary_unit);
            tv_conversion_rate = view.findViewById(R.id.tv_conversion_rate);
            rb_primary_unit = view.findViewById(R.id.rb_primary_unit);
            ll_unit_conversion = view.findViewById(R.id.ll_unit_conversion);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_unit_conversion, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UnitConversionPojo unitConversionPojo = unitConversionPojos.get(position);
        holder.tv_conversion_rate.setText(unitConversionPojo.getConversionRate());
        holder.tv_secondary_unit.setText(unitConversionPojo.getConversionUnitName());
        holder.rb_primary_unit.setText(" 1 " +unitConversionPojo.getMainUnitName());
        holder.rb_primary_unit.setChecked(lastSelectedPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = holder.getAdapterPosition();
                callBack.EditParty("",unitConversionPojos.get(position).getConversionId());
                notifyDataSetChanged();
            }
        });

        holder.rb_primary_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = holder.getAdapterPosition();
                callBack.EditParty("",unitConversionPojos.get(position).getConversionId());
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return unitConversionPojos.size();
    }


}