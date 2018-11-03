package com.example.fred.uscfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Sport;
import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {

   // private EditText mEmailText;
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
    private Button mCancel;

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

    private GetAllSports mGetAllSports;

    private static ArrayAdapter<String> dataAdapter;
    private static AddActivityAdapter loadingAdapter;
    private static AddActivityAdapter sportAdapter;

    private static List<String> userSports;

    final Context that = this;

    private static Spinner spinner;

    @Override
    protected void onResume() {
        super.onResume();
        //mActivityName.performClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbController = new DBController();

        activityStartDate = Calendar.getInstance();
        activityStartDate.clear();

        activityEndDate = Calendar.getInstance();
        activityEndDate.clear();

        mActivityName = findViewById(R.id.activity_name);
        spinner = findViewById(R.id.sports_dropdown_menu);
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
                if(userSports!=null && !userSports.contains(editableString)){
                    Toast.makeText(that, "No such sport", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mActivityName.addTextChangedListener(watcher);
        mActivityName.setVisibility(View.INVISIBLE);
        mActivityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(userSports == null) {
//                    userSports = mGetAllSports.getSports();
//                    dataAdapter = new ArrayAdapter<>(that, android.R.layout.simple_spinner_item, userSports);
//                    spinner.setAdapter(dataAdapter);
//                }
            }
        });

        mStartTime = findViewById(R.id.activity_start_time);

        mEmail = getIntent().getStringExtra("email");
        mEndTime = findViewById(R.id.activity_end_time);

        // set time picker layout

        mTimePicker = findViewById(R.id.time_picker);
        //mTimePicker.setBackgroundResource(android.R.drawable.btn_default);
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
        mStartTimeBtn.setBackgroundResource(android.R.color.transparent);
        mStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 0;
                mTimerLayout.setVisibility(View.VISIBLE);
            }
        });

        mEndTimeBtn = findViewById(R.id.show_time_picker_end);
        mEndTimeBtn.setBackgroundResource(android.R.color.transparent);
        mEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeType = 1;
                mTimerLayout.setVisibility(View.VISIBLE);
            }
        });

        mTimerCancel = findViewById(R.id.timer_cancel_btn);
        mTimerCancel.setBackgroundResource(android.R.color.transparent);
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
        mTimerSet.setBackgroundResource(android.R.color.transparent);
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
        mDatePickerBtn.setBackgroundResource(android.R.color.transparent);
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
                sb.append(monthOfYear+1).append("/").append(dayOfMonth).append("/").append(year);
                mDate.setText(sb.toString());
                AddActivity.year = year;
                AddActivity.month = monthOfYear;
                AddActivity.dayOfMonth = dayOfMonth;
            }
        });


        mDateCancel = findViewById(R.id.date_cancel_btn);
        mDateSet = findViewById(R.id.date_confirm_btn);
        mDateSet.setBackgroundResource(android.R.color.transparent);
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
        mDateCancel.setBackgroundResource(android.R.color.transparent);
        mDateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateLayout.setVisibility(View.GONE);
                mDate.setText("");
            }
        });

        mSubmit = findViewById(R.id.upload_activity);
        mSubmit.setBackgroundResource(android.R.color.transparent);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateTime()){
                    Timestamp startTimeStamp = new Timestamp(activityStartDate.getTime());
                    Timestamp endTimeStamp = new Timestamp(activityEndDate.getTime());
                    com.example.Activity act = new com.example.Activity(mActivityName.getText().toString(), startTimeStamp, endTimeStamp);
                    dbController.addActivity(mEmail, act);
                    //Toast.makeText(v.getContext(), "activity has been submitted!", Toast.LENGTH_LONG).show();
                    showAlertWhenValid();
                    mActivityName.setText("");
                    mStartTime.setText("");
                    mEndTime.setText("");
                    mDate.setText("");
                }

            }
        });

        mCancel = findViewById(R.id.cancel_activity);
        mCancel.setBackgroundResource(android.R.color.transparent);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });


        // get user's sports asy
        mGetAllSports = new GetAllSports(getIntent().getStringExtra("email"));
        mGetAllSports.execute((Void) null);

//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userSports);
//        spinner.setAdapter(dataAdapter);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                String text = parent.getItemAtPosition(position).toString();
//                text = spinner.getItemAtPosition(position).toString();
                mActivityName.setText(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        if(activityStartDate.compareTo(activityEndDate) >= 0){
            Toast.makeText(this, "Start time must be earlier that the end time", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }



    // asynchhronous function that gets all sports from database
    private class GetAllSports extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private List<Sport> sports;
        private List<String> result = new ArrayList<>();
        GetAllSports(String email)
        {
            this.mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            this.sports = dbController.getAllSports(this.mEmail);
            String[] loading = {"1"};
            loadingAdapter = new AddActivityAdapter(that, Arrays.asList(loading));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinner.setAdapter(loadingAdapter);
                }
            });
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            spinner = findViewById(R.id.sports_dropdown_menu);
            for(Sport sport : this.sports)
            {
                result.add(sport.name);
            }
            sportAdapter = new AddActivityAdapter(that, result);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinner.setAdapter(sportAdapter);
                }
            });
        }

        public List<String> getSports()
        {
            return this.result;
        }
    }

    public static List<String> getUserSports(){
        return userSports;
    }

    // shows alert box when the submission is successful
    private void showAlertWhenValid() {
        // when the user input is invalid (when they didn't input name or something)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Submit Successful!");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
