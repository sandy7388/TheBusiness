package com.example.admin.demo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.interfaces.OnSalesItemClick;
import com.example.admin.demo.model.InvoiceItemDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderItemDetailsAdapter extends RecyclerView.Adapter<OrderItemDetailsAdapter.MyViewHolder> {
    private ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList;
    private Context context;
    private DecimalFormat df;
    private String totalAmount,totalQty,itemPrice,unit,multiple,equals;
    private OnSalesItemClick onSalesItemClick;

    public OrderItemDetailsAdapter(ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList,
                                   Context context) {
        this.invoiceItemDetailsArrayList = invoiceItemDetailsArrayList;
        this.context = context;
        onSalesItemClick = (OnSalesItemClick)context;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_order_item_details, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, final int i) {
        final InvoiceItemDetails invoiceItemDetails = invoiceItemDetailsArrayList.get(i);
        df = new DecimalFormat("#0.00");
        double price,qty,totalItemPrice;
        holder.tv_order_item_name.setText(invoiceItemDetails.getProductName());
        holder.tv_order_item_price.setText((invoiceItemDetails.getItemQty()));
        price = Double.parseDouble((invoiceItemDetails.getItemPrice()));
        qty = Double.parseDouble((invoiceItemDetails.getItemQty()));
        totalItemPrice = price * qty;
        unit = invoiceItemDetails.getUnitName();
        totalQty = String.valueOf(df.format(qty));
        itemPrice = String.valueOf(df.format(price));
        String itemDetails = totalQty+" " + unit+" " + " X " + itemPrice+" " + " = ";
        holder.tv_order_item_details.setText(itemDetails);
        holder.tv_order_item_subtotal.setText(String.valueOf(df.format(totalItemPrice)));
        holder.tv_order_item_discount.setText("0.00");
        holder.tv_order_item_discount_amount.setText(invoiceItemDetails.getItemDiscountAmount());
        holder.tv_order_item_tax.setText("0.00");
        holder.tv_order_item_tax_amount.setText(invoiceItemDetails.getTaxAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InvoiceItemDetails invoiceItemDetails = invoiceItemDetailsArrayList.get(i);
                onSalesItemClick.onSalesItemClick(invoiceItemDetailsArrayList.get(i).getSalesInvoiceItemId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return invoiceItemDetailsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_order_item_name,tv_order_item_price,tv_order_item_subtotal,
                tv_order_item_discount,tv_order_item_discount_amount,tv_order_item_tax,
                tv_order_item_tax_amount,tv_order_item_details;
        public MyViewHolder( View itemView) {
            super(itemView);
            tv_order_item_name = itemView.findViewById(R.id.tv_order_item_name);
            tv_order_item_price = itemView.findViewById(R.id.tv_order_item_price);
            tv_order_item_subtotal = itemView.findViewById(R.id.tv_order_item_subtotal);
            tv_order_item_discount = itemView.findViewById(R.id.tv_order_item_discount);
            tv_order_item_discount_amount = itemView.findViewById(R.id.tv_order_item_discount_amount);
            tv_order_item_tax = itemView.findViewById(R.id.tv_order_item_tax);
            tv_order_item_tax_amount = itemView.findViewById(R.id.tv_order_item_tax_amount);
            tv_order_item_details = itemView.findViewById(R.id.tv_order_item_details);
        }
    }
}
