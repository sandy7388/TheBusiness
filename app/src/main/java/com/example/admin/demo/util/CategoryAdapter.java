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
import com.example.admin.demo.model.CustomerDetailsPojo;
import com.example.admin.demo.model.ProductCategory;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<ProductCategory> {
    private ArrayList<ProductCategory> customers, tempCustomer, suggestions;
    private CallBack callBack;
    private String strPartyId,strPartyName,strPartyBalance,strRouteId,strTripId;
    public CategoryAdapter(Context context, ArrayList<ProductCategory> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<ProductCategory>(objects);
        this.suggestions = new ArrayList<ProductCategory>(objects);
        callBack = (CallBack)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ProductCategory customer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_row, parent, false);
        }
        LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvCustomer);
        TextView txtCustomerBalance = (TextView) convertView.findViewById(R.id.tv_customer_balance);
        if (txtCustomer != null){
            txtCustomer.setText(customer.getServiceName());
        }
        if (txtCustomerBalance != null){
            txtCustomerBalance.setText(customer.getServiceId());
        }
        ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer != null) {
                    strPartyId = customer.getServiceId();
                }
                if (customer != null) {
                    strPartyName = customer.getServiceName();
                }
                callBack.EditParty(strPartyId,strPartyName);
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
            ProductCategory customer = (ProductCategory) resultValue;
            return customer.getServiceName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ProductCategory people : tempCustomer) {
                    if (people.getServiceName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            ArrayList<ProductCategory> c = (ArrayList<ProductCategory>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProductCategory cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
