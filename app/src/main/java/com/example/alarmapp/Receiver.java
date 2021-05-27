package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Receiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


                Intent ringIntent = new Intent(context, RingActivity.class);
                ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ringIntent.putExtra("number_of_snoozes", intent.getIntExtra("number_of_snoozes", -1));
                context.startActivity(ringIntent);

        }



}
