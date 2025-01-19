package com.theikdi.shwethike.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.theikdi.shwethike.sale.CreateSaleActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;
import java.util.Calendar;
import android.widget.DatePicker;
public class Theikdi {
    public static String numberFormat(int value){
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        String format = formatter.format((double) value);
        return format;
    }

    public static String currentDate() {
        Date date = new java.util.Date();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static void showDatePickerDialog(Context context, final TextView tvDate) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Update the TextView with the selected date
                        String formattedDay = String.format("%02d", dayOfMonth);
                        String formattedMonth = String.format("%02d", monthOfYear + 1);
                        tvDate.setText(year + "-" + (formattedMonth) + "-" + formattedDay);
                    }
                }, year, month, day);

        // Show the dialog
        datePickerDialog.show();
    }

    public static String getDueDate(String sale_date) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the sale_date string into a Date object
            Date date = dateFormat.parse(sale_date);

            // Create a Calendar object and set the time to the sale_date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Add 7 days to the date
            calendar.add(Calendar.DAY_OF_MONTH, 7);

            // Get the new date and format it back to a string
            Date dueDate = calendar.getTime();
            String due_date = dateFormat.format(dueDate);
            return due_date; // Return the due_date string

        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle parsing error if necessary
        }
    }
}




