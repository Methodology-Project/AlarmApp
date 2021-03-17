package com.example.alarmapp;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    AlarmManager.AlarmClockInfo alarm_info;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;

    Calendar calendar;
    Calendar currentTime;
    Button alarm_on;
    Intent myIntent;

    EditText debug;

    PendingIntent pending_intent; //will allow alarm to work even when app is closed
    MediaPlayer ring;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.timePicker);

        calendar = Calendar.getInstance();
        alarm_on = findViewById(R.id.alarm_on);
        myIntent = new Intent(this, Receiver.class);

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());

                myIntent.putExtra("alarm_on_extra", "alarm on");
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                myIntent.setAction("alarm on");
               alarm_info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pending_intent);

                alarm_manager.setAlarmClock(alarm_info, pending_intent);
                minute = calendar.get(Calendar.MINUTE);
                String minString = (minute < 10 ? "0" : "") + minute;
                Snackbar.make(v, "Alarm set to " + calendar.get(Calendar.HOUR) + ":" + minString, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });}

        public void onHandleIntent(Intent myIntent) {
            String action = myIntent.getAction();
            if (action.equals("alarm on")) {
                ring = MediaPlayer.create(MainActivity.this, R.raw.alarm_ring);
                ring.start();  //this starts the ringing of the alarm
            }
        }



        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Alarm set", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}