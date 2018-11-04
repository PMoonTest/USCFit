package com.example.fred.uscfit;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.db.DBController;

public class UpdateInfoActivity extends AppCompatActivity {

    private DBController dbController = null;
    private Button mSubmitUpdateBtn = null;
    private String mEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        // initialize dbController, btn, email
        this.dbController = new DBController();
        this.mSubmitUpdateBtn = (Button) findViewById(R.id.submitUpdateBtn);
        this.mEmail = getIntent().getStringExtra("email");

        this.mSubmitUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all the inputs
                String ageInput = ((EditText) findViewById(R.id.ageText)).getText().toString();
                String heightInput = ((EditText) findViewById(R.id.heightText)).getText().toString();
                String weightInput = ((EditText) findViewById(R.id.weightText)).getText().toString();

                if(ageInput.length() == 0 || heightInput.length() == 0 || weightInput.length() == 0) {
                    // make sure all fields have input
                    alert("Oops", "Please enter all fields.");
                    return;
                } else if(ageInput.contains(".")) {
                    // check if age contains "."
                    alert("Oops", "Please enter a valid age (Integer).");
                    return;
                } else {
                    // call dbController to update personal info
                    int age = Integer.parseInt(ageInput);
                    double height = Double.parseDouble(heightInput);
                    double weight = Double.parseDouble(weightInput);

                    dbController.setPersonalInfo(mEmail, height, weight, age);
                    alert("Yeah", "Successfully updated personal info");

                    // clear input fields
                    ((EditText) findViewById(R.id.ageText)).setText("");
                    ((EditText) findViewById(R.id.heightText)).setText("");
                    ((EditText) findViewById(R.id.weightText)).setText("");
                }
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

}
