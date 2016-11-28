package com.vitche.sms.hub.controller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.PhoneNumberDataSource;
import com.vitche.sms.hub.model.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Burmaka V on 24.11.2016.
 */
public class SourceDB extends MainAppDB {
    private static final String TAG = "myLogs";

    public SourceDB(Context ctx) {
        super(ctx);
    }

    public static long createSource(Context ctx, String sourceUID, String decription) {
        ContentValues cv = new ContentValues();
        cv.put(UID, sourceUID);
        cv.put(DESCRIPTION, decription);
        MessageDB.createMessagesTable(ctx, sourceUID);
        return isertDataInTable(ctx, SOURCES_TABLE, cv);
    }


    public static boolean deleteSource(Context ctx, String sourceUID) {
        MainAppDB db = new MainAppDB(ctx);
        db.open();
        try {
            int delCount = db.mDB.delete(SOURCES_TABLE, UID + " = " + "\"" + sourceUID + "\"", null);
            Log.d(TAG, "------MainAppDB : deleteSource: " + delCount);
        } catch (Exception e) {
            Log.e(TAG, "------MainAppDB : deleteSource: " + e.getMessage());
        }
        db.close();
        return MessageDB.dropMessagesTable(ctx, sourceUID);
    }

    public static List<PhoneNumberDataSource> getAllSorces(Context ctx){
        MainAppDB db = new MainAppDB(ctx);
        db.open();
        Cursor cursor = null;
        try {
            cursor = db.mDB.query(SOURCES_TABLE, null, null, null, null, null, null);
            if (cursor == null) {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            db.close();
            return null;
        }
        ArrayList<PhoneNumberDataSource> sources = new ArrayList<PhoneNumberDataSource>();

        if (cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex(UID);
            int descriptionColIndex = cursor.getColumnIndex(DESCRIPTION);
            do {
                Source source = new Source();
                source.setPhoneNumber(cursor.getString(idColIndex));
                source.setDecription(cursor.getString(descriptionColIndex));
                source.setMessages(MessageDB.getAllMessagesInBackOrder(ctx, source.getPhoneNumber()));
                sources.add(source);
            }while (cursor.moveToNext());

            cursor.close();
        }
        db.close();

        return sources;
    }

    public static List<PhoneNumberDataSource> getAllSorcesSorted(Context ctx){
        List<PhoneNumberDataSource> sources = getAllSorces(ctx);
        Collections.sort(sources);
        return sources;
    }


    public static Source getSourceInfo(Context ctx, String sourceId) {
        Source source = new Source();
        MainAppDB db = new MainAppDB(ctx);
        db.open();
        Cursor cursor = null;
        try {
            cursor =  db.mDB.rawQuery("select * from " + SOURCES_TABLE + " where " + UID + "='" + sourceId + "'" , null);
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    source.setPhoneNumber(cursor.getString(cursor.getColumnIndex(UID)));
                    source.setDecription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                    source.setMessages(MessageDB.getAllMessagesInBackOrder(ctx, sourceId));
                }
                cursor.close();
            }

            Log.d(TAG, "------MainAppDB : getSourceInfo: " );
        } catch (Exception e) {
            Log.e(TAG, "------MainAppDB : getSourceInfo: " + e.getMessage());
        }
        db.close();
        return source;
    }
}
