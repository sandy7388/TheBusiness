package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.model.ExpenseDetailsListPojo;
import com.example.admin.demo.model.InvoiceItemDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExpenseListDetailsAdapter extends RecyclerView.Adapter<ExpenseListDetailsAdapter.MyViewHolder> {
    private DecimalFormat df;
    private ArrayList<ExpenseDetailsListPojo> invoiceItemDetailsArrayList;
    private Context context;

    public ExpenseListDetailsAdapter(ArrayList<ExpenseDetailsListPojo> invoiceItemDetailsArrayList,
                                     Context context) {
        this.invoiceItemDetailsArrayList = invoiceItemDetailsArrayList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item_discount,tv_item_price,tv_item_qty,tv_item_details,
                tv_item_tax;

        public MyViewHolder(View view) {
            super(view);
            tv_item_tax = view.findViewById(R.id.tv_item_tax);
            tv_item_discount = view.findViewById(R.id.tv_item_discount);
            tv_item_price = view.findViewById(R.id.tv_item_price);
            tv_item_qty = view.findViewById(R.id.tv_item_qty);
            tv_item_details = view.findViewById(R.id.tv_item_details);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_expense_list_details, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ExpenseDetailsListPojo invoiceItemDetails = invoiceItemDetailsArrayList.get(position);
        df = new DecimalFormat("#0.00");
        double itemPrice,itemQty,tax;
        holder.tv_item_details.setText(invoiceItemDetails.getItemName());
        holder.tv_item_price.setText(invoiceItemDetails.getUnitPrice());
        holder.tv_item_qty.setText(invoiceItemDetails.getQuantity());
        holder.tv_item_tax.setText(invoiceItemDetails.getTotalitemAmount());

    }

    @Override
    public int getItemCount() {
        return invoiceItemDetailsArrayList.size();
    }


}