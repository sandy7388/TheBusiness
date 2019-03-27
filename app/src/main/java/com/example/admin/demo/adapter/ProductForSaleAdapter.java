package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.ProductDetailsPojo;

import java.util.ArrayList;

public class ProductForSaleAdapter extends RecyclerView.Adapter<ProductForSaleAdapter.MyViewHolder> {

    private ArrayList<ProductDetailsPojo> productDetailsPojoList;
    private Context context;
    private ProductIntefrace productIntefrace;

    public ProductForSaleAdapter(ArrayList<ProductDetailsPojo> productDetailsPojoList,
                                 Context context) {
        this.productDetailsPojoList = productDetailsPojoList;
        this.context = context;
        productIntefrace = (ProductIntefrace)context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_tax_name;
        private LinearLayout ll_TaxName;

        public MyViewHolder(View view) {
            super(view);
            tv_tax_name = view.findViewById(R.id.tv_tax_name);
            ll_TaxName = view.findViewById(R.id.ll_TaxName);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tax_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductDetailsPojo productDetailsPojo = productDetailsPojoList.get(position);
        holder.tv_tax_name.setText(productDetailsPojo.getProductName());
        holder.ll_TaxName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strProductId = productDetailsPojoList.get(position).getProductId();
                String strProductName = productDetailsPojoList.get(position).getProductName();
                String strPricePerUnit = productDetailsPojoList.get(position).getPricePerUnit();
                String strTaxId = productDetailsPojoList.get(position).getTaxId();
                productIntefrace.onProductSelected(strProductId,strProductName,strPricePerUnit,strTaxId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productDetailsPojoList.size();
    }


}