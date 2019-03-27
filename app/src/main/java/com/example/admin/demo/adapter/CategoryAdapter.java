package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.model.CategoryListPojo;
import com.example.admin.demo.model.ProductCategory;
import com.example.admin.demo.model.TripListPojo;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<ProductCategory> productCategoryList;
    private Context context;
    private CallBack callBack;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_categoryName,tv_category_id;
        private LinearLayout ll_categoryName;

        public MyViewHolder(View view) {
            super(view);
            tv_categoryName = view.findViewById(R.id.tv_category_name);
            tv_category_id = view.findViewById(R.id.tv_category_id);
            ll_categoryName = view.findViewById(R.id.ll_categoryName);

        }
    }

    public CategoryAdapter(List<ProductCategory> productCategoryList, Context context) {
        this.productCategoryList = productCategoryList;
        this.context = context;
        callBack = (CallBack) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProductCategory categoryListPojo = productCategoryList.get(position);
        holder.tv_categoryName.setText(categoryListPojo.getServiceName());
        holder.ll_categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCategoryName = productCategoryList.get(position).getServiceName();
                String strCategoryId = productCategoryList.get(position).getServiceId();
                callBack.AddParty(strCategoryName,"category",strCategoryId);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }


}