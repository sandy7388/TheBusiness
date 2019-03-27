package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.activity.ExpensesDetailsActivity;
import com.example.admin.demo.item.ItemExpenseList;
import com.example.admin.demo.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.MyViewHolder> {
    private static final int REQUEST_PHONE_CALL = 1;
    private List<ItemExpenseList> itemExpenseLists;
    private Context context;

    public ExpenseListAdapter(Context context, ArrayList<ItemExpenseList> itemExpenseLists) {
        this.context = context;
        this.itemExpenseLists = itemExpenseLists;
    }
    @Override
    public ExpenseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_expense_category, parent, false);
        return new ExpenseListAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final ExpenseListAdapter.MyViewHolder holder, final int position) {
        final ItemExpenseList itemExpenseList = itemExpenseLists.get(position);
        holder.card_view_expense_category.setText(itemExpenseList.getStr_expense_category());
        holder.card_view_amount.setText(itemExpenseList.getStr_amount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExpensesDetailsActivity.class);
                intent.putExtra("CATEGORY_NAME",itemExpenseList.getStr_expense_category());
                intent.putExtra("CATEGORY_ID",itemExpenseList.getStr_amount());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemExpenseLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView card_view_expense_category, card_view_amount;

        public MyViewHolder(View view) {
            super(view);

            card_view_expense_category = view.findViewById(R.id.card_view_expense_category);
            card_view_amount = view.findViewById(R.id.card_view_amount);
        }
    }
}