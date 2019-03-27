package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.activity.AddCompanyActivity;
import com.example.admin.demo.functions.OnRefreshView;
import com.example.admin.demo.item.ItemCompanyList;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.MyViewHolder> {


    private List<ItemCompanyList> companyLists;
    private Context context;
    private OnRefreshView itemListener;
    public static boolean isCompanyEdit = false;

    public CompanyListAdapter(Context context, ArrayList<ItemCompanyList> companyLists) {
        this.context = context;
        this.companyLists = companyLists;
        itemListener = (OnRefreshView) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemCompanyList itemCompanyList = companyLists.get(position);
        holder.tv_item_company.setText(itemCompanyList.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_name = companyLists.get(position).getName();
                String code = companyLists.get(position).getCompid();
                Intent intent = new Intent(context, AddCompanyActivity.class);
                intent.putExtra("COMP_ID",companyLists.get(position).getCompid());
                isCompanyEdit = true;
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return companyLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item_company;
        public MyViewHolder(View view) {
            super(view);
            tv_item_company = view.findViewById(R.id.tv_item_company);


        }
    }

}