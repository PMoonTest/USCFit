package com.example.fred.uscfit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
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

public class ProgressCalendarActivity extends AppCompatActivity {
    private static final String TAG = "ProgressCalendar";
    private DBController dbController = null;
    private String mEmail;
    private Plan mPlan;
    private List<Activity> mActivities;
    private Footstep mFootstep;
    private Calendar cal;
    private SimpleDateFormat sdf;

    private ConstraintLayout mConstraintLayout;
    private CalendarView mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_calendar);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        String dateStr = intent.getStringExtra("date");
        sdf = new SimpleDateFormat("yyyy_MM_dd");
        if (dateStr == null || dateStr.length() == 0) {
            cal = Calendar.getInstance();
        } else {
            try {
                cal = Calendar.getInstance();
                cal.setTime(sdf.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dbController = new DBController();
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mCalendar = (CalendarView) findViewById(R.id.calendarView);
        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month ++;
               String planName = year + "_" + month + "_" + dayOfMonth;
               getPlanTask(planName);
            }
        });

    }

    public Calendar getCalendar() {
        return cal;
    }

    public void getPlanTask(String planName) {
        GetPlanTask getPlanTask = new GetPlanTask(mEmail, this, planName);
        getPlanTask.execute((Void) null);
    }

    public void displayPlan(String planName) {
        TextView planNameText = (TextView) findViewById(R.id.planName);
        final ImageView badgeImageView = (ImageView) findViewById(R.id.badge);
        LinearLayout plannedActivitiesLayout = (LinearLayout) findViewById(R.id.plannedActivities);
        ProgressBar footstepProgressBar = (ProgressBar) findViewById(R.id.footstepProgress);

        // if no plan
        if (mPlan.name == null) {
            planNameText.setText("No plan for " +planName);
            badgeImageView.setVisibility(View.GONE);
            footstepProgressBar.setProgress(0);
            return;
        }


        // display footstep progress
        long actualStep = 0;
        if (mFootstep != null) {
            actualStep = mFootstep.value;
        }
        Footstep targetFootstep = null;
        long targetStep = actualStep;
        for (Object o : mPlan.activity) {
            if (o.getClass() == Footstep.class) {
                targetFootstep = (Footstep) o;
                break;
            }
        }
        if (targetFootstep != null) {
            targetStep = targetFootstep.value;
        }

        double footstepProgress = (double) actualStep / targetStep * 100;
        if (actualStep == 0 && targetStep == 0) footstepProgress = 100;
        footstepProgressBar.setProgress((int) footstepProgress);

        // get plan details
        boolean plancompleted = true;
        for (Object o : mPlan.activity) {
            if (o.getClass() == Activity.class) {
                final Activity plannedActivity = (Activity) o;
                // add curr plan to plandetailsLayout
                boolean plannedActivityCompleted = false;
                if (mActivities != null) {
                    for (Activity activity : mActivities) {
                        if (!plannedActivityCompleted && isFinishedActivity(activity, plannedActivity)) {
                            Log.d(TAG, "run: FINISHED PLAN");
                            plannedActivityCompleted = true;
                        }
                    }
                }

                // if the planned activity is not completed
                if (!plannedActivityCompleted) {
                    plancompleted = false;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            CheckBox planItemCheck = new CheckBox(ProgressActivity.this);
//                            planItemCheck.setChecked(false);
//                            int checkBoxId = View.generateViewId();
//                            checkBoxMap.put(checkBoxId, plannedActivity);
//                            Log.d(TAG, "CHECKBOX ADD: " + sdf.format(plannedActivity.start.toDate()));
//                            planItemCheck.setId(checkBoxId);
//
//                            planItemCheck.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
////
//                                    @Override
//                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                        if (isChecked == true) {
//                                            Activity currActivity = checkBoxMap.get(buttonView.getId());
//                                            Log.d(TAG, "CHECKBOX GET: " + sdf.format(currActivity.start.toDate()));
//                                            dbController.addActivity(mEmail, currActivity);
//                                        }
//                                    }
//                                }
//                                ));
////                                TextView planItemText = new TextView(ProgressActivity.this);
//                                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
//                                String startTime = timeFormat.format(plannedActivity.start.toDate());
//                                String endTime = timeFormat.format(plannedActivity.end.toDate());
////                                planItemText.setText(plannedActivity.name + " " + startTime + " - " + endTime);
//                                planItemCheck.setText(plannedActivity.name + " " + startTime + " - " + endTime);
//                                planDetailsLayout.addView(planItemCheck);
//                            }
//                        });
                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                CheckBox planItemCheck = new CheckBox(ProgressActivity.this);
//                                planItemCheck.setChecked(true);
////                                TextView planItemText = new TextView(ProgressActivity.this);
//                                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
//                                String startTime = timeFormat.format(plannedActivity.start.toDate());
//                                String endTime = timeFormat.format(plannedActivity.end.toDate());
////                                planItemText.setText(plannedActivity.name + " " + startTime + " - " + endTime);
//                                planItemCheck.setText(plannedActivity.name + " " + startTime + " - " + endTime);
//                                planDetailsLayout.addView(planItemCheck);
//                            }
//                        });
                    }
                }
            }
            if (plancompleted && footstepProgress == 100) {
                planNameText.setText(planName + " Completed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        badgeImageView.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                planNameText.setText(planName + " Not completed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        badgeImageView.setVisibility(View.GONE);
                    }
                });
            }
        }

    // return true is a fulfills plan b
    public boolean isFinishedActivity(Activity a, Activity b) {
        if (!a.name.equals(b.name)) return false;
//        if a start later than b
        if (a.start.compareTo(b.start) > 0) return false;
//        if a ends earlier than b
        if (a.end.compareTo(b.end) < 0) return false;
        return true;
    }

    public void setMyActivities(List<Activity> mActivities) {
        this.mActivities = mActivities;
    }

    public void setMyPlans(Plan plan) {
        this.mPlan = plan;
    }

    public void setMyFootsteps(Footstep footstep) {
        this.mFootstep = footstep;
    }

    class GetPlanTask extends AsyncTask<Void, Void, Boolean> {
        private String mEmail;
        private ProgressCalendarActivity mActivity;
        private DBController dbController;
        private String planName;

        public GetPlanTask(String email, ProgressCalendarActivity activity, String planName) {
            super();
            mEmail = email;
            mActivity = activity;
            this.planName = planName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            dbController = new DBController();
            // get plan
            Plan mPlan = dbController.getPlan(mEmail, planName);

            // getActivities
            List<Object> allActivities = dbController.getAllActivity(mEmail);
            List<Activity> mActivities = new ArrayList<>();
            Footstep mFootstep = null;
            for(Object o: allActivities) {
                HashMap<String, Object> map = (HashMap<String, Object>) o;
                if(map.get("name").equals("footsteps")) {
                    Footstep footstep = new Footstep();
                    footstep.name = "footsteps";
                    footstep.date = (Timestamp) map.get("date");
                    footstep.value = (Long) map.get("value");
                    String currDate = sdf.format(footstep.date.toDate());
                    if (!currDate.equals(planName)) {
                        continue;
                    }
                    mFootstep = footstep;
                }
                else {
                    Activity activity = new Activity();

                    activity.name = (String) map.get("name");
                    activity.start = (Timestamp)map.get("start");
                    activity.end = (Timestamp)map.get("end");
                    String currDate = sdf.format(activity.start.toDate());
                    if (!currDate.equals(planName)) {
                        continue;
                    }
                    mActivities.add(activity);
                }
            }

            mActivity.setMyPlans(mPlan);
            mActivity.setMyActivities(mActivities);
            mActivity.setMyFootsteps(mFootstep);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                mActivity.displayPlan(planName);
                return;
            } else {
                return;
            }
        }
    }



}
