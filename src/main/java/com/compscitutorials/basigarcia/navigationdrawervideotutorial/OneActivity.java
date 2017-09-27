package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import java.util.Calendar;

public class OneActivity extends AppCompatActivity implements View.OnClickListener{

    Button submitButton;
    EditText name;
    EditText duration;
    EditText address;
    EditText city;
    EditText minute;
    EditText hour;
    EditText year;
    EditText day;
    EditText month;
    Button calendarButton;
    Button timeButton;
    DatabaseReference mDatabase;
    FirebaseUser mUser;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private String currentTaskKey;
    static final int PICKER_ID = 1;
    TaskJSON current;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);

        setContentView(R.layout.activity_one);
        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT); //set below the setContentview
        //intent Info
        int startingPosition = getIntent().getIntExtra("position", 0);
        currentTaskKey = FirebaseTaskListAdapter.mTasks.get(startingPosition).firebaseaddress;
        current = FirebaseTaskListAdapter.mTasks.get(startingPosition);
        submitButton = (Button) findViewById(R.id.onesubmitbutton);
        submitButton.setOnClickListener(this);
        //Firebase Setup
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUser.getUid());
        submitButton.setOnClickListener(this);
        //Text Fields Setup
        name = (EditText) findViewById(R.id.onetextName);
        duration = (EditText) findViewById(R.id.onetextDuration);
        address = (EditText) findViewById(R.id.onestreetLocation);
        city = (EditText) findViewById(R.id.onecityLocation);
        //Calendar
        minute = (EditText) findViewById(R.id.oneminuteInput);
        hour = (EditText) findViewById(R.id.onehourInput);
        year = (EditText) findViewById(R.id.oneyearInput);
        day = (EditText) findViewById(R.id.onedayInput);
        month = (EditText) findViewById(R.id.onemonthInput);
        //Startup
        name.setText(current.getName());
        duration.setText(current.getDuration());
        address.setText(current.getAddress());
        city.setText(current.getCity());
        minute.setText(Integer.toString(current.getDueDate().getMinute_int()));
        hour.setText(Integer.toString(current.getDueDate().getHour_int()));
        year.setText(Integer.toString(current.getDueDate().getYear_int()));
        day.setText(Integer.toString(current.getDueDate().getDay_int()));
        month.setText(Integer.toString(current.getDueDate().getMonth_int()));
        //Calendar Button
        mDateListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int yr, int mnth, int DOM) {
                month.setText(Integer.toString(mnth+1));
                year.setText(Integer.toString(yr));
                day.setText(Integer.toString(DOM));
            }
        };
        calendarButton = (Button) findViewById(R.id.onecalendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OneActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        timeButton = (Button) findViewById(R.id.onetimeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int currenthour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int currentminute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(OneActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour.setText(Integer.toString(selectedHour));
                        minute.setText(Integer.toString(selectedMinute));
                    }
                }, currenthour, currentminute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }


    @Override
    public void onClick(final View v) {
        switch(v.getId()){
            case(R.id.onesubmitbutton):
                //Create the Child

                if(day.getText() == null || day.getText() == null || day.getText() == null || day.getText() == null || day.getText() == null){
                    Toast.makeText(v.getContext(),"Please Fill in All the Elemnts",Toast.LENGTH_SHORT);
                }

                int day_int = Integer.parseInt(day.getText().toString());
                int minute_int = Integer.parseInt(minute.getText().toString());
                int hour_int = Integer.parseInt(hour.getText().toString());
                int year_int = Integer.parseInt(year.getText().toString());
                int month_int = Integer.parseInt(month.getText().toString());


                Cal dueDate = new Cal(year_int,month_int,day_int,hour_int,minute_int);


                current.setDuration(duration.getText().toString().trim());
                current.setName(name.getText().toString().trim());
                current.setAddress(address.getText().toString().trim());
                current.setCity(city.getText().toString().trim());
                current.setDueDate(dueDate);


                mDatabase.child(currentTaskKey).setValue(current).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(OneActivity.this,"Stored",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(OneActivity.this,"Failed to Store: Check internet",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //Assign Value to Child

                break;
        }
    }
}

