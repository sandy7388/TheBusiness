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
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.UnitPojo;

import java.util.ArrayList;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.MyViewHolder> {

    private ArrayList<UnitPojo> unitPojoList;
    private Context context;
    private CallBack callBack;

    public UnitAdapter(ArrayList<UnitPojo> unitPojoList, Context context) {
        this.unitPojoList = unitPojoList;
        this.context = context;
        callBack = (CallBack)context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_unit_name,tv_unit_id;
        private LinearLayout ll_unitName;

        public MyViewHolder(View view) {
            super(view);
            tv_unit_name = view.findViewById(R.id.tv_unit_name);
            tv_unit_id = view.findViewById(R.id.tv_category_id);
            ll_unitName = view.findViewById(R.id.ll_unitName);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_unit_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final UnitPojo unitPojo = unitPojoList.get(position);
        holder.tv_unit_name.setText(unitPojo.getUnitName());
        holder.ll_unitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCategoryName = unitPojoList.get(position).getShortName();
                String strCategoryId = unitPojoList.get(position).getUnitId();
                callBack.AddParty(strCategoryName,"unit",strCategoryId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return unitPojoList.size();
    }


}