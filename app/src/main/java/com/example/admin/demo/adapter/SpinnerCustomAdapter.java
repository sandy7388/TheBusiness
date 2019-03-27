package com.example.admin.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.admin.demo.R;
import com.example.admin.demo.model.ListTaxGroup;
import com.example.admin.demo.model.TaxList;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpinnerCustomAdapter extends ArrayAdapter<ListTaxGroup> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<ListTaxGroup> listTaxGroupPojoArrayList;
    private DecimalFormat df;
    public SpinnerCustomAdapter(Context context, ArrayList<ListTaxGroup> list){

        super(context,0, list);
        context =context;
        inflater = LayoutInflater.from(context);
        listTaxGroupPojoArrayList = list;
    }
    private View createItemView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(R.layout.spinner_custom_layout, parent, false);
        TextView tv_group_name = (TextView) view.findViewById(R.id.tv_spinner_group_name);
        TextView tv_group_rate = (TextView) view.findViewById(R.id.tv_spinner_group_rate);
        df = new DecimalFormat("#0.00");
        ListTaxGroup listTaxGroup = listTaxGroupPojoArrayList.get(position);
        double taxRate = Double.parseDouble(listTaxGroup.getTaxGroupRate());
        tv_group_name.setText(listTaxGroup.getGroupName());
        tv_group_rate.setText(df.format(taxRate));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public ListTaxGroup getItem(int position) {
        return listTaxGroupPojoArrayList.get(position);
    }
}
