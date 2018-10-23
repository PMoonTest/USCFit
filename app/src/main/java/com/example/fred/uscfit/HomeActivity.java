package com.example.fred.uscfit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class HomeActivity extends AppCompatActivity implements SensorEventListener{

    private TextView mWelcomeMessage;
    private String mEmail;
    private Button footStepBtn;
    private String mStepNum;

    private LinearLayout mAddSportLayout;
    private LinearLayout mAddPlanLayout;
    private LinearLayout mAddActivityLayout;
    private LinearLayout mCheckProgress;

    private Button mProfileBtn;

    private SensorManager mSensorManager;
    private boolean hasStepcounterSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initialize welcome message and menus
        mEmail = getIntent().getStringExtra("email");

        mWelcomeMessage = findViewById(R.id.welcomeMessage);
        String welcomeMessage = "Welcome " + mEmail.split("@")[0];
        mWelcomeMessage.setText(welcomeMessage);

        mAddSportLayout = findViewById(R.id.addSportLayout);
        mAddPlanLayout = findViewById(R.id.addPlanLayout);
        mAddActivityLayout = findViewById(R.id.addActivityLayout);
        mCheckProgress = findViewById(R.id.progressLayout);
        //BottomNavigationView navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        mProfileBtn = findViewById(R.id.profile_btn);
        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });


        // initialize step coutner
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER) && packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            hasStepcounterSensor = true;
        } else {
            Toast.makeText(this, "Step counter sensor not found", Toast.LENGTH_SHORT).show();
            hasStepcounterSensor = false;
        }
        footStepBtn = findViewById(R.id.footstepBtn);
        mStepNum = "0";
        // initialize step coutner
        if(!hasStepcounterSensor) {
            footStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button)v;
                    String text = b.getText().toString();
                    if(text.equals(getString(R.string.footstep_btn_text))){
                        mStepNum = String.valueOf(Long.parseLong(mStepNum)+getRandomStep());
                        b.setText(mStepNum);
                    }
                    else{
                        b.setText(getString(R.string.footstep_btn_text));
                    }
                }
            });
        }
        else {
            Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                mSensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
            }
        }

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    v.setBackgroundColor(Color.parseColor("#87b5ff"));
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setBackgroundColor(Color.parseColor("#3a85ff"));
                }
                return false;
            }
        };

        // connect add sport button to AddSportActivity
        mAddSportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddSportActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });
        mAddSportLayout.setOnTouchListener(onTouchListener);
        mAddActivityLayout.setOnTouchListener(onTouchListener);
        mAddPlanLayout.setOnTouchListener(onTouchListener);
        mCheckProgress.setOnTouchListener(onTouchListener);

        mAddPlanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlanActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });

        mAddActivityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });

        mCheckProgress.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProgressActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });
        createNotificationChannel();


    }

    public void checkProgress(View view) {
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra("email", mEmail);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                footStepBtn = findViewById(R.id.footstepBtn);
                footStepBtn.setText(String.valueOf(sensorEvent.values[0]));
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private long getRandomStep(){
        float minVal = 1.0f;
        float maxVal = 20.0f;
        Random rand = new Random();

        return (long)(rand.nextFloat() * (maxVal - minVal) + minVal);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "my channel";
            String description = "this is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("myid", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
