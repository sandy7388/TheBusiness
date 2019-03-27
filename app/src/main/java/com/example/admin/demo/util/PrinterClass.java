package com.example.admin.demo.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.admin.demo.R;
import com.example.admin.demo.printer.ScanActivity;
import com.example.admin.demo.printer.UnicodeFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;

import static io.fabric.sdk.android.Fabric.TAG;

public class PrinterClass {

    private BluetoothSocket mBluetoothSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private Context context;

    public PrinterClass(Context context) {
        this.context = context;
    }

    public void printData(final String name, final String no,final String date){
        Thread t = new Thread() {
            public void run() {
                try {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
                    if (mPairedDevices.size() > 0) {
                        OutputStream os = mBluetoothSocket
                                .getOutputStream();
                        String BILL = "";

                        BILL = "          Party Name  : "+name +   "\n"
                                + "       Invoice N.. : "+ no+     "\n " +
                                "         Invoice Date: "+ date     +" \n" +
                                "                  XXXXX YYYYYY      \n" +
                                "                   MMM 590019091      \n";
                        BILL = BILL
                                + "-----------------------------------------------\n";


                        BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Total");
                        BILL = BILL + "\n";
                        BILL = BILL
                                + "-----------------------------------------------";
                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-001", "15", "10", "50.00");
                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
                        BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");

                        BILL = BILL
                                + "\n-----------------------------------------------";
                        BILL = BILL + "\n\n ";

                        BILL = BILL + "                   Total Qty:" + "      " + "95" + "\n";
                        BILL = BILL + "                   Total Value:" + "     " + "800.00" + "\n";

                        BILL = BILL
                                + "-----------------------------------------------\n";
                        BILL = BILL + "\n\n ";
                        os.write(BILL.getBytes());
                        //This is printer specific code you can comment ==== > Start

                        // Setting height
                        int gs = 29;
                        os.write(intToByteArray(gs));
                        int h = 104;
                        os.write(intToByteArray(h));
                        int n = 162;
                        os.write(intToByteArray(n));

                        // Setting Width
                        int gs_width = 29;
                        os.write(intToByteArray(gs_width));
                        int w = 119;
                        os.write(intToByteArray(w));
                        int n_width = 2;
                        os.write(intToByteArray(n_width));
                    } else {
                        String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
                        Toast.makeText(context, mNoDevices, Toast.LENGTH_SHORT).show();
                        //mPairedDevicesArrayAdapter.add(mNoDevices);
                    }



                } catch (Exception e) {
                    Log.e("ScanActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }


}
