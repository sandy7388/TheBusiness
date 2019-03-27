package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.interfaces.OnItemCheckListener;
import com.example.admin.demo.interfaces.OnSalesItemClick;
import com.example.admin.demo.item.AllItem;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class TripRouteAdapter extends RecyclerView.Adapter<TripRouteAdapter.MyViewHolder> {

    private List<AllItem> triprouteitem;
    private Context context;
    private onItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category;
        public ProgressBar pb_gallery_img;
        //public ImageView iv_edit;
        public TextView name_item_route_name,name_item_trip_name,iv_edit;

        public MyViewHolder(View view) {
            super(view);
            name_item_route_name = view.findViewById(R.id.name_item_route_and_trip);
            name_item_trip_name = view.findViewById(R.id.name_item_trip_and_trip);
            iv_edit = view.findViewById(R.id.iv_edit_party);


        }
    }

    public TripRouteAdapter(Context context, ArrayList<AllItem> triprouteitem,String call_method) {
        this.context = context;
        this.triprouteitem = triprouteitem;
        onItemClickListener = (onItemClickListener)context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_route_and_trip, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AllItem allItem = triprouteitem.get(position);

        holder.name_item_route_name.setText(allItem.getName());
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(triprouteitem.get(position).getId(),
                        triprouteitem.get(position).getName(),"route");
            }
        });
    }

    @Override
    public int getItemCount() {
        return triprouteitem.size();
    }


    public interface onItemClickListener{
        void onItemClick(String id,String name,String type);

    }
}