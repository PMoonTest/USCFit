package com.example.fred.uscfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.example.Sport;

import java.util.ArrayList;

public class AddSportActivity extends AppCompatActivity {

    private Button addSportBtn;
    private EditText sportNameInput;
    private EditText caloriesInput;
    private String sportName = "";
    private int calories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sport);

        Intent intent = getIntent();

        sportNameInput = (EditText) findViewById(R.id.sportNameText);
        caloriesInput = (EditText) findViewById(R.id.caloriesText);
        addSportBtn = (Button) findViewById(R.id.addSportBtn);
        addSportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sportNameInput.getText().toString().length() == 0) {
                    // empty sport name input
                    return;
                }
                if(caloriesInput.getText().toString().length() == 0) {
                    // empty calories input
                    return;
                }
                if(sportNameExists(sportName)) {
                    // sport name already exists in database

                }
                // insert to database
                calories = Integer.parseInt(caloriesInput.getText().toString());
                sportName = sportNameInput.getText().toString();
                // call DBController to insert sport

            }
        });

    }

    // check if the sport name already exists in database
    boolean sportNameExists(String sportName) {
        // query database
        ArrayList<Sport> sports = null;
        for(Sport sport : sports) {
            if(sport.name.equals(sportName)) {
                return true;
            }
        }
        return false;
    }
}
