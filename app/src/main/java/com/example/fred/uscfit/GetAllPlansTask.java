package com.example.fred.uscfit;

import android.os.AsyncTask;
import android.util.Log;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllPlansTask  extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private static final String TAG = "Progress";
    private DBController dbController = null;
    private ProgressActivity mActivity;

    public GetAllPlansTask(String email, ProgressActivity activity) {
        super();
        mEmail = email;
        mActivity = activity;

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        dbController = new DBController();
        Map<String, Plan> myPlans = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        for(int i = 0; i<7; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String planName = sdf.format(calendar.getTime());
            Log.d(TAG, "doInBackground: planName" + planName );
            Plan currPlan = dbController.getPlan(mEmail, planName);
            if(currPlan.name != null) {
                myPlans.put(planName, currPlan);
            }
        }
        Log.d(TAG, "doInBackground: " + myPlans.size());

        List<Object> allActivities = dbController.getAllActivity(mEmail);
        Map<String, List<Activity>> myActivities = new HashMap<>();
        Map<String, Footstep> myFootsteps = new HashMap<>();
        for(Object o: allActivities) {
            HashMap<String, Object> map = (HashMap<String, Object>) o;
            if(map.get("name").equals("footsteps")) {
                Footstep footstep = new Footstep();
                footstep.name = "footsteps";
                footstep.date = (Timestamp) map.get("date");
                footstep.value = (Long) map.get("value");
                String currDate = sdf.format(footstep.date.toDate());
                myFootsteps.put(currDate, footstep);
            }
            else {
                Activity activity = new Activity();
                activity.name = (String) map.get("name");
                activity.start = (Timestamp)map.get("start");
                activity.end = (Timestamp)map.get("end");
                String currDate = sdf.format(activity.start.toDate());
                if(myActivities.containsKey(currDate)) {
                    myActivities.get(currDate).add(activity);
                }
                else {
                    List<Activity> newActivity = new ArrayList<>();
                    newActivity.add(activity);
                    myActivities.put(currDate, newActivity);
                }
            }
        }
        mActivity.setMyActivities(myActivities);
        mActivity.setMyPlans(myPlans);
        mActivity.setMyFootsteps(myFootsteps);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mActivity.showProgress(false);
        if (success) {
            mActivity.updatePlanStatus();
            return;
        } else {
            return;
        }
    }


}
