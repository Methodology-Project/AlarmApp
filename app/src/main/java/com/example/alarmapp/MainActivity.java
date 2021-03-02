package com.example.alarmapp;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    PendingIntent pending_intent; //will allow alarm to work even when app is closed
    MediaPlayer ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        alarm_manager = (AlarmManager) getSystemService (ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById (R.id.timePicker);


        final Calendar calendar = Calendar.getInstance ();

        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        // create an onClick listener to start the alarm
//this is code that will set the alarm manager to keep track of the time, but we first need to add an Intent here that is set correctly for this method to work
       /* alarm_on.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)//since the gethour and getminute require a larger API
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set (Calendar.MINUTE, alarm_timepicker.getMinute ());

                //set the alarm manager, that will let the alarm ring
    //                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (),ADD INTENT HERE)

            }
                                        });*/


        //CODE FOR RESONATING THE ALARM SOUND:
        //this is the sound of the alarm clock
        ring = MediaPlayer.create(MainActivity.this,R.raw.alarm_ring);
        ring.start();  //this starts the ringing of the alarm


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}