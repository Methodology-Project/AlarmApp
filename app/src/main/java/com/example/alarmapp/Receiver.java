package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;


public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                Intent ringIntent = new Intent(context, RingActivity.class);
                ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ringIntent);
        }

}
