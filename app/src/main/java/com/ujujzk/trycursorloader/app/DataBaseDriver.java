package com.ujujzk.trycursorloader.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.support.v4.util.Pair;

import java.util.Random;

public class DataBaseDriver {

    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "mytab";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TXT = "txt";

    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TXT + " text" +
                    ");";

    private final Context mContext;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;


    public DataBaseDriver(Context ctx) {
        mContext = ctx;
    }


    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }


    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }


    public Pair<Cursor,Integer> getLimitedDataByQueryWord(String queryWord, int limitNum) {
        String selection = COLUMN_TXT + " LIKE '" + queryWord + "%'";
        String limit = "" + limitNum;

        return new Pair<Cursor,Integer>( mDB.query(DB_TABLE, null, selection, null, null, null, COLUMN_TXT, limit),
                getLimitedDataByQueryWordCount(queryWord, limitNum));

    }

    private int getLimitedDataByQueryWordCount (String queryWord, int limitNum) {
        int count;
        String countQuery = "select count(*) from " + DB_TABLE + " WHERE " + COLUMN_TXT + " LIKE '" + queryWord + "%'";
        Cursor c = mDB.rawQuery(countQuery, null);
        c.moveToFirst();
        count = c.getInt(0);
        if (count > limitNum) {
            return limitNum;
        } else if (count <= 0) {
            return 0;
        } else {
            return count;
        }
    }



    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

//            ContentValues cv = new ContentValues();
//            for (int i = 1; i < 200000; i++) {
//                int num = (int)(Math.random()*1000000);
//                cv.put(COLUMN_TXT, "" + num);
//                db.insert(DB_TABLE, null, cv);
//                if (i % 1000 == 0){
//                    Log.d("TAG", "" + i);
//                }
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
