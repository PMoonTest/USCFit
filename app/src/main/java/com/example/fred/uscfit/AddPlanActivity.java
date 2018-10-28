package com.example.fred.uscfit;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.Activity;
import com.example.Plan;
import com.example.Sport;
import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AddPlanActivity extends AppCompatActivity {
    private GetAllSports mGetAllSports = null;
    private DBController dbController = null;
    private static List<String> mySports;
    private static ArrayAdapter<String> dataAdapter;

    // stores all the plan and activity data for database usages
    private Plan plan = new Plan();
    private String planName = "";
    private List<Activity> activityList = new ArrayList<Activity>();
    private Timestamp planTime = null;
    private int index = 0;
    private Calendar today = null;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        dbController = new DBController();
        mGetAllSports = new GetAllSports(getIntent().getStringExtra("email"));
        mGetAllSports.execute((Void) null);
        mySports = mGetAllSports.getSports();
        System.out.println("mySports 大小第一： " + Integer.toString(mySports.size()));
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mySports);

        setContentView(R.layout.activity_add_plan);

        // handles case when submit button is clicked
        Button submitPlanBtn = (Button) findViewById(R.id.submitBtn);
        submitPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets the selected plan date, plan name text field
                final DatePicker datePicker = (DatePicker) findViewById(R.id.planDate);
                EditText planNameInput = (EditText) findViewById(R.id.planNameInput);
                // initializes the plan time
                planTime = new Timestamp(getDateFromDatePicker(datePicker));
                // selects the plan name
                planName  = planNameInput.getText().toString();

                datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        planTime = new Timestamp(getDateFromDatePicker(datePicker));
                    }
                });
                // when the user input is invalid, show alert box and end here
                if(!userInputValid())
                {
                    showAlertWhenInvalid();
                    return;
                }

                plan.name = planName;
                plan.date = planTime;
                plan.activity = (ArrayList) activityList;


                AddPlan mAddPlan = new AddPlan(plan, getIntent().getStringExtra("email"));
                mAddPlan.execute((Void) null);
                showAlertWhenValid();
            }
        });

        // Adding new activities
        FloatingActionButton addActivityBtn = (FloatingActionButton) findViewById(R.id.addActivityBtn);
        final Context that = this;
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Activity activity = new Activity();
                activityList.add(activity);
                System.out.println("大小： " + Integer.toString(activityList.size()));

                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
                EditText planNameInput = (EditText) findViewById(R.id.planNameInput);

                // add activity name label
                final Spinner spinner = new Spinner(that);
                spinner.setId(new Integer(1));
                spinner.setAdapter(dataAdapter);
                System.out.println("mySports 大小： " + Integer.toString(mySports.size()));
                if(mySports.size()>0)
                {
                    spinner.setSelection(0);
                    System.out.println("Spinner 大小： " + Integer.toString(spinner.getSelectedItemPosition()));
                }

                // initializes the activityName
                activityList.get(index).name = spinner.getSelectedItem() == null ? "" : spinner.getSelectedItem().toString();

                // sets the onchange listener for spinner
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    int currIndex = index;
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        activityList.get(currIndex).name = spinner.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
                );
                params1.addRule(RelativeLayout.ABOVE, planNameInput.getId());
                spinner.setLayoutParams(params1);


                today = Calendar.getInstance();
                final int mYear = today.get(Calendar.YEAR);
                final int mMonth = today.get(Calendar.MONTH);
                final int mDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
                final int mHour = today.get(Calendar.HOUR_OF_DAY);
                final int mMinute = today.get(Calendar.MINUTE);

                final TimePickerDialog startDate = new TimePickerDialog(that, new TimePickerDialog.OnTimeSetListener() {
                    int currIndex = index;
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        activityList.get(currIndex).start = new Timestamp(new Date(mYear, mMonth, mDayOfMonth, hourOfDay, minute));
                        System.out.println(Integer.toString(hourOfDay) + " " + Integer.toString(minute));
                    }
                }, mHour, mMinute, false);
                startDate.hide();


                final TimePickerDialog endDate = new TimePickerDialog(that, new TimePickerDialog.OnTimeSetListener() {
                    int currIndex = index;
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        activityList.get(currIndex).end = new Timestamp(new Date(mYear-1900, mMonth, mDayOfMonth, hourOfDay, minute));
                    }
                }, mHour, mMinute, false);
                endDate.hide();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                // initlaizes pick date button
                Button pickStartBtn = new Button(that);
                pickStartBtn.setGravity(Gravity.CENTER);
                pickStartBtn.setText("Pick Start Time");
                pickStartBtn.setId(new Integer(3));
                pickStartBtn.setOnClickListener(new View.OnClickListener() {
                    int currIndex = index;
                    @Override
                    public void onClick(View v) {
                        System.out.println(currIndex);
                        activityList.get(currIndex).start = new Timestamp(today.getTime());
                        startDate.show();
                    }
                });
                pickStartBtn.setLayoutParams(params);


                RelativeLayout.LayoutParams paramsTemp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsTemp.setMargins(0, 0, 0, 100);

                Button pickEndBtn = new Button(that);
                pickEndBtn.setGravity(Gravity.CENTER);
                // sets button width to 50%'

                pickEndBtn.setText("Pick End Time");
                pickEndBtn.setId(new Integer(3));
                pickEndBtn.setGravity(Gravity.CENTER_HORIZONTAL);
                pickEndBtn.setOnClickListener(new View.OnClickListener() {
                    int currIndex = index;
                    @Override
                    public void onClick(View v) {
                        activityList.get(currIndex).start = new Timestamp(today.getTime());
                        endDate.show();
                    }
                });

                pickEndBtn.setLayoutParams(paramsTemp);


                layout.addView(spinner);
                layout.addView(pickStartBtn);
                layout.addView(pickEndBtn);


                // initializes the activity class for this particular activity

                index++;
            }
        });
    }

    // shows alert box when the data user inputs is invalid
    private void showAlertWhenInvalid() {
        // when the user input is invalid (when they didn't input name or something)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error Message");
        builder.setMessage("Invalid User Input. Please Fill in All Fields!");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
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
    // returns false when the user input is invalid
    // returns true otherwise
    private Boolean userInputValid() {
        if(planName == null || planName.length()==0) return false;
        if(planTime == null) return false;
        for(int x=0; x<activityList.size(); x++) {
            Activity curr = activityList.get(x);
            if(curr.name==null || curr.name.length()==0) return false;
            if(curr.start == null) return false;
            if(curr.end == null) return false;
        }
        return true;
    }
    // gets date from datePicker
    private Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // asynchhronous function that gets all sports from database
    private class GetAllSports extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private List<Sport> sports;
        private List<String> result = new ArrayList<String>();
        GetAllSports(String email)
        {
            this.mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params){
            this.sports = dbController.getAllSports(this.mEmail);
            if(sports == null || sports.size() == 0) return false;
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            for(Sport sport : this.sports)
            {
                result.add(sport.name);
            }
        }

        public List<String> getSports()
        {
            return this.result;
        }
    }

    private class AddPlan extends AsyncTask<Void, Void, Boolean> {
        private final Plan plan;
        private final String email;
        AddPlan(Plan _plan, String _email) {
            this.plan = _plan;
            this.email = _email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            dbController.addPlan(email, plan);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            return;
        }
    }

}
