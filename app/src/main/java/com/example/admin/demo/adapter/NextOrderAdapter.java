package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.MainActivity;
import com.example.admin.demo.activity.NextDayOrderActivity;
import com.example.admin.demo.activity.SalesItemActivity;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.model.PartyInvoicePojo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NextOrderAdapter extends RecyclerView.Adapter<NextOrderAdapter.ProductInvoiceHolder> {

    private ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList;
    private Context context;
    private PartyDetails partyDetails;
    private SoapPrimitive resultString;
    private ItemDetailsAdapter itemDetailsAdapter;
    private DecimalFormat df;
    private String strInvoiceNo,strAmount,strSalesId,strPartyId,
            strTripId,strRouteId,strPartyBalance;
    public static boolean isEditClicked = false;
    public NextOrderAdapter(ArrayList<PartyInvoicePojo> partyInvoicePojoArrayList, Context context) {
        this.partyInvoicePojoArrayList = partyInvoicePojoArrayList;
        this.context = context;
        partyDetails = (PartyDetails)context;
    }

    @Override
    public ProductInvoiceHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_next_order, viewGroup, false);
        return new ProductInvoiceHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductInvoiceHolder holder,final int i) {
        final PartyInvoicePojo partyInvoicePojo = partyInvoicePojoArrayList.get(i);
        holder.tv_invoice_no_adapter.setText(partyInvoicePojo.getInvoiceNo());
        holder.tv_invoice_date_adapter.setText(partyInvoicePojo.getInvoiceDate());
        holder.tv_invoice_total_adapter.setText(partyInvoicePojo.getInvoiceAmount());
        holder.tv_invoice_received_adapter.setText(partyInvoicePojo.getInvoicePaidAmount());
        holder.tv_invoice_balance_adapter.setText(partyInvoicePojo.getInvoicePendingAmount());
        if (partyInvoicePojo.getInvoiceStatus().equals("Closed Orders"))
        {
            holder.tv_convert_to_sale.setVisibility(View.GONE);
        }
        else {
            holder.tv_convert_to_sale.setVisibility(View.VISIBLE);
        }
        holder.tv_convert_to_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String salesId = partyInvoicePojoArrayList.get(i).getSalesInvoiceId();
                String status = "Closed Orders";
                AsyncCallInvoiceList asyncCallInvoiceList = new AsyncCallInvoiceList();
                asyncCallInvoiceList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,salesId,status);

                ((NextDayOrderActivity)context).resetRecyclerview(context,i);

            }
        });
        if (partyInvoicePojo.getNickName()==null || partyInvoicePojo.getNickName().equals("0")){
            holder.tv_customer_name.setText(partyInvoicePojo.getPartyName());
        }
        else {
            holder.tv_customer_name.setText(partyInvoicePojo.getNickName());
        }

        df = new DecimalFormat("#0.00");
        holder.ll_item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ll_item_list_invoice.getVisibility() == View.GONE){
                    holder.ll_item_list_invoice.setVisibility(View.VISIBLE);
                    holder.iv_invoice_down_arrow.setVisibility(View.GONE);
                    holder.iv_invoice_up_arrow.setVisibility(View.VISIBLE);
                    strInvoiceNo = partyInvoicePojoArrayList.get(i).getInvoiceNo();
                    getItemDetails(strInvoiceNo,holder);
                    partyDetails.PartyId(strInvoiceNo);
                }
                else {
                    holder.ll_item_list_invoice.setVisibility(View.GONE);
                    holder.iv_invoice_down_arrow.setVisibility(View.VISIBLE);
                    holder.iv_invoice_up_arrow.setVisibility(View.GONE);
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSalesId = partyInvoicePojoArrayList.get(i).getSalesInvoiceId();
                strAmount = partyInvoicePojoArrayList.get(i).getInvoicePendingAmount();
                strInvoiceNo = partyInvoicePojoArrayList.get(i).getInvoiceNo();
                strPartyId = partyInvoicePojoArrayList.get(i).getPartyId();
                strTripId = partyInvoicePojoArrayList.get(i).getTripId();
                strRouteId = partyInvoicePojoArrayList.get(i).getRouteId();
                strPartyBalance = partyInvoicePojoArrayList.get(i).getPartyCurrentBalance();
                Intent intent = new Intent(context, SalesItemActivity.class);
                intent.putExtra("SALES_NO",strSalesId);
                intent.putExtra("PARTY_BALANCE",strPartyBalance);
                intent.putExtra("PREVIOUS_AMOUNT",strAmount);
                intent.putExtra("INVOICE_NO",strInvoiceNo);
                intent.putExtra("PARTY_ID",strPartyId);
                intent.putExtra("TRIP_ID",strTripId);
                intent.putExtra("ROUTE_ID",strRouteId);
                intent.putExtra("PARTY_NAME",partyInvoicePojoArrayList.get(i).getPartyName());
                isEditClicked = true;
                MainActivity.isPMOrder = false;
                context.startActivity(intent);

            }
        });


    }

    private class AsyncCallInvoiceList extends AsyncTask<String, Void, String> {

        String salesId,status;
        int position;

        @Override
        protected String doInBackground(String... params) {
            salesId = params[0];
            status = params[1];
            String SOAP_ACTION = "http://tempuri.org/UpdateSalesStatus";
            String METHOD_NAME = "UpdateSalesStatus";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("salesId", salesId);
                Request.addProperty("status", status);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL);
                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();
                String responseJSON = resultString.toString();
                if (responseJSON.equals(null)) {
                    Toast.makeText(context, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                } else {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Successfully changed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }

            return salesId;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("success")) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            } else if (result.equals("error")) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public int getItemCount() {
        return partyInvoicePojoArrayList.size();
    }

    public class ProductInvoiceHolder extends RecyclerView.ViewHolder {
        private TextView tv_invoice_no_adapter,tv_invoice_date_adapter,
                tv_invoice_total_adapter,tv_invoice_received_adapter,
                tv_invoice_balance_adapter,tv_total_subtotal,tv_total_qty,
                tv_customer_name,tv_convert_to_sale;
        private LinearLayout ll_item_details,ll_item_list_invoice;
        private RecyclerView rv_item_details;
        private ImageView iv_invoice_down_arrow,iv_invoice_up_arrow;
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
            tv_customer_name = itemView.findViewById(R.id.tv_customer_name);
            tv_convert_to_sale = itemView.findViewById(R.id.tv_convert_to_sale);
        }

    }

    public void getItemDetails(final String invNo, final ProductInvoiceHolder holder){
        final ArrayList<InvoiceItemDetails> invoiceItemDetailsArrayList = new ArrayList<>();

        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {

                String SOAP_ACTION = "http://tempuri.org/ListSalesInvoiceItem";
                String METHOD_NAME = "ListSalesInvoiceItem";
                String NAMESPACE = "http://tempuri.org/";
                String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
                try {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                    Request.addProperty("invNo", invNo);
                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;
                    soapEnvelope.setOutputSoapObject(Request);
                    HttpTransportSE transport = new HttpTransportSE(URL);
                    transport.call(SOAP_ACTION, soapEnvelope);
                    transport.debug = true;
                    resultString = (SoapPrimitive) soapEnvelope.getResponse();
                    String responseJSON = resultString.toString();
                    if (!TextUtils.isEmpty(responseJSON)) {
                        final JSONArray jarray = new JSONArray(responseJSON);
                        for (int i = 0; i < jarray.length(); i++) {
                            final JSONObject jsonObject = jarray.getJSONObject(i);
                            final String ProductName = jsonObject.getString("ProductName");
                            final String ItemPrice = jsonObject.getString("ItemPrice");
                            final String ItemQty = jsonObject.getString("ItemQty");
                            final String ItemSubTotalAmount = jsonObject.getString("ItemSubTotalAmount");
                            final String TaxAmount = jsonObject.getString("TaxAmount");
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    InvoiceItemDetails invoiceItemDetails = new InvoiceItemDetails();
                                    invoiceItemDetails.setProductName(ProductName);
                                    invoiceItemDetails.setTaxAmount(TaxAmount);
                                    invoiceItemDetails.setItemPrice(ItemPrice);
                                    invoiceItemDetails.setItemQty(ItemQty);
                                    invoiceItemDetails.setItemSubTotalAmount(ItemSubTotalAmount);
                                    invoiceItemDetails.setTotal(Double.parseDouble(invoiceItemDetails.getItemPrice()) * Double.parseDouble(invoiceItemDetails.getItemQty()) );
                                    invoiceItemDetailsArrayList.add(invoiceItemDetails);

                                    if (itemDetailsAdapter
                                            != null) {
                                        itemDetailsAdapter
                                                .notifyDataSetChanged();
                                    }

                                }
                            });
                        }

                    } else {
                        Toast.makeText(context, "Error From Server", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
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
                    tax = qty+Double.parseDouble(invoiceItemDetailsArrayList.get(j).getTaxAmount());
                    total = total + (price *qty) + tax;

                }
                holder.tv_total_qty.setText(df.format(qty));
                holder.tv_total_subtotal.setText(df.format(price));
            }
        };
        asyncTask.execute(invNo);
    }

}
