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
import com.example.admin.demo.interfaces.ProductIntefrace;
import com.example.admin.demo.model.UnitPojo;

import java.util.ArrayList;

public class SecondaryUnitAdapter extends ArrayAdapter<UnitPojo> {
    private ArrayList<UnitPojo> customers, tempCustomer, suggestions;
    private ProductIntefrace callBack;
    private String strUnitId,strUnitName,strShortName,strRouteId,strTripId;
    public SecondaryUnitAdapter(Context context, ArrayList<UnitPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<UnitPojo>(objects);
        this.suggestions = new ArrayList<UnitPojo>(objects);
        callBack = (ProductIntefrace)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UnitPojo customer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.unit_adapter, parent, false);
        }
        LinearLayout ll_custom_adapter = convertView.findViewById(R.id.ll_custom_adapter);
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvUnit);
        if (txtCustomer != null){
            txtCustomer.setText(customer.getUnitName());
        }
        ll_custom_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customer != null) {
                    strUnitId = customer.getUnitId();
                }
                if (customer != null) {
                    strUnitName = customer.getUnitName();
                }
                if (customer != null) {
                    strShortName = customer.getShortName();
                }

                callBack.onProductSelected(strUnitId,strUnitName,strShortName,"secondary");
                //callBack.EditParty(strRouteId,strTripId);
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
            UnitPojo customer = (UnitPojo) resultValue;
            return customer.getUnitName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (UnitPojo people : tempCustomer) {
                    if (people.getUnitName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            ArrayList<UnitPojo> c = (ArrayList<UnitPojo>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (UnitPojo cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
