package com.compscitutorials.basigarcia.navigationdrawervideotutorial;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.R.attr.key;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAddFragment extends Fragment implements View.OnClickListener {


    public TaskAddFragment() {
        // Required empty public constructor
    }

    View view;
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
    private static long randomNumber = 999999;
    private DatePickerDialog.OnDateSetListener mDateListener;
    static final int PICKER_ID = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_task, container, false);

        submitButton = (Button) view.findViewById(R.id.submitbutton);
        submitButton.setOnClickListener(this);
        //Firebase Setup
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUser.getUid());
        submitButton.setOnClickListener(this);
        //Text Fields Setup
        name = (EditText) view.findViewById(R.id.textName);
        duration = (EditText) view.findViewById(R.id.textDuration);
        address = (EditText) view.findViewById(R.id.streetLocation);
        city = (EditText) view.findViewById(R.id.cityLocation);
        //Calendar
        minute = (EditText) view.findViewById(R.id.minuteInput);
        hour = (EditText) view.findViewById(R.id.hourInput);
        year = (EditText) view.findViewById(R.id.yearInput);
        day = (EditText) view.findViewById(R.id.dayInput);
        month = (EditText) view.findViewById(R.id.monthInput);
        //Calendar Button
        mDateListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int yr, int mnth, int DOM) {
                month.setText(Integer.toString(mnth+1));
                year.setText(Integer.toString(yr));
                day.setText(Integer.toString(DOM));
            }
        };
        calendarButton = (Button) view.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        timeButton = (Button) view.findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int currenthour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int currentminute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
        return view;
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case(R.id.submitbutton):
                //Create the Child

                if(day.getText() == null || day.getText() == null || day.getText() == null || day.getText() == null || day.getText() == null){
                    Toast.makeText(view.getContext(),"Please Fill in All the Elemnts",Toast.LENGTH_SHORT);
                }

                int day_int = Integer.parseInt(day.getText().toString());
                int minute_int = Integer.parseInt(minute.getText().toString());
                int hour_int = Integer.parseInt(hour.getText().toString());
                int year_int = Integer.parseInt(year.getText().toString());
                int month_int = Integer.parseInt(month.getText().toString());


                Cal dueDate = new Cal(year_int,month_int,day_int,hour_int,minute_int);

                String key = mDatabase.push().getKey();
                final TaskJSON task1 = new TaskJSON (name.getText().toString().trim(),duration.getText().toString().trim(),address.getText().toString().trim(),city.getText().toString().trim(),dueDate,randomNumber+"",key);
                mDatabase.child(key).setValue(task1).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getView().getContext(),"Stored",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getView().getContext(),"Failed to Store: Check internet",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        FirebaseTaskListAdapter.mTasks.add(dataSnapshot.getValue(TaskJSON.class));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                randomNumber--;
                //Assign Value to Child
                break;
        }
    }


}
