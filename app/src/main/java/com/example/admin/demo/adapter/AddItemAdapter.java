package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.item.ItemMenu1;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.MyViewHolder> {


    private List<ItemMenu1> itemMenu1s;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_category;
        public ProgressBar pb_gallery_img;
        public ImageView itemImage;
        public TextView tv_product_details;
        EditText tv_qty, price;


        public MyViewHolder(View view) {
            super(view);


            //ll_category = (LinearLayout) view.findViewById(R.id.ll_category);
            tv_product_details = view.findViewById(R.id.tv_product_details);
            tv_qty = view.findViewById(R.id.tv_qty);
            price = view.findViewById(R.id.price);


        }
    }


    public AddItemAdapter(Context context, ArrayList<ItemMenu1> itemMenu1s) {
        this.context = context;
        this.itemMenu1s = itemMenu1s;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.additem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemMenu1 itemMenu1 = itemMenu1s.get(position);
        holder.tv_product_details.setText(itemMenu1.getProductdestails());
        holder.tv_qty.setText(itemMenu1.getQty());
        holder.price.setText(itemMenu1.getPrice());


    }


    @Override
    public int getItemCount() {
        return itemMenu1s.size();
    }


}