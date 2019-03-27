package com.example.admin.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.functions.Functions;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar_report_list;
    private TextView tv_app_name_nav_bar;
    private String[] reportArray = {"Vehicle Load Report","Payment Collection Report",
            "Next Day Order Report","Sales Report"};
    private ArrayAdapter<String> adapter;
    private LinearLayout ll_vehicle_report,ll_payment_report,ll_nxt_order_report,ll_sale_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar_report_list = findViewById(R.id.toolbar_report_list);
        View view = toolbar_report_list.getRootView();
        tv_app_name_nav_bar = view.findViewById(R.id.app_name_nav_bar);
        tv_app_name_nav_bar.setText("Reports");
        ll_vehicle_report = findViewById(R.id.ll_vehicle_report);
        ll_payment_report = findViewById(R.id.ll_payment_report);
        ll_nxt_order_report = findViewById(R.id.ll_nxt_order_report);
        ll_sale_report = findViewById(R.id.ll_sale_report);
        ll_vehicle_report.setOnClickListener(this);
        ll_payment_report.setOnClickListener(this);
        ll_nxt_order_report.setOnClickListener(this);
        ll_sale_report.setOnClickListener(this);
        Functions.setToolBar(ReportActivity.this, toolbar_report_list,
                "Add Item", true);

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, reportArray);

        ListView listView = (ListView) findViewById(R.id.list_report);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
                //Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();

                if (value.equals("Vehicle Load Report")){
                    startActivity(new Intent(ReportActivity.this,VehicleLoadActivity.class));
                }
                else if (value.equals("Payment Collection Report")){
                    startActivity(new Intent(ReportActivity.this,CollectionReportActivity.class));
                }
                else if (value.equals("Next Day Order Report")){
                    startActivity(new Intent(ReportActivity.this,NextDayOrderReport.class));
                }
                else if (value.equals("Sales Report")){
                    startActivity(new Intent(ReportActivity.this,SalesReportActivity.class));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_vehicle_report:
                startActivity(new Intent(ReportActivity.this,VehicleLoadActivity.class));
                break;
            case R.id.ll_payment_report:
                startActivity(new Intent(ReportActivity.this,CollectionReportActivity.class));
                break;
            case R.id.ll_nxt_order_report:
                startActivity(new Intent(ReportActivity.this,NextDayOrderReport.class));
                break;
            case R.id.ll_sale_report:
                startActivity(new Intent(ReportActivity.this,SalesReportActivity.class));
                break;
        }
    }
}
