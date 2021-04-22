package com.example.alarmapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.Manifest.permission.SEND_SMS;
import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    AlarmManager.AlarmClockInfo alarm_info;
    TimePicker alarm_timepicker;
    Context context;

    Calendar calendar;
    Button alarm_on;
    Intent myIntent;

    PendingIntent pending_intent; //will allow alarm to work even when app is closed
    MediaPlayer ring;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if app has permission to send sms
        //TODO

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = findViewById(R.id.timePicker);

        calendar = Calendar.getInstance();
        alarm_on = findViewById(R.id.alarm_on);
        myIntent = new Intent(this, Receiver.class);

        alarm_on.setOnClickListener((View.OnClickListener) v -> {
            if (getApplicationContext().checkSelfPermission(SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(SEND_SMS)) {
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
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {SEND_SMS}, 0);
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
        });}

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