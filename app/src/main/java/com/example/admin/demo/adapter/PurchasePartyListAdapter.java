package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.MainActivity;
import com.example.admin.demo.activity.PartyInvoiceActivity;
import com.example.admin.demo.model.PartyDetailsPojo;

import java.util.ArrayList;
import java.util.List;

public class PurchasePartyListAdapter extends RecyclerView.Adapter<PurchasePartyListAdapter.MyViewHolder> {

    private List<PartyDetailsPojo> partyDetailsPojoList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category;
        public ProgressBar pb_gallery_img;
        public ImageView itemImage;
        public TextView tv_service_category, itemName,tv_amount;
        private Button btn_edit;
        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.tv_item_menu);
            tv_amount = view.findViewById(R.id.btn_edit_party);
        }
    }
    public PurchasePartyListAdapter(Context context, ArrayList<PartyDetailsPojo> partyDetailsPojoList) {
        this.context = context;
        this.partyDetailsPojoList = partyDetailsPojoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu1, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PartyDetailsPojo partyDetailsPojo = partyDetailsPojoList.get(position);
        if (partyDetailsPojo.getNickName().equals("") || partyDetailsPojo.getNickName().equals("0")){
            holder.itemName.setText(partyDetailsPojo.getPartyName());
        }
        else {
            holder.itemName.setText(partyDetailsPojo.getNickName());
        }

        holder.tv_amount.setText(partyDetailsPojo.getPartyCurrentBalance());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PartyInvoiceActivity.class);
                intent.putExtra("PARTY_ID",partyDetailsPojoList.get(position).getPartyId());
                intent.putExtra("PARTY_NAME",partyDetailsPojoList.get(position).getPartyName());
                intent.putExtra("PARTY_BALANCE",partyDetailsPojoList.get(position).getPartyCurrentBalance());
                intent.putExtra("ROUTE_ID",partyDetailsPojoList.get(position).getRouteId());
                intent.putExtra("TRIP_ID",partyDetailsPojoList.get(position).getTripId());
                intent.putExtra("NICK_NAME",partyDetailsPojoList.get(position).getNickName());
                MainActivity.isCashSale = false;
                MainActivity.isPurchase = true;
                MainActivity.isSale = false;
                context.startActivity(intent);
            }
        });

    }

    public void updateList(List<PartyDetailsPojo> list){
        partyDetailsPojoList = new ArrayList<>();
        partyDetailsPojoList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return partyDetailsPojoList.size();
    }


}