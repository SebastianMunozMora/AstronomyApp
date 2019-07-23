package com.example.astronomyapp;


import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

import static java.time.LocalDate.now;


public class MainActivity extends AppCompatActivity {
    TextView titleView, descriptionView;
    Button calendarButton;
    DatePickerDialog datePickerDialog;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Elements
        titleView = findViewById(R.id.textTitle);
        calendarButton = findViewById(R.id.textDate);
        descriptionView = findViewById(R.id.textDesc);


        //Date Picker
        final Date currentDate = Calendar.getInstance().getTime();
        //currentCalendar = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = dateFormat.format(currentDate);
        Log.d("Current_time => ","" + formattedDate);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.set(Calendar.YEAR,year);
                currentCalendar.set(Calendar.MONTH,month);
                currentCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                Date calendarDate = currentCalendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                formattedDate = dateFormat.format(calendarDate);
                Log.d("Current_time => ","" + formattedDate);
                calendarButton.setText(formattedDate);
            }
        },currentYear,currentMonth,currentDay);




        //Current Date
        /*Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = dateFormat.format(date);
        Log.d("Current time => ","" + formattedDate);
        getCurrentDay();
        getCurrentMonth();
        getCurrentYear();

        //set text view date
        dateView.setText(formattedDate);*/




    }

    public String getCurrentDay(){
        String currentDay;
        currentDay = formattedDate.substring(0,2);
        Log.d("Current Day =>", currentDay);
        return currentDay;
    }

    public String getCurrentMonth(){
        String currentMonth;
        currentMonth = formattedDate.substring(3,5);
        Log.d("Current Month =>", currentMonth);
        return currentMonth;
    }

    public String getCurrentYear(){
        String currentYear;
        currentYear = formattedDate.substring(6);
        Log.d("Current Year =>", currentYear);
        return currentYear;
    }

    public void showCalendar(View view) {
        datePickerDialog.show();
    }
}
