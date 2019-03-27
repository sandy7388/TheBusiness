package com.example.admin.demo.adapter.expandable;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.demo.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        //listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        ImageView imageView = convertView.findViewById(R.id.icon_image);

        String teamName= getGroup(listPosition).toString();
        if (teamName.equals("Add Party")){
            imageView.setImageResource(R.drawable.addparty) ;
        }
        else if (teamName.equals("Items")){
            imageView.setImageResource(R.drawable.addparty) ;
        }
        else if (teamName.equals("Purchase Order")){
            imageView.setImageResource(R.drawable.purchaseorder) ;
        }
        else if (teamName.equals("Next Day Order")){
            imageView.setImageResource(R.drawable.purchaseorderreq) ;
        }
        else if (teamName.equals("Sales Order")){
            imageView.setImageResource(R.drawable.salesorder) ;
        }
        else if (teamName.equals("Manage Payment")){
            imageView.setImageResource(R.drawable.managepayment) ;
        }
        else if (teamName.equals("Add Tax")){
            imageView.setImageResource(R.drawable.addtax) ;
        }
        else if (teamName.equals("Expenses")){
            imageView.setImageResource(R.drawable.expense) ;
        }
        else if (teamName.equals("Reports")){
            imageView.setImageResource(R.drawable.reports) ;
        }
        else if (teamName.equals("Bank Accounts")){
            imageView.setImageResource(R.drawable.managepayment) ;
        }
        else if (teamName.equals("Manage User")){
            imageView.setImageResource(R.drawable.addparty) ;
        }
        else if (teamName.equals("Logout")){
            imageView.setImageResource(R.drawable.logout1) ;
        }
        else if (teamName.equals("Bulk Order")){
            imageView.setImageResource(R.drawable.purchaseorderreq) ;
        }
        else if (teamName.equals("PM Order")){
            imageView.setImageResource(R.drawable.purchaseorderreq) ;
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
