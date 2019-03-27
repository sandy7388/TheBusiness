package com.example.admin.demo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.functions.CallBack;
import com.example.admin.demo.interfaces.ProductAdapterInterface;
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.ProductDetailsPojo;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<ProductDetailsPojo> {
    private ArrayList<ProductDetailsPojo> customers, tempCustomer, suggestions;
    private ProductIntefrace productIntefrace;
    private ProductAdapterInterface productAdapterInterface;
    private String strProductId,strProductName,strPricePerUnit,
            strTaxId,strTripId,strUnitId,strUnitName,strSalesPrice,strPurchasePrice;
    public ProductAdapter(Context context, ArrayList<ProductDetailsPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<ProductDetailsPojo>(objects);
        this.suggestions = new ArrayList<ProductDetailsPojo>(objects);
        productIntefrace = (ProductIntefrace)context;
        productAdapterInterface = (ProductAdapterInterface)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProductDetailsPojo customer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_row, parent, false);
        }
        LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvCustomer);
        TextView txtCustomerBalance = (TextView) convertView.findViewById(R.id.tv_customer_balance);
        if (txtCustomer != null){
            txtCustomer.setText(customer.getProductName());
        }
        if (txtCustomerBalance != null){
            txtCustomerBalance.setText(customer.getPricePerUnit());
        }
        ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer != null) {
                    strProductId = customer.getProductId();
                    strProductName = customer.getProductName();
                    strSalesPrice = customer.getSalePrice();
                    strTaxId = customer.getTaxGroupName();
                    strUnitId = customer.getUnitId();
                    strPurchasePrice = customer.getPurchasePrice();
                }
                productAdapterInterface.UnitDetails(strUnitId,strPurchasePrice);
                productIntefrace.onProductSelected(strProductId,strProductName,strSalesPrice,strTaxId);
            }
        });

        // Now assign alternate color for rows
        if (position % 2 == 0)
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.odd));
        else
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.even));

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductDetailsPojo customer = (ProductDetailsPojo) resultValue;
            return customer.getProductName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ProductDetailsPojo people : tempCustomer) {
                    if (people.getProductName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ProductDetailsPojo> c = (ArrayList<ProductDetailsPojo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProductDetailsPojo cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public void updateList(List<ProductDetailsPojo> list){
        customers = new ArrayList<>();
        customers.addAll(list);
        notifyDataSetChanged();
    }
}
