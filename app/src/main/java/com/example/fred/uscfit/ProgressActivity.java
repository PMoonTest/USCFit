package com.example.fred.uscfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = "Progress";
    private DBController dbController = null;
    private String mEmail;
    private Map<String, Plan> myPlans;
    private Map<String, Activity> myActivities;
    private Map<String, Footstep> myFootsteps;
    private Calendar cal;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        dbController = new DBController();
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy_MM_dd");


        GetAllPlansTask mGetAllPlansTask = new GetAllPlansTask((mEmail));
        mGetAllPlansTask.execute((Void) null);


    }

    public void updateFootstepBar() {
        // for today's data
        String currDate = sdf.format(cal);

        long actualStep = myFootsteps.get(currDate).value;
        Plan plan = myPlans.get(currDate);
        Footstep targetFootstep = null;
        for(Object o: plan.activity) {
            if(o.getClass() == Footstep.class) {
                targetFootstep = (Footstep) o;
                break;
            }
        }
        long targetStep = targetFootstep.value;
        double footstepProgress = (double) actualStep/targetStep * 100;
        ProgressBar footstepsBar = (ProgressBar) findViewById(R.id.progressBar);
        footstepsBar.setProgress((int)footstepProgress);
    }


    public class GetAllPlansTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;

        public GetAllPlansTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Map<String, Plan> myPlans = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
            for(int i = 0; i<7; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String planName = sdf.format(calendar.getTime());
                Plan currPlan = dbController.getPlan(mEmail, planName);
                myPlans.put(planName, currPlan);
            }
            Log.d(TAG, "doInBackground: " + myPlans.size());

            List<Object> allActivities = dbController.getAllActivity(mEmail);
            Map<String, Activity> myActivities = new HashMap<>();
            Map<String, Footstep> myFootsteps = new HashMap<>();
            for(Object o: allActivities) {
                if(o.getClass() == Activity.class) {
                    Activity activity = (Activity) o;
                    Date date = activity.start.toDate();
                    myActivities.put(sdf.format(date), activity);
                }
                else {
                    Footstep footstep = (Footstep) o;
                    Date date = footstep.date.toDate();
                    myFootsteps.put(sdf.format(date), footstep);
                }
            }

            ProgressActivity.this.myActivities = myActivities;
            ProgressActivity.this.myPlans = myPlans;
            ProgressActivity.this.myFootsteps = myFootsteps;


            return true;
        }

//        @Override
//        protected void onPostExecute(final Boolean success) {
//            if (success) {
//                // this means sport already exists
//                alert("Oops...", "Sport category already exists.");
//                return;
//            } else {
//                // sport doesn't exist
//                // all input is valid, now call DBController to insert sport
////                dbController.addSports(getIntent().getStringExtra("email"), new Sport(sportName, calories));
//                alert("Yeah!", "Successfully added sport category.");
//                return;
//            }
//        }

    }
}
