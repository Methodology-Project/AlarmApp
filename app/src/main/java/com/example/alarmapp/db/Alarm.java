package com.example.alarmapp.db;

import android.provider.BaseColumns;

import java.time.LocalTime;

public class Alarm {
    public static final String DB_NAME = "com.example.alarmapp.db";
    public static final int DB_VERSION = 1;

    public class AlarmEntry implements BaseColumns {
        public static final String TABLE = "alarms";
        public static final String COL_ALARM_TITLE = "title";
        public static final String COL_ALARM_TIME = "time";

    }
}
