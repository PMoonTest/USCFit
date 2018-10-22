package com.example.fred.uscfit;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
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
    private SensorManager sensorManager;
    private String mStepNum;

    private LinearLayout mAddSportLayout;
    private LinearLayout mAddPlanLayout;
    private LinearLayout mAddActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initialize welcome message and menus
        mEmail = getIntent().getStringExtra("email");
        mWelcomeMessage = findViewById(R.id.welcomeMessage);
        String welcomeMessage = "Welcome " + mEmail;
        mWelcomeMessage.setText(welcomeMessage);

        mAddSportLayout = (LinearLayout) findViewById(R.id.addSportLayout);
        mAddPlanLayout = (LinearLayout) findViewById(R.id.addPlanLayout);
        mAddActivityLayout = findViewById(R.id.addActivityLayout);
        //BottomNavigationView navigation = findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // initialize step coutner
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        footStepBtn = findViewById(R.id.footstepBtn);
        mStepNum = "0";
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
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }

        // connect add sport button to AddSportActivity
        mAddSportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddSportActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            }
        });

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
        createNotificationChannel();


    }

    public void checkProgress(View view) {
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra("email", mEmail);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mStepNum = String.valueOf(event.values[0]);
        //startCounterBtn.setText(String.valueOf(event.values[0]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private long getRandomStep(){
        float minVal = 1.0f;
        float maxVal = 99.0f;

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
