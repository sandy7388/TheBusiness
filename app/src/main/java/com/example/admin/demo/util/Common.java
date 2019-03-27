package com.example.admin.demo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.example.admin.demo.R;
import com.example.admin.demo.printer.UnicodeFormatter;

import java.nio.ByteBuffer;

public class Common {
    private static ProgressDialog progressDialog;

    public static void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showProgressDialog(Context ctx) {
        progressDialog = ProgressDialog.show(ctx, "", "Please Wait", true);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        progressDialog.setCancelable(true);
        progressDialog.show();
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
