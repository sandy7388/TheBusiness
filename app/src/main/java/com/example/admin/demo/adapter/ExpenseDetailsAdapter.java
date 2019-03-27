package com.example.admin.demo.adapter;

import android.content.Context;
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
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.ExpenseDetailsListPojo;
import com.example.admin.demo.model.ExpenseItemDetailsPojo;
import com.example.admin.demo.model.ExpenseItems;
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

public class ExpenseDetailsAdapter extends RecyclerView.Adapter<ExpenseDetailsAdapter.ProductInvoiceHolder> {

    private ArrayList<ExpenseItems> expenseItemsArrayList;
    private Context context;
    private PartyDetails partyDetails;
    private SoapPrimitive resultString;
    private ExpenseItemDetailsAdapter itemDetailsAdapter;
    private DecimalFormat df;
    double subtotal = 0,total_qty = 0;
    private String strInvoiceNo;
    private ArrayList<ExpenseDetailsListPojo> invoiceItemDetailsArrayList;

    public ExpenseDetailsAdapter(ArrayList<ExpenseItems> expenseItemsArrayList, Context context) {
        this.expenseItemsArrayList = expenseItemsArrayList;
        this.context = context;
        //partyDetails = (PartyDetails)context;
    }

    @Override
    public ProductInvoiceHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_expense_details, viewGroup, false);
        return new ProductInvoiceHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductInvoiceHolder holder,final int i) {
        final ExpenseItems expenseItems = expenseItemsArrayList.get(i);
        //holder.tv_expense_subtotal.setText(expenseItems.getInvoiceNo());
        //holder.tv_expense_qty_total.setText(expenseItems.getInvoiceDate());
        holder.tv_expense_total.setText(expenseItems.getTotalAmount());
        holder.tv_expense_date.setText(expenseItems.getExDate());
        holder.tv_expense_category.setText(expenseItems.getExpenseCategory());
        df = new DecimalFormat("#0.00");
        holder.ll_expense_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ExpenseItems expenseItems = expenseItemsArrayList.get(i);
                if (holder.ll_expense_item_list.getVisibility() == View.GONE){
                    holder.ll_expense_item_list.setVisibility(View.VISIBLE);
                    holder.iv_expense_down_arrow.setVisibility(View.GONE);
                    holder.iv_expense_up_arrow.setVisibility(View.VISIBLE);
                    strInvoiceNo = expenseItemsArrayList.get(i).getExpNumber();
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//                    holder.rv_expense_details.setLayoutManager(layoutManager);
//                    holder.rv_expense_details.setHasFixedSize(true);
//                    itemDetailsAdapter = new ExpenseItemDetailsAdapter(getExpenseDetails(strInvoiceNo),context);
//                    holder.rv_expense_details.setAdapter(itemDetailsAdapter);
                    //partyDetails.PartyId(strInvoiceNo);
                    getExpenseList(strInvoiceNo,holder);
                }
                else {
                    holder.ll_expense_item_list.setVisibility(View.GONE);
                    holder.iv_expense_down_arrow.setVisibility(View.VISIBLE);
                    holder.iv_expense_up_arrow.setVisibility(View.GONE);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return expenseItemsArrayList.size();
    }

    public class ProductInvoiceHolder extends RecyclerView.ViewHolder {
        private TextView tv_expense_subtotal,tv_expense_qty_total,
                tv_expense_total,tv_expense_date,tv_expense_category;
        private LinearLayout ll_expense_details,ll_expense_item_list;
        private RecyclerView rv_expense_details;
        private ImageView iv_expense_down_arrow,iv_expense_up_arrow;
        public ProductInvoiceHolder( View itemView) {
            super(itemView);
            rv_expense_details = itemView.findViewById(R.id.rv_expense_details);
            ll_expense_details = itemView.findViewById(R.id.ll_expense_details);
            tv_expense_subtotal = itemView.findViewById(R.id.tv_expense_subtotal);
            tv_expense_qty_total = itemView.findViewById(R.id.tv_expense_qty_total);
            tv_expense_total = itemView.findViewById(R.id.tv_expense_total);
            tv_expense_date = itemView.findViewById(R.id.tv_expense_date);
            iv_expense_down_arrow = itemView.findViewById(R.id.iv_expense_down_arrow);
            iv_expense_up_arrow = itemView.findViewById(R.id.iv_expense_up_arrow);
            ll_expense_item_list = itemView.findViewById(R.id.ll_expense_item_list);
            tv_expense_category = itemView.findViewById(R.id.tv_expense_category);

        }

    }


    public ArrayList<ExpenseDetailsListPojo> getExpenseDetails(String expNo) {
        invoiceItemDetailsArrayList = new ArrayList<>();
        String SOAP_ACTION = "http://tempuri.org/ListAllExpensesItemByExpNo";
        String METHOD_NAME = "ListAllExpensesItemByExpNo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("expNo", expNo);
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
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    final String UnitPrice = jsonObject.getString("UnitPrice");
                    final String ItemName = jsonObject.getString("ItemName");
                    final String Quantity = jsonObject.getString("Quantity");
                    final String TotalitemAmount = jsonObject.getString("TotalitemAmount");

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ExpenseDetailsListPojo expenseDetailsListPojo = new ExpenseDetailsListPojo();
                            expenseDetailsListPojo.setItemName(ItemName);
                            expenseDetailsListPojo.setUnitPrice(UnitPrice);
                            expenseDetailsListPojo.setQuantity(Quantity);
                            expenseDetailsListPojo.setTotalitemAmount(TotalitemAmount);
                            invoiceItemDetailsArrayList.add(expenseDetailsListPojo);
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
        return invoiceItemDetailsArrayList;

    }


    public void getExpenseList(final String expNo, final ProductInvoiceHolder holder){
        final ArrayList<ExpenseDetailsListPojo> invoiceItemDetailsArrayList = new ArrayList<>();
        AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String SOAP_ACTION = "http://tempuri.org/ListAllExpensesItemByExpNo";
                String METHOD_NAME = "ListAllExpensesItemByExpNo";
                String NAMESPACE = "http://tempuri.org/";
                String URL = "http://thebusiness.globaltechpie.co.in/cOrder.asmx";
                try {
                    SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                    Request.addProperty("expNo", expNo);
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
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            final String UnitPrice = jsonObject.getString("UnitPrice");
                            final String ItemName = jsonObject.getString("ItemName");
                            final String Quantity = jsonObject.getString("Quantity");
                            final String TotalitemAmount = jsonObject.getString("TotalitemAmount");

                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ExpenseDetailsListPojo expenseDetailsListPojo = new ExpenseDetailsListPojo();
                                    expenseDetailsListPojo.setItemName(ItemName);
                                    expenseDetailsListPojo.setUnitPrice(UnitPrice);
                                    expenseDetailsListPojo.setQuantity(Quantity);
                                    expenseDetailsListPojo.setTotalitemAmount(TotalitemAmount);
                                    invoiceItemDetailsArrayList.add(expenseDetailsListPojo);
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
                super.onPostExecute(s);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        holder.rv_expense_details.setLayoutManager(linearLayout);
                        holder.rv_expense_details.setHasFixedSize(true);
                        itemDetailsAdapter = new ExpenseItemDetailsAdapter(invoiceItemDetailsArrayList,context);
                        holder.rv_expense_details.setAdapter(itemDetailsAdapter);
                    }
                });

            }
        };

        asyncTask.execute(expNo);
    }
}
