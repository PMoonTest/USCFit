package com.example.fred.uscfit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;
import com.google.api.Distribution;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = "Progress";
    private DBController dbController = null;
    private String mEmail;
    private Map<String, Plan> myPlans;
    private Map<String, List<Activity>> myActivities;
    private Map<String, Footstep> myFootsteps;
    private Calendar cal;
    private SimpleDateFormat sdf;

    private ConstraintLayout mConstraintLayout;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        dbController = new DBController();
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy_MM_dd");
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mLoadingView = findViewById(R.id.loadingProgress);
        showProgress(true);


        GetAllPlansTask mGetAllPlansTask = new GetAllPlansTask((mEmail));
        mGetAllPlansTask.execute((Void) null);

    }


    public void updatePlanStatus() {
        SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy MMM.dd");
        final TableRow BadgeTableRow = (TableRow) findViewById(R.id.BadgeRow);
        boolean weeklyPlanCompleted = true;

        Calendar currCal = cal;
        for(int i=0; i<mConstraintLayout.getChildCount(); i++) {
            if(mConstraintLayout.getChildAt(i).getClass() != CardView.class) {
                continue;
            }
            CardView childCardView = (CardView) mConstraintLayout.getChildAt(i);
            LinearLayout childLinearLayout = (LinearLayout) childCardView.getChildAt(0);
            if(childLinearLayout.getChildAt(0).getClass() != ProgressBar.class) {
                continue;
            }
            ProgressBar childProgressBar = (ProgressBar) childLinearLayout.getChildAt(0);
            LinearLayout childVerticalLayout = (LinearLayout) childLinearLayout.getChildAt(1);
            final ImageView childImageView = (ImageView) childVerticalLayout.getChildAt(0);
            TextView childTextView = (TextView) childVerticalLayout.getChildAt(1);

            currCal.add(Calendar.DAY_OF_MONTH, -1);
            String currDate = sdf.format(currCal.getTime());
            String outputDate = outputSdf.format(currCal.getTime());

            // get plan
            Plan plan = myPlans.get(currDate);
            if(plan == null) {
                childProgressBar.setProgress(0);
                childTextView.setText("No plan for " + outputDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        childImageView.setVisibility(View.GONE);
                    }
                });
                weeklyPlanCompleted = false;
                continue;
            }

            // get footsteps
            long actualStep = 0;
            if(myFootsteps.containsKey(currDate)) {
                actualStep = myFootsteps.get(currDate).value;
            }
            Footstep targetFootstep = null;
            long targetStep = actualStep;
            for(Object o: plan.activity) {
                if(o.getClass() == Footstep.class) {
                    targetFootstep = (Footstep) o;
                    break;
                }
            }
            if(targetFootstep != null) {
                targetStep = targetFootstep.value;
            }
            double footstepProgress = (double) actualStep/targetStep * 100;
            childProgressBar.setProgress((int)footstepProgress);

            // get plan details
            boolean plancompleted = true;
            for(Object o: plan.activity) {
                if(o.getClass() == Activity.class) {
                    Activity plannedActivity = (Activity) o;
                    List<Activity> currActivities = myActivities.get(currDate);
                    boolean plannedActivityCompleted = false;
                    for(Activity activity : currActivities) {
                        if(isFinishedActivity(activity, plannedActivity)) {
                            Log.d(TAG, "run: FINISHED PLAN");
                            plannedActivityCompleted = true;
                            break;
                        }
                    }
                    if(!plannedActivityCompleted) {
                        plancompleted = false;
                        break;
                    }
                }
            }
            if(plancompleted && footstepProgress == 100) {
                childTextView.setText(outputDate + " Completed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView newBadge = new ImageView(ProgressActivity.this);
                        newBadge.setImageResource(R.drawable.guarantee);
                        TableRow.LayoutParams params = new TableRow.LayoutParams();
                        params.height = 100;
                        params.width = 100;
                        newBadge.setLayoutParams(params);
                        BadgeTableRow.addView(newBadge);
                    }
                });


            }
            else {
                childTextView.setText(outputDate + " Not completed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        childImageView.setVisibility(View.GONE);
                    }
                });
                weeklyPlanCompleted = false;
            }
        }

        if(weeklyPlanCompleted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView newBadge = new ImageView(ProgressActivity.this);
                    newBadge.setImageResource(R.drawable.trojan);
                    TableRow.LayoutParams params = new TableRow.LayoutParams();
                    params.height = 100;
                    params.width = 100;
                    newBadge.setLayoutParams(params);
                    BadgeTableRow.addView(newBadge);
                }
            });
        }
    }


    // return true is a fulfills plan b
    public boolean isFinishedActivity(Activity a, Activity b) {
        if(!a.name.equals(b.name)) return false;
//        if a start later than b
        if(a.start.compareTo(b.start) > 0) return false;
//        if a ends earlier than b
        if(a.end.compareTo(b.end) < 0) return false;
        return true;
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

            ProgressActivity.this.myActivities = myActivities;
            ProgressActivity.this.myPlans = myPlans;
            ProgressActivity.this.myFootsteps = myFootsteps;

//            ProgressActivity.this.updateFootstepBar();
            ProgressActivity.this.updatePlanStatus();


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mConstraintLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mConstraintLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mConstraintLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mConstraintLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
