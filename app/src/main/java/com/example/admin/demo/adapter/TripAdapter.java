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

import com.example.admin.demo.R;
import com.example.admin.demo.item.AllItem;
import com.example.admin.demo.model.TripListPojo;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private List<TripListPojo> tripListPojoList;
    private Context context;
    private OnRouteClickListener onRouteClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category;
        public ProgressBar pb_gallery_img;
        //public ImageView iv_edit;
        public TextView name_item_route_name,name_item_trip_name,iv_edit;

        public MyViewHolder(View view) {
            super(view);
            name_item_route_name = view.findViewById(R.id.name_item_route);
            name_item_trip_name = view.findViewById(R.id.name_item_trip);
            iv_edit = view.findViewById(R.id.iv_edit_trip);

        }
    }

    public TripAdapter(Context context,List<TripListPojo> tripListPojoList, String call_method) {
        this.context = context;
        this.tripListPojoList = tripListPojoList;
        onRouteClickListener = (OnRouteClickListener)context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_trip_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TripListPojo allItem = tripListPojoList.get(position);
        holder.name_item_route_name.setText(allItem.getRouteName());
        holder.name_item_trip_name.setText(allItem.getTripName());

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRouteClickListener.onClickListener(tripListPojoList.get(position).getTripId(),
                        tripListPojoList.get(position).getRouteId(),
                        tripListPojoList.get(position).getTripName(),"trip");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripListPojoList.size();
    }


    public interface OnRouteClickListener{
        void onClickListener(String tripId,String routeId,String tripName,String type);
    }
}