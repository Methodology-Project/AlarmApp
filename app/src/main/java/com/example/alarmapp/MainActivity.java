package com.example.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Random;

import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    AlarmManager.AlarmClockInfo alarm_info;
    TimePicker alarm_timepicker;
    CheckBox recurring, sun, mon, tue, wed, thu, fri, sat;
    int numSnoozes;
    Calendar calendar;
    Button alarm_on;
    Intent myIntent;
    SharedPreferences preferences;

    PendingIntent pending_intent; //will allow alarm to work even when app is closed

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // check if app has permission to send sms


        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.timePicker);

        calendar = Calendar.getInstance();
        alarm_on = findViewById(R.id.alarm_on);
        myIntent = new Intent(this, Receiver.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        alarm_on.setOnClickListener((View.OnClickListener) v -> {
            if (getApplicationContext().checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                if (preferences.getBoolean("send_sms", true)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_window, null);

                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = 1000;

                    boolean focusable = false;

                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    Button allowSMS = (Button) popupView.findViewById(R.id.allowSMS);
                    Button denySMS = (Button) popupView.findViewById(R.id.denySMS);

                    allowSMS.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{SEND_SMS}, 0);
                            popupWindow.dismiss();
                        }
                    });

                    denySMS.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                }
            }

            //method that deals with alarm scheduling
            scheduleAlarm(v);
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void scheduleAlarm(View v) {

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
        calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        numSnoozes  = 0;
        recurring = findViewById(R.id.fragment_createalarm_recurring);


        if (recurring.isChecked()) {
            setRecurringAlarm(v);
        }
        else {
            sun = findViewById(R.id.fragment_createalarm_checkSun);
            mon = findViewById(R.id.fragment_createalarm_checkMon);
            tue = findViewById(R.id.fragment_createalarm_checkTue);
            wed = findViewById(R.id.fragment_createalarm_checkWed);
            thu = findViewById(R.id.fragment_createalarm_checkThu);
            fri = findViewById(R.id.fragment_createalarm_checkFri);
            sat = findViewById(R.id.fragment_createalarm_checkSat);

            boolean atLeastOne = false;
            String daysStr = "";
            if (sun.isChecked()) {
                setAlarmForDayOfWeek(7, v);
                atLeastOne = true;
                daysStr += "Sun ";
            }
            if (mon.isChecked()) {
                setAlarmForDayOfWeek(1, v);
                atLeastOne = true;
                daysStr += "Mon ";
            }
            if (tue.isChecked()) {
                setAlarmForDayOfWeek(2, v);
                atLeastOne = true;
                daysStr += "Tue ";
            }
            if (wed.isChecked()) {
                setAlarmForDayOfWeek(3, v);
                atLeastOne = true;
                daysStr += "Wed ";
            }
            if (thu.isChecked()) {
                setAlarmForDayOfWeek(4, v);
                atLeastOne = true;
                daysStr += "Thurs ";
            }
            if (fri.isChecked()) {
                setAlarmForDayOfWeek(5, v);
                atLeastOne = true;
                daysStr += "Fri ";
            }
            if (sat.isChecked()) {
                setAlarmForDayOfWeek(6, v);
                atLeastOne = true;
                daysStr += "Sat ";
            }
            if(atLeastOne){
                int minute = calendar.get(Calendar.MINUTE);


                String minString = (minute < 10 ? "0" : "") + minute;
                Snackbar.make(v, "Alarm set to " + calendar.get(Calendar.HOUR) + ":" + minString  + " for "+ daysStr,  Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            else{
                setJustOneAlarm(v);
            }


        }
    }

    public void setRecurringAlarm(View v){
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        myIntent.putExtra("alarm_on_extra", "alarm on");
        myIntent.putExtra("number_of_snoozes", 0);
        pending_intent = PendingIntent.getBroadcast(MainActivity.this, alarmId,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myIntent.setAction("alarm on");
        alarm_info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pending_intent);

        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending_intent);

        int minute = calendar.get(Calendar.MINUTE);
        String minString = (minute < 10 ? "0" : "") + minute;
        Snackbar.make(v, "Daily Alarm set to " + calendar.get(Calendar.HOUR) + ":" + minString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }


    private void setAlarmForDayOfWeek(int dayOfWeek, View v) {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Calendar calDayOfWeek = (Calendar) calendar.clone();
        calDayOfWeek.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE,7);
        }
        myIntent.putExtra("alarm_on_extra", "alarm on");
        myIntent.putExtra("number_of_snoozes", 0);
        pending_intent = PendingIntent.getBroadcast(MainActivity.this, alarmId,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myIntent.setAction("alarm on");
        alarm_info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pending_intent);

        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (DateUtils.DAY_IN_MILLIS) * 7, pending_intent);


    }

    private void setJustOneAlarm(View v){
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);


        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        myIntent.putExtra("alarm_on_extra", "alarm on");
        myIntent.putExtra("number_of_snoozes", 0);
        pending_intent = PendingIntent.getBroadcast(MainActivity.this, alarmId,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myIntent.setAction("alarm on");
        alarm_info = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pending_intent);


        alarm_manager.setAlarmClock(alarm_info, pending_intent);
        int minute = calendar.get(Calendar.MINUTE);
        String minString = (minute < 10 ? "0" : "") + minute;
        Snackbar.make(v, "Alarm set to " + calendar.get(Calendar.HOUR) + ":" + minString, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();



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

