package com.example.fred.uscfit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        System.out.println(intent1.getStringExtra("name"));
        intent1.putExtra("name",intent1.getStringExtra("name"));
        context.startService(intent1);
    }
}