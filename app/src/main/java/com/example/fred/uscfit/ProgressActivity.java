package com.example.fred.uscfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.Activity;
import com.example.Footstep;
import com.example.Plan;
import com.example.db.DBController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = "Progress";
    private DBController dbController = null;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");
        dbController = new DBController();


        GetAllPlansTask mGetAllPlansTask = new GetAllPlansTask((mEmail));
        mGetAllPlansTask.execute((Void) null);
    }

    public void updateFootstepBar() {

    }

    private void alert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class GetAllPlansTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;

        public GetAllPlansTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<Plan> myPlans = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
            for(int i = 0; i<7; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String planName = sdf.format(calendar.getTime());
                Plan currPlan = dbController.getPlan(mEmail, planName);
                myPlans.add(currPlan);
            }
            Log.d(TAG, "doInBackground: " + myPlans.size());

            List<Object> allActivities = dbController.getAllActivity(mEmail);
            List<Activity> myActivities = new ArrayList<>();
            Footstep myFootstep = null;
            for(Object o: allActivities) {
                if(o.getClass() == Activity.class) {
                    myActivities.add((Activity) o);
                }
                else {
                    myFootstep = (Footstep) o;
                }
            }


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
