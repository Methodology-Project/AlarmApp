package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;


public class Receiver extends BroadcastReceiver {
    MediaPlayer ring;
        @Override
        public void onReceive(Context context, Intent intent) {

                //ring = MediaPlayer.create(context, R.raw.alarm_ring);

                //ring.start();  //this starts the ringing of the alarm

                Intent ringIntent = new Intent(context, RingActivity.class);
                ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ringIntent);
        }

}
