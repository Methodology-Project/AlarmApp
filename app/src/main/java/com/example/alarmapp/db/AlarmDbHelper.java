package com.example.alarmapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDbHelper extends SQLiteOpenHelper {
    public AlarmDbHelper(Context context) {
        super(context, Alarm.DB_NAME, null, Alarm.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Alarm.AlarmEntry.TABLE + " ( " +
                Alarm.AlarmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Alarm.AlarmEntry.COL_ALARM_TITLE + " TEXT NOT NULL, " +
                Alarm.AlarmEntry.COL_ALARM_TIME + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Alarm.AlarmEntry.TABLE);

        onCreate(db);
    }


}
