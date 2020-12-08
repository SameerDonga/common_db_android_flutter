package com.retroportalstudio.www.background_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("dogs"))
        {
            Toast.makeText(context, "Dog Count ==> "+ intent.getIntExtra("dogs",0)+": " + intent.getAction(), Toast.LENGTH_SHORT).show();

        }
        else
        Toast.makeText(context, "NO DB CONNECTED: " + intent.getAction(), Toast.LENGTH_SHORT).show();
    }
}