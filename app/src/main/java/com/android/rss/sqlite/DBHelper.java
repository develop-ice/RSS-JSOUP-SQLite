package com.android.rss.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.android.rss.sqlite.Constants.DATABASE_NAME;
import static com.android.rss.sqlite.Constants.DATABASE_VERSION;
import static com.android.rss.sqlite.Constants.SQL_CREATE_TABLA;
import static com.android.rss.sqlite.Constants.SQL_DELETE_ENTRIES;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
