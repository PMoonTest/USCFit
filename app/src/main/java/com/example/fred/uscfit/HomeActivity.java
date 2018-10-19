package com.example.fred.uscfit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class HomeActivity extends AppCompatActivity implements SensorEventListener{

    private TextView mWelcomeMessage;
    private String mEmail;
    private Button footStepBtn;
    private SensorManager sensorManager;
    private String mStepNum;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mWelcomeMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mWelcomeMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mWelcomeMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initialize welcome message and menus
        mEmail = getIntent().getStringExtra("email");
        mWelcomeMessage = findViewById(R.id.welcomeMessage);
        String welcomeMessage = "Welcome " + mEmail;
        mWelcomeMessage.setText(welcomeMessage);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
}
