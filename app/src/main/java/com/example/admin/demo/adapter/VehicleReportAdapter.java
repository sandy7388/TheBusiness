package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.model.VehicleReportPojo;

import java.util.ArrayList;

public class VehicleReportAdapter extends RecyclerView.Adapter<VehicleReportAdapter.VehicleReportHolder> {

    private ArrayList<VehicleReportPojo> vehicleReportPojoArrayList;
    private Context context;;

    public VehicleReportAdapter(ArrayList<VehicleReportPojo> vehicleReportPojoArrayList, Context context) {
        this.vehicleReportPojoArrayList = vehicleReportPojoArrayList;
        this.context = context;
    }

    @Override
    public VehicleReportHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.adapter_vehicle_report,viewGroup,false);
        return new VehicleReportHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleReportHolder holder, int i) {
        VehicleReportPojo vehicleReportPojo = vehicleReportPojoArrayList.get(i);
        holder.tv_item_name.setText(vehicleReportPojo.getStrItemName());
        holder.tv_item_qty.setText(vehicleReportPojo.getStrItemQty());
        holder.tv_item_unit.setText(vehicleReportPojo.getStrItemUnit());

    }

    @Override
    public int getItemCount() {
        return vehicleReportPojoArrayList.size();
    }

    public class VehicleReportHolder extends RecyclerView.ViewHolder {
        private TextView tv_item_unit,tv_item_qty,tv_item_name;
        public VehicleReportHolder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_qty = itemView.findViewById(R.id.tv_item_qty);
            tv_item_unit = itemView.findViewById(R.id.tv_item_unit);
        }
    }
}
