package com.example.alarmapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alarmapp.db.Alarm;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.SEND_SMS;

public class RingActivity extends AppCompatActivity {

    EditText snooze;
    MediaPlayer ring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        ring = MediaPlayer.create(getApplicationContext(), R.raw.alarm_ring);

        ring.start();  //this starts the ringing of the alarm
        snooze=findViewById(R.id.snoozeAmnt);

    }


    public void sendTextMessage(String phoneNumber, String message, View view) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Snackbar.make(view, "User snoozed for !" + snooze + "minutes", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pressSnooze(View view) {
        ring.stop();
        if (getApplicationContext().checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendTextMessage("5554", "User pressed the snooze button!", view);
        }

        Intent alarmIntent = new Intent(this, Receiver.class);
        Calendar calendar = Calendar.getInstance();
        //get snooze time entered and snooze based on that amount
        Date nextAlarm = new Date(System.currentTimeMillis() + (Integer.parseInt(snooze.getText().toString())*6000));
        calendar.setTime(nextAlarm);

        alarmIntent.putExtra("alarm_on_extra", "alarm on");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setAction("alarm on");

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent);
        am.setAlarmClock(info, pendingIntent);

        int minute = calendar.get(Calendar.MINUTE);
        String minString = (minute < 10 ? "0" : "") + minute;
        Snackbar.make(view, "Alarm set to " + calendar.get(Calendar.HOUR) + ":" + minString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);


    }

    public void pressStop(View view) {
        ring.stop();
        Intent backToMain = new Intent(this, MainActivity.class);
       startActivity(backToMain);

    }
}