package com.ujujzk.trycursorloader.app;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseDriver {

    private static final String TAG = "DataBaseDriverTAG";

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


    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }


    public void addRec(String txt) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TXT, txt);
        mDB.insert(DB_TABLE, null, cv);
    }


    public void delRec(long id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
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
//            for (int i = 1; i < 300000; i++) {
//                cv.put(COLUMN_TXT, "000" + i);
//                db.insert(DB_TABLE, null, cv);
//                if (i % 1000 == 0){
//                    Log.d(TAG, "" + i);
//                }
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
