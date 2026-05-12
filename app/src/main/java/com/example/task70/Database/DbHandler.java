package com.example.task70.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DbHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "appdb.db";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        String itemsTable = "CREATE TABLE " + LostItem.TABLE_NAME + " ( " + LostItem.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                LostItem.COLUMN_NAME + " TEXT NOT NULL, " + LostItem.COLUMN_PHONE + " TEXT NOT NULL, " + LostItem.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                LostItem.COLUMN_LOCATION + " TEXT NOT NULL, " + LostItem.COLUMN_CATERGORY + " INTEGER NOT NULL, " + LostItem.COLUMN_IMAGE + " TEXT NOT NULL, "
                + LostItem.COLUMN_POST_TYPE + " INTEGER NOT NULL, "+ LostItem.COLUMN_DATE +" TEXT NOT NULL)";

        db.execSQL(itemsTable);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public static class LostItem implements BaseColumns {
        public static final String TABLE_NAME = "lost_item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_CATERGORY = "catergory";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_TYPE = "post_type";
        public static final String COLUMN_DATE = "date";


    }
}

