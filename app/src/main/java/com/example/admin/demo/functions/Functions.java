package com.example.admin.demo.functions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {
    DatePickerDialog datePickerDialog;
    static final Calendar myCalendar = Calendar.getInstance();
    public static void setDatePicker(Context context, final EditText et) {
       // mm / dd / yyyy
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();

                // mm/dd/yyyy
                newDate.set(year, monthOfYear, dayOfMonth);
                //  newDate.set(monthOfYear, dayOfMonth, year);
                et.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public static void currentDate(Context context, final EditText editText){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }

        };
    }

    private static void updateLabel(EditText editText) {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @SuppressLint("RestrictedApi")
    public static void setToolBar(final Context context, Toolbar toolbar, String title, boolean back_button) {
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.setSupportActionBar(toolbar);
        activity.setTitle(title);
        if (back_button) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) context).onBackPressed();
                }
            });
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm a");
        Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
        String s_time = dateFormat.format(currentCalender.getTime());
        return s_time;
    }

    public static void setAdapterwithposition(Context context, String[] list, Spinner spinner, String value) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, list);
        spinner.setAdapter(adapter);
        for (int i = 0; i < list.length; i++) {
            String valuez = adapter.getItem(i).toString();
            if (TextUtils.equals(value, valuez)) {
                int spinnerPosition = adapter.getPosition(valuez);
                spinner.setSelection(spinnerPosition, false);
            }

        }


    }


    public static Double calculateDiscount(String price_tocaluclate, String discount) {
        double price = Double.parseDouble(price_tocaluclate);
        double ePer = Double.parseDouble(discount);
        // percent
        double per = (price / 100.0f) * ePer;
        return per;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void setDatatoRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context, boolean value) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        if (value) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
            recyclerView.setLayoutManager(layoutManager);
        }

        recyclerView.setAdapter(adapter);

    }
/*

    public static void info(final Context context, String str_info) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.dialog_info, null);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setView(v);
        alertDialog.setCancelable(true);
        alertDialog.show();
        Button btn_close = (Button) v.findViewById(R.id.btn_close_dialog);
        TextView tv_info = (TextView) v.findViewById(R.id.tv_info);
        tv_info.setText(str_info);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
*/

    public static void setTimePicker(Context context, final EditText et) {
        final TimePickerDialog timePickerDialog;
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                int mHour = selectedHour;
                int mMin = selectedMinute;

                String AM_PM;
                if (selectedHour < 12) {
                    AM_PM = "AM";

                } else {
                    AM_PM = "PM";
                    mHour = mHour - 12;
                }
                et.setText(mHour + ":" + mMin + " " + AM_PM);

            }
        }, hour, minute, false);

        timePickerDialog.show();
    }

    public static void setAdapter(Context context, String[] list, Spinner spinner) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, list);
        spinner.setAdapter(adapter);
    }

    public static void detailsDialog(final Context context, String title, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "  CLOSE  ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

    public static void startNewActivity(Context context, Class<Activity> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    /*public static void getJsonArrayFunction(JSONObject response, List bList, String str1, String str2, Class<?> context) {
        try {
            Iterator<String> keys = response.keys();
            while (keys.hasNext()) {
                String str_Name = keys.next();
//String value = response.optString(str_Name);

                JSONArray jsonArray = response.getJSONArray(str_Name);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String time = jsonObject.getString(str1);
                    String created_date = jsonObject.getString(str2);

                    bList.add(new BookingItem(str_Name, created_date));
                }

            }
        } catch (Exception e) {

        }

    }

    public static void intentArraylist(Context context, Class<?> activity, ArrayList arrayList) {
        Intent intent = new Intent(context, activity);
        intent.putStringArrayListExtra("stock_list", arrayList);
        context.startActivity(intent);
    }
*/
    public static void exitApp(final Context context, String str_msg) {
        new AlertDialog.Builder(context)
                .setMessage(str_msg)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((Activity) context).finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void setStatusBarColor(Context context, View statusBar, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = ((AppCompatActivity) context).getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusBar.setBackgroundColor(color);
        }
    }

    public void CompareDates(Context context) throws Exception {
        String dateString = "10-11-2000";
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 21);
        Toast.makeText(context, "User is not complete 21 years", Toast.LENGTH_SHORT).show();
        //Toast.makeText(, dateString + "" + calendar.getTime().after(date), Toast.LENGTH_LONG).show();
        System.out.printf("Date %s is older than 18? %s", dateString, calendar.getTime().after(date));
    }


    public static CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new BackgroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }




}
