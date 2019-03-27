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

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<CustomerDetailsPojo> {
    private ArrayList<CustomerDetailsPojo> customers, tempCustomer, suggestions;
    private CallBack callBack;
    private String strPartyId,strPartyName,strPartyBalance,strRouteId,strTripId;
    public CustomerAdapter(Context context, ArrayList<CustomerDetailsPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<CustomerDetailsPojo>(objects);
        this.suggestions = new ArrayList<CustomerDetailsPojo>(objects);
        callBack = (CallBack)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CustomerDetailsPojo customer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_row, parent, false);
        }
        LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvCustomer);
        TextView txtCustomerBalance = (TextView) convertView.findViewById(R.id.tv_customer_balance);
        if (txtCustomer != null){
            txtCustomer.setText(customer.getPartyName());
        }
        if (txtCustomerBalance != null){
            txtCustomerBalance.setText(customer.getPartyCurrentBalance());
        }
        ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer != null) {
                    strPartyId = customer.getPartyId();
                }
                if (customer != null) {
                    strPartyName = customer.getPartyName();
                }
                if (customer != null) {
                    strPartyBalance = customer.getPartyCurrentBalance();
                }
                if (customer != null) {
                    strRouteId = customer.getRouteId();
                }
                if (customer != null) {
                    strTripId = customer.getTripId();
                }
                callBack.AddParty(strPartyName,strPartyBalance,strPartyId);
                callBack.EditParty(strRouteId,strTripId);
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
            CustomerDetailsPojo customer = (CustomerDetailsPojo) resultValue;
            return customer.getPartyName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CustomerDetailsPojo people : tempCustomer) {
                    if (people.getPartyName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            ArrayList<CustomerDetailsPojo> c = (ArrayList<CustomerDetailsPojo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CustomerDetailsPojo cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }


    };

}
