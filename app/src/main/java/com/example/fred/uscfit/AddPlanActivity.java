package com.example.fred.uscfit;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.Sport;
import com.example.db.DBController;

import java.util.ArrayList;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        dbController = new DBController();
        mGetAllSports = new GetAllSports(getIntent().getStringExtra("email"));
        mGetAllSports.execute((Void) null);
        mySports = mGetAllSports.getSports();
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mySports);

        setContentView(R.layout.activity_add_plan);

        // Adding new activities
        FloatingActionButton addActivityBtn = (FloatingActionButton) findViewById(R.id.addActivityBtn);
        final Context that = this;
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
                EditText planNameInput = (EditText) findViewById(R.id.planNameInput);

                // add activity name label
                Spinner spinner = new Spinner(that);
                spinner.setId(new Integer(1));
                spinner.setAdapter(dataAdapter);


                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT
                );
                params1.addRule(RelativeLayout.ABOVE, planNameInput.getId());
                spinner.setLayoutParams(params1);

                Calendar today = Calendar.getInstance();
                int mHour = today.get(Calendar.HOUR_OF_DAY);
                int mMinute = today.get(Calendar.MINUTE);

                final TimePickerDialog startDate = new TimePickerDialog(that, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        // txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
                startDate.hide();


                final TimePickerDialog endDate = new TimePickerDialog(that, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        // txtTime.setText(hourOfDay + ":" + minute);
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
                    @Override
                    public void onClick(View v) {
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
                    @Override
                    public void onClick(View v) {
                        endDate.show();
                    }
                });

                pickEndBtn.setLayoutParams(paramsTemp);


                layout.addView(spinner);
                layout.addView(pickStartBtn);
                layout.addView(pickEndBtn);
            }
        });
    }

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
}
