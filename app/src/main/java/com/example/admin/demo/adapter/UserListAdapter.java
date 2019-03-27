package com.example.admin.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.demo.R;
import com.example.admin.demo.activity.AddUserActivity;
import com.example.admin.demo.model.UserListPojo;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<UserListPojo> userListPojoList;
    private Context context;
    public static boolean isUserEdit = false;

    public UserListAdapter(List<UserListPojo> userListPojoList, Context context) {
        this.userListPojoList = userListPojoList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        UserListPojo userListPojo = userListPojoList.get(position);
        holder.itemName.setText(userListPojo.getUserName());
        holder.tv_amount.setText(userListPojo.getUserStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddUserActivity.class);
                isUserEdit = true;
                intent.putExtra("USER_ID",userListPojoList.get(position).getUserLoginId());
                context.startActivity(intent);
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName,tv_amount;
        public MyViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.tv_item_menu);
            tv_amount = view.findViewById(R.id.btn_edit_party);
        }
    }


    @Override
    public int getItemCount() {
        return userListPojoList.size();
    }


    public interface onUserClickListener{
        void onClickUser();
    }
}