package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.admin.demo.activity.PaymentActivity;
import com.example.admin.demo.interfaces.PartyDetails;
import com.example.admin.demo.model.InvoiceItemDetails;
import com.example.admin.demo.model.PartyInvoicePojo;
import com.example.admin.demo.model.PaymentListPojo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ProductInvoiceHolder> {

    private ArrayList<PaymentListPojo> paymentListPojoArrayList;
    private Context context;
    private DecimalFormat df;
    public static boolean isPaymentEdit = false;

    public PaymentListAdapter(ArrayList<PaymentListPojo> paymentListPojoArrayList, Context context) {
        this.paymentListPojoArrayList = paymentListPojoArrayList;
        this.context = context;
        //partyDetails = (PartyDetails)context;
    }

    @Override
    public ProductInvoiceHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_payment_list, viewGroup, false);
        return new ProductInvoiceHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductInvoiceHolder holder,final int i) {
        final PaymentListPojo paymentListPojo = paymentListPojoArrayList.get(i);
        df = new DecimalFormat("#0.00");
        holder.tv_payment_party_name.setText(paymentListPojo.getPartyName());
        holder.tv_payment_ref_no.setText(paymentListPojo.getReferenceNo());
        holder.tv_payment_balance.setText(paymentListPojo.getPartyCurrentBalance());
        holder.tv_paid_payment.setText(paymentListPojo.getPaidAmount());
        holder.tv_payment_date.setText(paymentListPojo.getPaymentDate());
        holder.tv_payment_type.setText(paymentListPojo.getPaymentMode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("PARTY_NAME",paymentListPojoArrayList.get(i).getPartyName());
                intent.putExtra("BALANCE",paymentListPojoArrayList.get(i).getPartyCurrentBalance());
                intent.putExtra("PAYMENT_ID",paymentListPojoArrayList.get(i).getPaymentId());
                isPaymentEdit = true;
                context.startActivity(intent);
            }
        });

   }

    @Override
    public int getItemCount() {
        return paymentListPojoArrayList.size();
    }

    public class ProductInvoiceHolder extends RecyclerView.ViewHolder {
        private TextView tv_payment_party_name,tv_payment_ref_no,
                tv_payment_balance,tv_paid_payment,
                tv_payment_date,tv_payment_type;
        public ProductInvoiceHolder( View itemView) {
            super(itemView);
            tv_payment_party_name = itemView.findViewById(R.id.tv_payment_party_name);
            tv_payment_ref_no = itemView.findViewById(R.id.tv_payment_ref_no);
            tv_payment_balance = itemView.findViewById(R.id.tv_payment_balance);
            tv_paid_payment = itemView.findViewById(R.id.tv_paid_payment);
            tv_payment_date = itemView.findViewById(R.id.tv_payment_date);
            tv_payment_type = itemView.findViewById(R.id.tv_payment_type);

        }

    }

    public void updateList(List<PaymentListPojo> list){
        paymentListPojoArrayList = new ArrayList<>();
        paymentListPojoArrayList.addAll(list);
        notifyDataSetChanged();
    }


}
