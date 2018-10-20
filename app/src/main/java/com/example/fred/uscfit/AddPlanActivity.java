package com.example.fred.uscfit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AddPlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.activity_add_plan);

        // Adding new activities
        FloatingActionButton addActivityBtn = (FloatingActionButton) findViewById(R.id.addActivityBtn);
        final Context that = this;
        addActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.constraintLayout);
                Button btn = new Button(that);
                btn.setText("Send");
                btn.setId(new Integer(1));

                layout.addView(btn);
            }
        });
    }
}
