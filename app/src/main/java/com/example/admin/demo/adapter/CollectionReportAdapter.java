package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.AddUserActivity;
import com.example.admin.demo.model.CollectionReport;
import com.example.admin.demo.model.UserListPojo;

import java.util.List;

public class CollectionReportAdapter extends RecyclerView.Adapter<CollectionReportAdapter.MyViewHolder> {

    private List<CollectionReport> collectionReportList;
    private Context context;

    public CollectionReportAdapter(List<CollectionReport> collectionReportList, Context context) {
        this.collectionReportList = collectionReportList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_collection_report, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CollectionReport collectionReport = collectionReportList.get(position);
        holder.itemName.setText(collectionReport.getStrName());
        holder.tv_amount.setText(collectionReport.getStrAmount());
        holder.tv_item_date.setText(collectionReport.getStrDate());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName,tv_amount,tv_item_date;
        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.tv_item_menu);
            tv_amount = view.findViewById(R.id.btn_edit_party);
            tv_item_date = view.findViewById(R.id.tv_item_date);
        }
    }


    @Override
    public int getItemCount() {
        return collectionReportList.size();
    }

}