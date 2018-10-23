package com.example.fred.uscfit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.Activity;
import com.example.Footstep;
import com.example.Sport;
import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private DBController dbController = null;
    private GetProfileTask mGetProfileTask = null;
    private TextView mAllSportsText = null;
    private TextView mAllActivitiesText = null;
    private TextView mUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.dbController = new DBController();
        this.mAllSportsText = (TextView) findViewById(R.id.allSportsText);
        this.mAllSportsText.setMovementMethod(new ScrollingMovementMethod());

        this.mAllActivitiesText = (TextView) findViewById(R.id.allActivitiesText);
        this.mAllActivitiesText.setMovementMethod(new ScrollingMovementMethod());

        this.mUsername = (TextView) findViewById(R.id.usernameLabel);
        this.mUsername.setText(getIntent().getStringExtra("email"));

        this.mGetProfileTask = new GetProfileTask(getIntent().getStringExtra("email"));
        this.mGetProfileTask.execute((Void) null);


    }


    public class GetProfileTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private String allSportsContent = "";
        private String allActivitiesContent = "";
        private HashMap<String, Integer> calorieMap = new HashMap<>();

        public GetProfileTask(String email) {
            this.mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // populate all the sports
            this.populateAllSports();

            // populate all the activities
            this.populateAllActivities();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mGetProfileTask = null;
            mAllSportsText.setText(this.allSportsContent);
            mAllActivitiesText.setText(this.allActivitiesContent);
        }

        private void populateAllSports() {
            List<Sport> mySports = dbController.getAllSports(mEmail);
            if(mySports == null || mySports.size() == 0) return;
            StringBuilder sb = new StringBuilder();
            for(Sport mySport : mySports) {
                sb.append(mySport.name + ": " + Integer.toString(mySport.calorie) + " calories/hr" + "\n");

                // also populate calorie map
                this.calorieMap.put(mySport.name, mySport.calorie);
            }
            this.allSportsContent = sb.toString();
        }

        private void populateAllActivities() {
            SimpleDateFormat sdf = new SimpleDateFormat();
            List<Object> allActivities = dbController.getAllActivity(mEmail);
            Map<String, Footstep> myFootsteps = new HashMap<>();
            Map<String, Activity> myActivities = new HashMap<>();
            StringBuilder sb = new StringBuilder();

            for(Object o: allActivities) {
                HashMap<String, Object> map = (HashMap<String, Object>) o;
                if(map.get("name").equals("footsteps")) {
                    Footstep footstep = new Footstep();
                    footstep.name = "footsteps";
                    footstep.date = (Timestamp) map.get("date");
                    footstep.value = (Long) map.get("value");
                    String currDate = sdf.format(footstep.date.toDate().getTime());
                    myFootsteps.put(currDate, footstep);
                } else {
                    Activity activity = new Activity();
                    activity.name = (String) map.get("name");
                    activity.start = (Timestamp)map.get("start");
                    activity.end = (Timestamp)map.get("end");
                    String startDate = sdf.format(activity.start.toDate().getTime());
                    String endDate = sdf.format(activity.end.toDate().getTime());

                    double calorieSum = this.getCalories(activity.name, activity.start, activity.end);
                    DecimalFormat calorieSumFormat = new DecimalFormat("#0.00");

                    myActivities.put("total calories: " + calorieSumFormat.format(calorieSum) + ", " + startDate + " - " + endDate, activity);
                }

            }

            for(String key : myFootsteps.keySet()) {
                sb.append(myFootsteps.get(key).name + ", " + Long.toString(myFootsteps.get(key).value) + " steps, " + key + "\n");
            }

            for(String key : myActivities.keySet()) {
                sb.append(myActivities.get(key).name + ", " + key + "\n");
            }

            this.allActivitiesContent = sb.toString();
        }

        private double getCalories(String name, Timestamp start, Timestamp end) {
            if(!this.calorieMap.containsKey(name)) {
                double diff = Math.abs(end.toDate().getTime() - start.toDate().getTime()) / (60 * 60 * 1000.0);
                return 100 * diff;
            } else {
                double rate = this.calorieMap.get(name);
                double diff = Math.abs(end.toDate().getTime() - start.toDate().getTime()) / (60 * 60 * 1000.0);
                return rate * diff;
            }
        }

    }


}
