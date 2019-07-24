package com.example.astronomyapp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;

import static com.example.astronomyapp.BuildConfig.API_KEY;


public class MainActivity extends AppCompatActivity {
    TextView titleView, descriptionView;
    ImageView imageView;
    Button calendarButton;
    DatePickerDialog datePickerDialog;
    String calendarDateFormatted, currentDateFormatted;
    String minDate = "1995-06-16", requestDate;
    Calendar minDateCalendar, todayCalendar;
    Date calendarDate;
    Calendar currentCalendar;
    int currentDayOfYear;
    String ApiKey = API_KEY;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View Elements
        titleView = findViewById(R.id.textTitle);
        calendarButton = findViewById(R.id.textDate);
        descriptionView = findViewById(R.id.textDesc);
        imageView = findViewById(R.id.imageView);

        //Current Date
        Date currentDate = Calendar.getInstance().getTime();
        todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY,0);
        int currentDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = todayCalendar.get(Calendar.MONTH);
        int currentYear = todayCalendar.get(Calendar.YEAR);
        currentDayOfYear = todayCalendar.get(Calendar.DAY_OF_YEAR);

        currentDateFormatted = formatDate(currentDate);

        //minDate
        minDateCalendar = Calendar.getInstance();
        minDateCalendar.set(Calendar.YEAR,1995);
        minDateCalendar.set(Calendar.MONTH,5);
        minDateCalendar.set(Calendar.DAY_OF_MONTH,16);
        Date minDateDate = minDateCalendar.getTime();
        //request current date
        currentCalendar = Calendar.getInstance();
        requestDate = currentDateFormatted;
        makeRequest(currentDateFormatted);

        //Date Picker
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentCalendar = Calendar.getInstance();
                currentCalendar.set(Calendar.YEAR,year);
                currentCalendar.set(Calendar.MONTH,month);
                currentCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                currentCalendar.set(Calendar.HOUR_OF_DAY,0);
                calendarDate = currentCalendar.getTime();
                calendarDateFormatted = formatDate(calendarDate);
                requestDate = calendarDateFormatted;
                makeRequest(calendarDateFormatted);

            }
        },currentYear,currentMonth,currentDay);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(minDateDate.getTime());
        //Imageview swipe
        imageView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                subsDay(currentCalendar);

            }
            public void onSwipeLeft() {
                //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                addDay(currentCalendar);

            }
            public void onSwipeBottom() {
                //Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void addDay(Calendar calendar){
        if (calendar.getTimeInMillis() < todayCalendar.getTimeInMillis()){

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        Date date = calendar.getTime();
        //Log.d("test:", formatDate(date));
        makeRequest(formatDate(date));
    }
    public void subsDay(Calendar calendar){
        //Log.d("cur tod", String.valueOf(calendar.getTimeInMillis()) +" "+ String.valueOf(minDateCalendar.getTimeInMillis()));
        if (calendar.getTimeInMillis() > minDateCalendar.getTimeInMillis()){
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        Date date = calendar.getTime();
        //Log.d("test:", formatDate(date));
        makeRequest(formatDate(date));
    }
    public void makeRequest(String date){
        updateGUI(date);
        String url = "https://api.nasa.gov/planetary/apod?date=" + date + "&api_key=" + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Picasso.get().load(response.getString("url")).into(imageView);
                            titleView.setText(response.getString("title"));
                            descriptionView.setText(response.getString("explanation"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d("Request: ", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this,"Wrong Date",Toast.LENGTH_SHORT  ).show();
                        //Log.d("Request: ", error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void updateGUI(String buttonDate){
        calendarButton.setText(buttonDate);
    }

    public String formatDate (Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate;
        formattedDate = dateFormat.format(date);
        //Log.d("Current_time => ","" + formattedDate);
        return formattedDate;

    }


    public void showCalendar(View view) {
        datePickerDialog.show();
    }
}
