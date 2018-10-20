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
import com.example.db.DBController;

import java.util.List;

public class AddSportActivity extends AppCompatActivity {

    private Button addSportBtn = null;
    private EditText sportNameInput = null;
    private EditText caloriesInput = null;
    private String sportName = "";
    private int calories = 0;
    private DBController dbController = null;
    private GetAllSportsTask mGetAllSportsTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sport);

        final Intent intent = getIntent();
        dbController = new DBController();

        addSportBtn = (Button) findViewById(R.id.addSportBtn);
        addSportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if sport name is empty
                sportNameInput = (EditText) findViewById(R.id.sportNameText);
                if(sportNameInput.getText().toString().length() == 0) {
                    alert("Oops...", "Please enter a sport category.");
                    return;
                } else {
                    sportName = sportNameInput.getText().toString();
                }

                // check if calories is empty
                caloriesInput = (EditText) findViewById(R.id.caloriesText);
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

}
