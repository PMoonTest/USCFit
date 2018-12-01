package com.example.fred.uscfit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.Sport;
import com.example.User;
import com.example.db.DBController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class AddSportActivity extends AppCompatActivity {

    private Button addSportBtn = null;
    private EditText sportNameInput = null;
    private EditText caloriesInput = null;
    private String sportName = "";
    private int calories = 0;
    private DBController dbController = null;
    private GetAllSportsTask mGetAllSportsTask = null;
    private Button autoCalculateBtn = null;
    private AutoCalcTask mAutoCalcTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sport);

        final Intent intent = getIntent();
        dbController = new DBController();
        sportNameInput = (EditText) findViewById(R.id.sportNameText);
        caloriesInput = (EditText) findViewById(R.id.caloriesText);
        addSportBtn = (Button) findViewById(R.id.addSportBtn);
        autoCalculateBtn = (Button) findViewById(R.id.autoCalculateBtn);

        addSportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if sport name is empty
                if(sportNameInput.getText().toString().length() == 0) {
                    alert("Oops...", "Please enter a sport category.");
                    return;
                } else {
                    sportName = sportNameInput.getText().toString();
                }

                // check if calories is empty
                if(caloriesInput.getText().toString().length() == 0) {
                    alert("Oops...", "Please enter the calories consumption rate.");
                    return;
                } else {
                    calories = Integer.parseInt(caloriesInput.getText().toString());
                }

                // Async task check if sport category already exists in database
                mGetAllSportsTask = new GetAllSportsTask(getIntent().getStringExtra("email"), sportName, calories);
                mGetAllSportsTask.execute((Void) null);

                // clear input to wait for next input
                sportNameInput.setText("");
                caloriesInput.setText("");
            }
        });

        autoCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if sport name is empty
                if(sportNameInput.getText().toString().length() == 0) {
                    alert("Oops...", "Please enter a sport category.");
                    return;
                } else {
                    sportName = sportNameInput.getText().toString();
                }

                // start asynchronous task that is responsible for auto calculation
                mAutoCalcTask = new AutoCalcTask(getIntent().getStringExtra("email"), sportName);
                mAutoCalcTask.execute((Void) null);
            }
        });
    }

    // generate alert box with customized title and message
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

    // generate alert box displaying auto-calculation info
    private void alertAutoCalc(String title, double height, double weight, int age) {

        // generate calculation equation string as the msg of this alert box
        final int result = autoCalculateCalories(height, weight, age);
        String msg = "BMR = 6.25 * height(" + Double.toString(height) + "cm)\n" +
                "           + 10 * weight(" + Double.toString(weight) + "kg)\n" +
                "            - 5 * age(" + Integer.toString(age) + ")\n" +
                "          = " +
                Integer.toString(result) +
                " calories per hour";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update the calories input in the UI
                        updateCalories(result);
                        return;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // calculate calories consumption rate based on user info (height, weight, age, etc)
    private int autoCalculateCalories(double height, double weight, int age) {
        Double result = 6.25 * height + 10 * weight - 5 * age;
        return result.intValue();
    }

    // update the calories consumption rate
    private void updateCalories(int calories) {
        this.caloriesInput.setText(Integer.toString(calories));
    }

    public class AutoCalcTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mSportName;
        private double height = 0.0;
        private double weight = 0.0;
        private int age = 0;

        AutoCalcTask(String email, String sportName) {
            mEmail = email;
            mSportName = sportName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = dbController.getPersonalInfo(mEmail);
            this.height = user.height;
            this.weight = user.weight;
            this.age = user.age;
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                alertAutoCalc("Auto-generate calories rate for " + mSportName, this.height, this.weight, this.age);
            }
        }
    }

    public class GetAllSportsTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mSportName;
        private final int mCalories;

        GetAllSportsTask(String email, String sportName, int calories) {
            mEmail = email;
            mSportName = sportName;
            mCalories = calories;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // check if the sport already exists
            List<Sport> mySports = dbController.getAllSports(mEmail);
            if(mySports == null || mySports.size() == 0) return false;
            for(Sport mySport : mySports) {
                if(mySport.name.equals(mSportName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                // this means sport already exists
                alert("Oops...", "Sport category already exists.");
                return;
            } else {
                // sport doesn't exist
                // all input is valid, now call DBController to insert sport
                dbController.addSports(getIntent().getStringExtra("email"), new Sport(sportName, calories));
                alert("Yeah!", "Successfully added sport category.");
                return;
            }
        }

    }

    public static void addDefaultSports(String my_email) {
//        TestTask testTask = new TestTask();
//        testTask.execute((Void) null);
        final String email = my_email;
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    URL oracle = new URL("https://firebasestorage.googleapis.com/v0/b/uscfit-d58d6.appspot.com/o/testCalories.csv?alt=media&token=63b5c869-9597-4c01-9afa-86a548a20324");
                    BufferedReader br = new BufferedReader(new InputStreamReader(oracle.openStream()));
                    String line;
                    DBController temp = new DBController();
                    while ((line = br.readLine()) != null) {
                        String sportName = line.split(",")[0];
                        int calories = Integer.parseInt(line.split(",")[1]);
                        temp.addSports(email, new Sport(sportName, calories));
                    }
                    br.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

//    public class TestTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                URL oracle = new URL("https://firebasestorage.googleapis.com/v0/b/uscfit-d58d6.appspot.com/o/testCalories.csv?alt=media&token=63b5c869-9597-4c01-9afa-86a548a20324");
//                BufferedReader br = new BufferedReader(new InputStreamReader(oracle.openStream()));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String sportName = line.split(",")[0];
//                    int calories = Integer.parseInt(line.split(",")[1]);
//                    dbController.addSports(getIntent().getStringExtra("email"), new Sport(sportName, calories));
//                }
//                br.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//
//        }
//    }

}
