package com.example.admin.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.item.ItemBankList;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;


public class BankAccountListAdapter extends RecyclerView.Adapter<BankAccountListAdapter.MyViewHolder> {
    private static final int REQUEST_PHONE_CALL = 1;
    private List<ItemBankList> itemAccounts;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView card_view_bankAccount_balance, card_view_bankAccount_name;

        public MyViewHolder(View view) {
            super(view);

            card_view_bankAccount_balance = view.findViewById(R.id.card_view_bankAccount_balance);
            card_view_bankAccount_name = view.findViewById(R.id.card_view_bankAccount_name);


        }
    }


    public BankAccountListAdapter(Context context, ArrayList<ItemBankList> itemAccounts) {
        this.context = context;
        this.itemAccounts = itemAccounts;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bank_accountlistcardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ItemBankList itemAccount = itemAccounts.get(position);
        itemAccounts.size();
        holder.card_view_bankAccount_name.setText(itemAccount.getBankName());
        holder.card_view_bankAccount_balance.setText(itemAccount.getCurrentBalance());

    }


    @Override
    public int getItemCount() {
        return itemAccounts.size();
    }


}