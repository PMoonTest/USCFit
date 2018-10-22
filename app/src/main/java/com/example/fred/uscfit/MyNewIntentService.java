package com.example.fred.uscfit;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //System.out.println("1231231231231231231");
        String sport_name =intent.getStringExtra("name");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "myid")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Notification from USCFit")
                .setContentText("You have upcoming activity in 3 hours. Check your plan!")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }



}