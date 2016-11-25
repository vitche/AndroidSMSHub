package com.vitche.sms.hub.controller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Burmaka V on 23.11.2016.
 */
public class MainAppDB {
    protected static final String TAG = "myLogs";
    protected static final String DB_NAME = "main_app_db";
    protected static final String SOURCES_TABLE = "sources_table";

    protected static final String ID = "_id";
    protected static final String UID = "uid";
    protected static final String DESCRIPTION = "description";

    protected static final String BODY = "body";

    protected static final int DB_VERSION = 1;
    protected Context mCtx;

    protected DBHelper mDBHelper;
    protected SQLiteDatabase mDB;


    public MainAppDB(Context ctx) {
        mCtx = ctx;
    }

    public String createSourcesTable() {
        return "create table " + SOURCES_TABLE + "(" +
                UID + " text primary key not null," +
                DESCRIPTION + " text " +
                ");";
    }

    public static boolean exequteAnySingeQuery(Context ctx, String query) {
        Log.e(TAG, "------MainAppDB : exequteAnySingeQuery: " + query);
        MainAppDB db = new MainAppDB(ctx);
        db.open();
        try {
            db.mDB.execSQL(query);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "------MainAppDB : exequteAnySingeQuery: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    protected static long isertDataInTable(Context ctx, String tableName, ContentValues cv) {
        long result = -1;
        MainAppDB db = new MainAppDB(ctx);
        db.open();
        try {
            result = db.mDB.insert(tableName, null, cv);
        } catch (Exception e) {
            Log.e(TAG, "------MainAppDB : exequteAnySingeQuery: " + e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }


    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }


    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }

    protected class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(createSourcesTable());
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//            TODO
        }
    }
}
