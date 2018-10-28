package com.example.fred.uscfit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class FootstepActivity extends AppCompatActivity {

//    private DBController dbController = null;
//    private GetFootstepTask mGetFootstepTask = null;
//    private int currFootstep = 0;
//    private Button showFootstepHistoryBtn = null;
//    private TextView footstepNumText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_footstep);
//
//        final Intent intent = getIntent();
//        dbController = new DBController();
//        showFootstepHistoryBtn = (Button) findViewById(R.id.showFootstepHistoryBtn);
//        footstepNumText = (TextView) findViewById(R.id.footstepNumText);
//
//        mGetFootstepTask = new GetFootstepTask();
//        mGetFootstepTask.execute((Void) null);


    }

    public class GetFootstepTask extends AsyncTask<Void, Void, Boolean> {
//        private final String email;

        public GetFootstepTask() {
//            email = getIntent().getStringExtra("email");
        }

        @Override
        protected Boolean doInBackground(Void... params) {

//            SimpleDateFormat sdf = new SimpleDateFormat();
//            List<Object> allActivities = dbController.getAllActivity(email);
//            Map<String, Footstep> myFootsteps = new HashMap<>();
//
//            // get all the footsteps
//            for(Object o: allActivities) {
//                HashMap<String, Object> map = (HashMap<String, Object>) o;
//                if(map.get("name").equals("footsteps")) {
//                    Footstep footstep = new Footstep();
//                    footstep.name = "footsteps";
//                    footstep.date = (Timestamp) map.get("date");
//                    footstep.value = (Long) map.get("value");
//                    String currDate = sdf.format(footstep.date.toDate());
//                    myFootsteps.put(currDate, footstep);
//                }
//
//            }
//            // check which footstep belongs to today
//            for(String key : myFootsteps.keySet()) {
//                Footstep footstep = myFootsteps.get(key);
//                if(DateUtils.isToday(footstep.date.toDate().getTime())) {
//                    // update today's footstep in UI
//                    FootstepActivity.this.footstepNumText.setText(Long.toString(footstep.value));
//                    return true;
//                }
//            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
//            mGetFootstepTask = null;
//            if(success) {
//                ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
//                pb.setVisibility(View.GONE);
//            } else {
//                alert("Oops", "You haven't walked today. Get up and go for a walk now :)");
//            }
//            return;
        }

    }

    // generate alert box with customized title and message
    private void alert(String title, String msg) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(msg)
//                .setTitle(title);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                return;
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//        }
        return super.onKeyDown(keyCode, event);
    }

}
