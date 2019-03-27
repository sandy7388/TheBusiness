package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.ActivityAddItem;
import com.example.admin.demo.model.ProductDetailsPojo;

import java.util.ArrayList;
import java.util.List;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ProductItemsHolder> {

    private ArrayList<ProductDetailsPojo> productDetailsPojoList;
    private Context context;
    public static boolean isItemsEdit = false;

    public ProductItemsAdapter(ArrayList<ProductDetailsPojo> productDetailsPojoList, Context context) {
        this.productDetailsPojoList = productDetailsPojoList;
        this.context = context;
    }

    @Override
    public ProductItemsHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_product_items, viewGroup, false);
        return new ProductItemsHolder(view);

    }

    @Override
    public void onBindViewHolder( ProductItemsHolder holder, final int i) {
        final ProductDetailsPojo productDetailsPojo = productDetailsPojoList.get(i);
        holder.tv_product_name_adapter.setText(productDetailsPojo.getProductName());
        holder.tv_product_category_adapter.setText(productDetailsPojo.getCategoryName());
        holder.tv_product_stock_qty.setText(productDetailsPojo.getMinimumStockQty());
        holder.tv_product_stock_value.setText(productDetailsPojo.getOpeningStock());
        holder.tv_product_sale_price.setText(productDetailsPojo.getSalePrice());
        holder.tv_product_purchase_price.setText(productDetailsPojo.getPurchasePrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityAddItem.class);
                intent.putExtra("PRODUCT_ID",productDetailsPojoList.get(i).getProductId());
                isItemsEdit = true;
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetailsPojoList.size();
    }

    public class ProductItemsHolder extends RecyclerView.ViewHolder {
        private TextView tv_product_name_adapter,tv_product_category_adapter,
                tv_product_stock_qty,tv_product_stock_value,tv_product_sale_price,
                tv_product_purchase_price;
        public ProductItemsHolder( View itemView) {
            super(itemView);
            tv_product_name_adapter = itemView.findViewById(R.id.tv_product_name_adapter);
            tv_product_category_adapter = itemView.findViewById(R.id.tv_product_category_adapter);
            tv_product_stock_qty = itemView.findViewById(R.id.tv_product_stock_qty);
            tv_product_stock_value = itemView.findViewById(R.id.tv_product_stock_value);
            tv_product_sale_price = itemView.findViewById(R.id.tv_product_sale_price);
            tv_product_purchase_price = itemView.findViewById(R.id.tv_product_purchase_price);
        }
    }
}
