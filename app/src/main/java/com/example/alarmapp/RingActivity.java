package com.example.alarmapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.SEND_SMS;

public class RingActivity extends AppCompatActivity {



    int snooze;
    MediaPlayer ring;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_ring);
        ring = MediaPlayer.create(getApplicationContext(), R.raw.alarm_ring);

        ring.start();  //this starts the ringing of the alarm
        View inflatedView = getLayoutInflater().inflate(R.layout.content_main, null);
        snooze = Integer.parseInt(preferences.getString("snooze_amount", "1"));

    }


    public void sendTextMessage(String phoneNumber, View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        String userName = preferences.getString("username", "<user>");
        String message = userName + " has snoozed their alarm for " + snooze + " minutes.";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void pressSnooze(View view) {
        ring.stop();
        if (getApplicationContext().checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendTextMessage(preferences.getString("phone_number", "5556"),  view);
        }

        Intent alarmIntent = new Intent(this, Receiver.class);
        Calendar calendar = Calendar.getInstance();
        //get snooze time entered and snooze based on that amount
        Date nextAlarm = new Date(System.currentTimeMillis() + (snooze*60000));
        calendar.setTime(nextAlarm);

        alarmIntent.putExtra("alarm_on_extra", "alarm on");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setAction("alarm on");

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent);
        am.setAlarmClock(info, pendingIntent);



         Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);


    }

    public void pressStop(View view) {
        ring.stop();
        Intent backToMain = new Intent(this, MainActivity.class);
       startActivity(backToMain);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}