package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.fragment.CategoryProductFragment;
import com.example.admin.demo.model.ProductCategory;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductServiceHolder> {

    private Context context;
    private ArrayList<ProductCategory> productCategoryArrayList;
    private onCategoryClickListener onCategoryClickListener;
    private CategoryProductFragment categoryProductFragment;

    public ProductCategoryAdapter(Context context, ArrayList<ProductCategory> productCategoryArrayList) {
        this.context = context;
        this.productCategoryArrayList = productCategoryArrayList;
        onCategoryClickListener = (onCategoryClickListener)context;
    }

    public ProductCategoryAdapter(ArrayList<ProductCategory> productCategoryArrayList,
                                  CategoryProductFragment categoryProductFragment) {
        this.context = context;
        this.productCategoryArrayList = productCategoryArrayList;
        this.categoryProductFragment = categoryProductFragment;
        onCategoryClickListener = (onCategoryClickListener)categoryProductFragment;
    }

    @Override
    public ProductServiceHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_product_category, viewGroup, false);
        return new ProductServiceHolder(view);

    }

    @Override
    public void onBindViewHolder( ProductServiceHolder holder, final int i) {
        ProductCategory productCategory = productCategoryArrayList.get(i);
        holder.tv_category_name.setText(productCategory.getServiceName());

        holder.iv_edit_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClickListener.onCategoryClick(productCategoryArrayList.get(i).getServiceId(),
                        productCategoryArrayList.get(i).getServiceName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productCategoryArrayList.size();
    }

    public class ProductServiceHolder extends RecyclerView.ViewHolder {
        private TextView tv_category_name;
        private ImageView iv_edit_category;
        public ProductServiceHolder( View itemView) {
            super(itemView);
            tv_category_name = itemView.findViewById(R.id.name_product_category);
            iv_edit_category = itemView.findViewById(R.id.iv_edit_category);
        }
    }

    public interface onCategoryClickListener{
        void onCategoryClick(String catId,String catName);
    }
}
