package com.example.fred.uscfit;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText mEmailText;
    private TextInputEditText mActivityName;

    private String mEmail;

    private TimePicker mTimePicker;

    private TextView mStartTime;
    private Button mStartTimeBtn;

    private TextView mEndTime;
    private Button mEndTimeBtn;

    private Button mTimerCancel;
    private Button mTimerSet;
    private LinearLayout mTimerLayout;
    private DatePicker mDatePicker;

    private TextView mDate;
    private Button mDatePickerBtn;
    private LinearLayout mDateLayout;
    private Button mDateCancel;
    private Button mDateSet;
    private Button mSubmit;

    private static int year       = 0;
    private static int month      = 0; // Jan = 0, dec = 11
    private static int dayOfMonth = 0;

    private static int hourOfDay  = 0; // 24 hour clock
    private static int minute     = 0;

    private String activityName = "";

    private static int timeType = 0; // 1 means end time, 0 means start time
    private Calendar activityStartDate;
    private Calendar activityEndDate;

    private DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbController = new DBController();

        activityStartDate = Calendar.getInstance();
        activityStartDate.clear();

        activityEndDate = Calendar.getInstance();
        activityEndDate.clear();

        mEmailText = findViewById(R.id.email);

        mActivityName = findViewById(R.id.activity_name);

        // format input and update activity name
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String editableString = s.toString();
                if (editableString.contains(" ")) {
                    final String spaceFreeString = editableString.replaceAll(" ", "");
                    mActivityName.setText(spaceFreeString);
                    mActivityName.setSelection(spaceFreeString.length());
                }
            }
        };

        mActivityName.addTextChangedListener(watcher);

        mStartTime = findViewById(R.id.activity_start_time);

        mEmail = getIntent().getStringExtra("email");
        mEmailText.setText(mEmail);

        mEndTime = findViewById(R.id.activity_end_time);

        // set time picker layout

        mTimePicker = findViewById(R.id.time_picker);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hr = String.valueOf(hourOfDay);
                String min = String.valueOf(minute);
                String currTime = hr + ":" + min;
                if(timeType == 0){ mStartTime.setText(currTime);}
                else if(timeType == 1) {mEndTime.setText(currTime);}

                AddActivity.minute = minute;
                AddActivity.hourOfDay = hourOfDay;
            }
        });

        mTimerLayout = findViewById(R.id.timer_layout);

        mStartTimeBtn = findViewById(R.id.show_time_picker);
        mStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 0;
                mTimerLayout.setVisibility(View.VISIBLE);
            }
        });

        mEndTimeBtn = findViewById(R.id.show_time_picker_end);
        mEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 1;
                mTimerLayout.setVisibility(View.VISIBLE);
            }
        });

        mTimerCancel = findViewById(R.id.timer_cancel_btn);
        mTimerCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeType == 0){mStartTime.setText("");}
                else if(timeType == 1) {mEndTime.setText("");}
                mStartTime.setText("");
                mTimerLayout.setVisibility(View.GONE);
            }
        });
        mTimerSet = findViewById(R.id.timer_confirm_btn);
        mTimerSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerLayout.setVisibility(View.GONE);
                if(timeType == 0) {
                    activityStartDate.set(Calendar.HOUR, AddActivity.hourOfDay);
                    activityStartDate.set(Calendar.MINUTE, AddActivity.minute);

                }
                else if(timeType == 1) {
                    activityEndDate.set(Calendar.HOUR, AddActivity.hourOfDay);
                    activityEndDate.set(Calendar.MINUTE, AddActivity.minute);
                }
            }
        });



        // set date picker layout

        mDateLayout = findViewById(R.id.date_layout);
        mDatePickerBtn = findViewById(R.id.show_date_picker);
        mDate = findViewById(R.id.activity_date);
        mDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.VISIBLE);
            }
        });
        mDatePicker = findViewById(R.id.date_picker);
        mDatePicker.getYear();
        mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                StringBuilder sb = new StringBuilder();
                sb.append(monthOfYear).append("/").append(dayOfMonth).append("/").append(year);
                mDate.setText(sb.toString());
                AddActivity.year = year;
                AddActivity.month = monthOfYear;
                AddActivity.dayOfMonth = dayOfMonth;
            }
        });


        mDateCancel = findViewById(R.id.date_cancel_btn);
        mDateSet = findViewById(R.id.date_confirm_btn);

        mDateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
                activityStartDate.set(Calendar.MONTH, AddActivity.month);
                activityStartDate.set(Calendar.YEAR, AddActivity.year);
                activityStartDate.set(Calendar.DAY_OF_MONTH, AddActivity.dayOfMonth);

                activityEndDate.set(Calendar.MONTH, AddActivity.month);
                activityEndDate.set(Calendar.YEAR, AddActivity.year);
                activityEndDate.set(Calendar.DAY_OF_MONTH, AddActivity.dayOfMonth);

            }
        });

        mDateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
                mDate.setText("");
            }
        });

        mSubmit = findViewById(R.id.upload_activity);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateTime()){
                    Timestamp startTimeStamp = new Timestamp(activityStartDate.getTime());
                    Timestamp endTimeStamp = new Timestamp(activityEndDate.getTime());
                    com.example.Activity act = new com.example.Activity(mActivityName.getText().toString(), startTimeStamp, endTimeStamp);
                    dbController.addActivity(mEmail, act);
                }
            }
        });



    }

    private boolean validateTime(){
        if(mActivityName.getText() == null || mActivityName.getText().toString().matches("")){
            Toast.makeText(this,"Activity name cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(activityStartDate.isSet(Calendar.HOUR) && activityStartDate.isSet(Calendar.MINUTE) &&
                activityStartDate.isSet(Calendar.DAY_OF_MONTH) && activityStartDate.isSet(Calendar.MONTH)
                && activityStartDate.isSet(Calendar.YEAR))){
            Toast.makeText(this,"Please select activity start time", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(activityEndDate.isSet(Calendar.HOUR) && activityEndDate.isSet(Calendar.MINUTE) &&
                activityEndDate.isSet(Calendar.DAY_OF_MONTH) && activityEndDate.isSet(Calendar.MONTH)
                && activityEndDate.isSet(Calendar.YEAR))){
            Toast.makeText(this, "Please select activity end time",  Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }
}
