package com.example.alarmapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.SEND_SMS;

public class RingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
    }


    public void sendTextMessage(String phoneNumber, String message, View view) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Snackbar.make(view, "Text message sent!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pressSnooze(View view) {

        if (getApplicationContext().checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendTextMessage("5554", "User pressed the snooze button!", view);
        }

        Intent alarmIntent = new Intent(this, Receiver.class);
        Calendar calendar = Calendar.getInstance();
        Date nextAlarm = new Date(System.currentTimeMillis() + 30000);
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
        Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);
    }
}