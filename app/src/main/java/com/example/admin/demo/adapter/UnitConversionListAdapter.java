package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.admin.demo.R;
import com.example.admin.demo.model.UnitConversionPojo;

import java.util.ArrayList;

public class UnitConversionListAdapter extends RecyclerView.Adapter<UnitConversionListAdapter.MyViewHolder> {

  private ArrayList<UnitConversionPojo> taxListPojoArrayList;
  private Context context;

  public UnitConversionListAdapter(ArrayList<UnitConversionPojo> taxListPojoArrayList, Context context) {
    this.taxListPojoArrayList = taxListPojoArrayList;
    this.context = context;
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
    final UnitConversionPojo taxListPojo = taxListPojoArrayList.get(position);
    String strRate,strPrimaryName ,strSecondaryName;
    strRate = taxListPojo.getConversionRate();
    strPrimaryName = taxListPojo.getMainUnitName();
    strSecondaryName = taxListPojo.getConversionUnitName();
    holder.tv_tax_name.setText(" 1 " + strPrimaryName + " = " + strRate + " " + strSecondaryName);

  }

  @Override
  public int getItemCount() {
    return taxListPojoArrayList.size();
  }


}