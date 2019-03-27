package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.PartyInvoiceActivity;
import com.example.admin.demo.activity.SalesItemActivity;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.model.PartyInvoicePojo;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PartyInvoiceAdapter extends RecyclerView.Adapter<PartyInvoiceAdapter.ProductInvoiceHolder> {

    private ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList;
    private Context context;
    private PartyDetails partyDetails;
    private SoapPrimitive resultString;
    private ItemDetailsAdapter itemDetailsAdapter;
    private DecimalFormat df;
    double subtotal = 0,total_qty = 0;
    private String strInvoiceNo,strAmount,strSalesId,strPartyId,
            strTripId,strRouteId,strPartyBalance;
    private ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList;
    public static boolean isEditClicked = false;

    public PartyInvoiceAdapter(ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList, Context context) {
        this.partyInvoicePojoArrayList = partyInvoicePojoArrayList;
        this.context = context;
        partyDetails = (PartyDetails)context;
        invoiceItemDetailsArrayList = new ArrayList<>();
    }

    @Override
    public ProductInvoiceHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_party_invoice, viewGroup, false);
        return new ProductInvoiceHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductInvoiceHolder holder,final int i) {
        final PartyInvoicePojo partyInvoicePojo = partyInvoicePojoArrayList.get(i);
        invoiceItemDetailsArrayList = partyInvoicePojoArrayList.get(i).getInvoiceItemDetailsArrayList();
        holder.tv_invoice_no_adapter.setText(partyInvoicePojo.getInvoiceNo());
        holder.tv_invoice_date_adapter.setText(partyInvoicePojo.getInvoiceDate());
        holder.tv_invoice_total_adapter.setText(partyInvoicePojo.getInvoiceAmount());
        holder.tv_invoice_received_adapter.setText(partyInvoicePojo.getInvoicePaidAmount());
        holder.tv_invoice_balance_adapter.setText(partyInvoicePojo.getInvoicePendingAmount());
        if (partyInvoicePojoArrayList.get(i).getInvoiceStatus().equals("Open Orders")){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFE0"));

            holder.iv_convert_to_sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String salesId = partyInvoicePojoArrayList.get(i).getSalesInvoiceId();
                    String status = "Closed Orders";
                    //logoutConfirmation(salesId,status);
                    ((PartyInvoiceActivity)context).logoutConfirmation(salesId,status);

                }
            });

        }
        else if (partyInvoicePojoArrayList.get(i).getInvoiceStatus().equals("Closed Orders"))
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#E6E6FA"));
        }
                df = new DecimalFormat("#0.00");
        holder.ll_item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList = partyInvoicePojoArrayList.get(i).getInvoiceItemDetailsArrayList();
                if (holder.ll_item_list_invoice.getVisibility() == View.GONE){
                    holder.ll_item_list_invoice.setVisibility(View.VISIBLE);
                    holder.iv_invoice_down_arrow.setVisibility(View.GONE);
                    holder.iv_invoice_up_arrow.setVisibility(View.VISIBLE);
                    strInvoiceNo = partyInvoicePojoArrayList.get(i).getInvoiceNo();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    holder.rv_item_details.setLayoutManager(layoutManager);
                    holder.rv_item_details.setHasFixedSize(true);
                    holder.rv_item_details.setNestedScrollingEnabled(true);
                    itemDetailsAdapter = new ItemDetailsAdapter(invoiceItemDetailsArrayList,context);
                    holder.rv_item_details.setAdapter(itemDetailsAdapter);

                    double price = 0,qty = 0,tax = 0,total = 0;
                    for (int j = 0; j<invoiceItemDetailsArrayList.size();j++){
                        price = price+(invoiceItemDetailsArrayList.get(j).getTotal());
                        qty = qty+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getItemQty());
                        tax = tax+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                        total = total + (price *qty) + tax;

                    }
                    holder.tv_total_qty.setText(df.format(qty));
                    holder.tv_total_subtotal.setText(df.format(price));
                }
                else {
                    holder.ll_item_list_invoice.setVisibility(View.GONE);
                    holder.iv_invoice_down_arrow.setVisibility(View.VISIBLE);
                    holder.iv_invoice_up_arrow.setVisibility(View.GONE);
                }

            }
        });

        holder.ll_item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PartyInvoicePojo partyInvoicePojo = partyInvoicePojoArrayList.get(i);
                strSalesId = partyInvoicePojoArrayList.get(i).getSalesInvoiceId();
                strAmount = partyInvoicePojoArrayList.get(i).getInvoicePendingAmount();
                strInvoiceNo = partyInvoicePojoArrayList.get(i).getInvoiceNo();
                strPartyId = partyInvoicePojoArrayList.get(i).getPartyId();
                strTripId = partyInvoicePojoArrayList.get(i).getTripId();
                strRouteId = partyInvoicePojoArrayList.get(i).getRouteId();
                strPartyBalance = partyInvoicePojoArrayList.get(i).getPartyCurrentBalance();
                Intent intent = new Intent(context, SalesItemActivity.class);
                intent.putExtra("SALES_NO",strSalesId);
                intent.putExtra("PARTY_NAME", partyInvoicePojoArrayList.get(i).getPartyName());
                intent.putExtra("PARTY_BALANCE",strPartyBalance);
                intent.putExtra("PREVIOUS_AMOUNT",strAmount);
                intent.putExtra("INVOICE_NO",strInvoiceNo);
                intent.putExtra("PARTY_ID",strPartyId);
                intent.putExtra("TRIP_ID",strTripId);
                intent.putExtra("ROUTE_ID",strRouteId);
                isEditClicked = true;
                context.startActivity(intent);

            }
        });

        holder.iv_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof PartyInvoiceActivity){
                    ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList = partyInvoicePojoArrayList.get(i).getInvoiceItemDetailsArrayList();
                    ((PartyInvoiceActivity)context).printData(partyInvoicePojoArrayList.get(i).getPartyName(),
                            partyInvoicePojoArrayList.get(i).getInvoiceNo(),
                            partyInvoicePojoArrayList.get(i).getInvoiceDate(),
                            partyInvoicePojoArrayList.get(i).getPartyCurrentBalance(),
                            invoiceItemDetailsArrayList);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return partyInvoicePojoArrayList.size();
    }

    public class ProductInvoiceHolder extends RecyclerView.ViewHolder {
        private TextView tv_invoice_no_adapter,tv_invoice_date_adapter,
                tv_invoice_total_adapter,tv_invoice_received_adapter,
                tv_invoice_balance_adapter,tv_total_subtotal,tv_total_qty;
        private LinearLayout ll_item_details,ll_item_list_invoice,ll_item_click;
        private RecyclerView rv_item_details;
        private ImageView iv_invoice_down_arrow,iv_invoice_up_arrow,iv_convert_to_sale,iv_printer;
        public ProductInvoiceHolder( View itemView) {
            super(itemView);
            rv_item_details = itemView.findViewById(R.id.rv_item_details);
            ll_item_details = itemView.findViewById(R.id.ll_item_details);
            tv_invoice_no_adapter = itemView.findViewById(R.id.tv_invoice_no_adapter);
            tv_invoice_date_adapter = itemView.findViewById(R.id.tv_invoice_date_adapter);
            tv_invoice_total_adapter = itemView.findViewById(R.id.tv_invoice_total_adapter);
            tv_invoice_received_adapter = itemView.findViewById(R.id.tv_invoice_received_adapter);
            tv_invoice_balance_adapter = itemView.findViewById(R.id.tv_invoice_balance_adapter);
            iv_invoice_down_arrow = itemView.findViewById(R.id.iv_invoice_down_arrow);
            iv_invoice_up_arrow = itemView.findViewById(R.id.iv_invoice_up_arrow);
            ll_item_list_invoice = itemView.findViewById(R.id.ll_item_list_invoice);
            tv_total_subtotal = itemView.findViewById(R.id.tv_total_subtotal);
            tv_total_qty = itemView.findViewById(R.id.tv_total_qty);
            ll_item_click = itemView.findViewById(R.id.ll_item_click);
            iv_convert_to_sale = itemView.findViewById(R.id.iv_convert_to_sale);
            iv_printer = itemView.findViewById(R.id.iv_printer);
        }

    }

}
