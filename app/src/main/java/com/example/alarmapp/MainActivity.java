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
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;

    EditText debug;

    PendingIntent pending_intent; //will allow alarm to work even when app is closed
    MediaPlayer ring;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm_manager = (AlarmManager) getSystemService (ALARM_SERVICE);
        alarm_timepicker =  findViewById(R.id.timePicker);

        debug= findViewById(R.id.debug);

        final Calendar calendar = Calendar.getInstance ();

        final Button alarm_on = findViewById(R.id.alarm_on);

        final Intent myIntent = new Intent();

        alarm_on.setOnClickListener (new View.OnClickListener (){
//
//
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int hour = alarm_timepicker.getHour ();
                int minute = alarm_timepicker.getMinute ();
                alarm_on.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        calendar.set (Calendar.MINUTE, alarm_timepicker.getMinute ());
                        calendar.set (Calendar.HOUR_OF_DAY, alarm_timepicker.getHour ());

                        Calendar currentTime = Calendar.getInstance();

                        debug.setText("alarm is set to: "+ hour + ":" + minute); //remove later- for debugging purposes
                        if(currentTime.get(Calendar.HOUR) == hour && currentTime.get(Calendar.MINUTE) == minute){
                            ring = MediaPlayer.create(MainActivity.this,R.raw.alarm_ring);

                            ring.start();  //this starts the ringing of the alarm
                        }
                    }
                });

//                //if we want to access the time in a string
                String hour_str = String.valueOf(hour);
                String min_Str = String.valueOf(minute);
//
//                //if time is less than 10, string will look like 10:7, not 10:07
               if (minute < 10){
                   min_Str = "0" + String.valueOf (minute);
               }
//                //will want to tell user that time was set to whatever time, so do that here

                myIntent.putExtra("alarm_on_extra", "alarm on");
                pending_intent = PendingIntent.getBroadcast (MainActivity.this, 0,
                     myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
               alarm_manager.set (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (),
                       pending_intent);
//
            }
        });



        //CODE FOR RESONATING THE ALARM SOUND:
        //this is the sound of the alarm clock
        //ring = MediaPlayer.create(MainActivity.this,R.raw.alarm_ring);

           // ring.start();  //this starts the ringing of the alarm


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Alarm set", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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