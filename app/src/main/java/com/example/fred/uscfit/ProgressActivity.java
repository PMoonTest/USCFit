package com.example.fred.uscfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.sql.Time;
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
        cal.add(Calendar.DAY_OF_MONTH, -1);
        sdf = new SimpleDateFormat("yyyy_MM_dd");


        GetAllPlansTask mGetAllPlansTask = new GetAllPlansTask((mEmail));
        mGetAllPlansTask.execute((Void) null);




    }

    public void updateFootstepBar() {
        String currDate="2018_10_20";
        // for today's data
        if(cal != null) {
            currDate = sdf.format(cal.getTime());
            Log.d(TAG, "updateFootstepBar: " + currDate);
        }


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

    public void updatePlanStatus() {
        SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy MMM.dd");
        String currDate = outputSdf.format(cal.getTime());
//        TextView tv2 = (TextView) findViewById(R.id.textViewPlan2);
//        TextView tv3 = (TextView) findViewById(R.id.textViewPlan3);
//        TextView tv4 = (TextView) findViewById(R.id.textViewPlan4);
//        TextView tv5 = (TextView) findViewById(R.id.textViewPlan5);
//        TextView tv6 = (TextView) findViewById(R.id.textViewPlan6);
//        TextView tv7 = (TextView) findViewById(R.id.textViewPlan7);

        boolean planCompleted = true;

//        Plan plan = myPlans.get(currDate);




//        TextView tv1 = (TextView) findViewById(R.id.textViewPlan1);
//        tv1.setText(currDate);
//        ImageView imageViewCheckOn = (ImageView) findViewById(R.id.imageView2);
//        if(planCompleted) {
//            imageViewCheckOn.setVisibility(View.VISIBLE);
//        }
//        else{
//            imageViewCheckOn.setVisibility(View.GONE);
//        }


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
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
            for(int i = 0; i<3; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String planName = sdf.format(calendar.getTime());
                Log.d(TAG, "doInBackground: planName" + planName );
                Plan currPlan = dbController.getPlan(mEmail, planName);
                if(currPlan != null) {
                    myPlans.put(planName, currPlan);
                }
            }
            Log.d(TAG, "doInBackground: " + myPlans.size());

            List<Object> allActivities = dbController.getAllActivity(mEmail);
            Map<String, Activity> myActivities = new HashMap<>();
            Map<String, Footstep> myFootsteps = new HashMap<>();
            for(Object o: allActivities) {
                HashMap<String, Object> map = (HashMap<String, Object>) o;
                if(map.get("name").equals("footsteps")) {
                    Footstep footstep = new Footstep();
                    footstep.name = "footsteps";
                    footstep.date = (Timestamp) map.get("start");
                    footstep.value = (Long) map.get("value");
                }
                else {
                    Activity activity = new Activity();
                    activity.name = (String) map.get("name");
                    activity.start = (Timestamp)map.get("start");
                    activity.end = (Timestamp)map.get("end");
                }
            }

            ProgressActivity.this.myActivities = myActivities;
            ProgressActivity.this.myPlans = myPlans;
            ProgressActivity.this.myFootsteps = myFootsteps;

            ProgressActivity.this.updateFootstepBar();


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                // this means sport already exists
//                alert("Oops...", "Sport category already exists.");
//                ProgressActivity.this.updateFootstepBar();
                return;
            } else {
                // sport doesn't exist
                // all input is valid, now call DBController to insert sport
//                dbController.addSports(getIntent().getStringExtra("email"), new Sport(sportName, calories));
//                alert("Yeah!", "Successfully added sport category.");
                return;
            }
        }

    }
}
