package com.example.fred.uscfit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;
import com.google.common.io.LineReader;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.ParseException;
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
    private LinearLayout mLinearLayout;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        String dateStr = intent.getStringExtra("date");
        sdf = new SimpleDateFormat("yyyy_MM_dd");
        if(dateStr == null || dateStr.length() == 0) {
            cal = Calendar.getInstance();
        }
        else {
            try {
                cal = Calendar.getInstance();
                cal.setTime(sdf.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dbController = new DBController();
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mLoadingView = findViewById(R.id.loadingProgress);
        showProgress(true);


        GetAllPlansTask mGetAllPlansTask = new GetAllPlansTask(mEmail, this);
        mGetAllPlansTask.execute((Void) null);

    }

    public Calendar getCalendar() {
        return cal;
    }


    public void updatePlanStatus() {
        SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy MMM.dd");
        final TableRow BadgeTableRow = (TableRow) findViewById(R.id.BadgeRow);
        boolean weeklyPlanCompleted = true;

        Calendar currCal = Calendar.getInstance();
        currCal.setTime(cal.getTime());
        currCal.add(Calendar.DAY_OF_MONTH, 1);
        for(int i=0; i<mLinearLayout.getChildCount(); i++) {
            if(mLinearLayout.getChildAt(i).getClass() != CardView.class) {
                continue;
            }
            CardView childCardView = (CardView) mLinearLayout.getChildAt(i);
            LinearLayout childLinearLayout = (LinearLayout) childCardView.getChildAt(0);
            if(childLinearLayout.getChildAt(0).getClass() != ProgressBar.class) {
                continue;
            }
            ProgressBar childProgressBar = (ProgressBar) childLinearLayout.getChildAt(0);
            LinearLayout childVerticalLayout = (LinearLayout) childLinearLayout.getChildAt(1);
            final ImageView childImageView = (ImageView) childVerticalLayout.getChildAt(0);
            final LinearLayout planDetailsLayout = (LinearLayout) childVerticalLayout.getChildAt(1);
            TextView childTextView = (TextView) planDetailsLayout.getChildAt(0);

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
            if(actualStep == 0 && targetStep == 0) footstepProgress = 100;
            childProgressBar.setProgress((int)footstepProgress);

            // get plan details
            boolean plancompleted = true;
            for(Object o: plan.activity) {
                if(o.getClass() == Activity.class) {
                    final Activity plannedActivity = (Activity) o;
                    // add curr plan to plandetailsLayout
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView planItemText = new TextView(ProgressActivity.this);
                            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            String startTime = timeFormat.format(plannedActivity.start.toDate());
                            String endTime = timeFormat.format(plannedActivity.end.toDate());
                            planItemText.setText(plannedActivity.name + " " + startTime + " - " + endTime);
                            planDetailsLayout.addView(planItemText);
                        }
                    });

                    List<Activity> currActivities = myActivities.get(currDate);
                    boolean plannedActivityCompleted = false;
                    if(currActivities == null) {
                        plancompleted = false;
                        continue;
                    }
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

    public void setMyActivities(Map<String, List<Activity>> myActivities) {
        this.myActivities = myActivities;
    }

    public void setMyPlans(Map<String, Plan> myPlans) {
        this.myPlans = myPlans;
    }

    public void setMyFootsteps(Map<String, Footstep> myFootsteps) {
        this.myFootsteps = myFootsteps;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
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
