package com.android.rss.sqlite;

class Constants {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "TrabajadorDB.db";

    static final String TABLE_NAME = "Trabajador";

    static final String COLUMN_CI = "ci";
    static final String COLUMN_NAME = "nombre";
    static final String COLUMN_JOB = "cargo";
    static final String TEXT_TYPE = " TEXT";
    static final String COMMA_SEP = ",";

    static final String SQL_CREATE_TABLA =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CI + TEXT_TYPE + " PRIMARY KEY,"
                    + COLUMN_NAME + TEXT_TYPE + COMMA_SEP + COLUMN_JOB + TEXT_TYPE + " )";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
