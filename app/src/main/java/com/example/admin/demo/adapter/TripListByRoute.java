package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.item.AllItem;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class TripListByRoute extends RecyclerView.Adapter<TripListByRoute.MyViewHolder> {


    private List<AllItem> allItemList;
    private Context context;
    private CallBack callBack;
    public String call_method1 = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_main_services;
        public ProgressBar pb_gallery_img;
        public ImageView itemImage;
        public TextView tv_main;
        public CardView card_company;


        public MyViewHolder(View view) {
            super(view);

            //ll_category = (LinearLayout) view.findViewById(R.id.ll_category);
            tv_main = view.findViewById(R.id.tv_main);
            ll_main_services = view.findViewById(R.id.ll_main_services);


        }
    }


    public TripListByRoute(Context context, ArrayList<AllItem> allItemList, String call_method) {

        this.context = context;
        this.allItemList = allItemList;
        this.call_method1 = call_method;
        callBack = (CallBack) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_name, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AllItem allItem = allItemList.get(position);
        holder.tv_main.setText(allItem.getName());


        holder.ll_main_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_name = allItemList.get(position).getName();
                String id = allItemList.get(position).getId();

                if (call_method1.equals("trip")) {
                    callBack.AddParty(str_name, "Trip",id);
                } else if (call_method1.equals("route")) {

                    callBack.AddParty(str_name, "route",id);
                } else if (call_method1.equals("party_type")) {
                    callBack.AddParty(str_name, "partytype",id);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return allItemList.size();
    }


}