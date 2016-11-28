package com.vitche.sms.hub.controller.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.vitche.sms.hub.model.Message;
import com.vitche.sms.hub.model.PhoneNumberDataSource;
import com.vitche.sms.hub.model.Source;

import java.util.ArrayList;

/**
 * Created by Burmaka V on 24.11.2016.
 */
public class MessageDB extends MainAppDB {
    private static final String TAG = "myLogs";

    public MessageDB(Context ctx) {
        super(ctx);
    }

    private static String createMessagesTableQuery(String sourceUID) {
        return "create table " + tableName(sourceUID) + " (" +
                ID + " integer primary key, " +
                BODY + " text " +
                ");";
    }

    private static String tableName(String sourceUID){
        return "table" + sourceUID;
    }

    public static void createMessagesTable(Context ctx, String sourceUID) {
        exequteAnySingeQuery(ctx, createMessagesTableQuery(sourceUID));
    }

    public static boolean dropMessagesTable(Context ctx, String sourceUID) {
        return exequteAnySingeQuery(ctx, "DROP TABLE IF EXISTS " + tableName(sourceUID));
    }

    public static long insertMessage(Context context, String phoneNumber, long timeStamp, String msgBody) {
        long result = -1;

        ContentValues cv = new ContentValues();
        cv.put(ID, timeStamp);
        cv.put(BODY, msgBody);

        MainAppDB db = new MainAppDB(context);
        db.open();
        try {
            result = db.mDB.insert(tableName(phoneNumber), null, cv);
        } catch (Exception e) {
            Log.e(TAG, "------MessageDB : insertMessage " + e.getMessage());
        } finally {
            db.close();
        }
        return result;
    }

    private static void deleteMessage(Context ctx, String soureceUID, int id) {
//        TODO test
        MainAppDB db = new MainAppDB(ctx);
        db.open();

        try {
             db.mDB.rawQuery("delete from " + tableName(soureceUID) + " where " + ID + "='" + id + "'" , null);
              Log.d(TAG, "------MessageDB : deleteMessage: " );
        } catch (Exception e) {
            Log.e(TAG, "------MessageDB : deleteMessage: " + e.getMessage());
        }
        db.close();
    }

    public static ArrayList<Message> getAllMessagesInBackOrder(Context ctx, String telNumber) {
//        TODO test

        MainAppDB db = new MainAppDB(ctx);
        db.open();
        Cursor cursor = null;
        try {
            cursor = db.mDB.query(tableName(telNumber), null, null, null, null, null, null);
            if (cursor == null) {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            db.close();
            return null;
        }
        ArrayList<Message> messages = new ArrayList<Message>();

//        if (cursor.moveToFirst()) {
        if (cursor.moveToLast()) {
            int idColIndex = cursor.getColumnIndex(ID);
            int bodyColIndex = cursor.getColumnIndex(BODY);
            do {
                Message message = new Message();
                message.setId(cursor.getLong(idColIndex));
                message.setBody(cursor.getString(bodyColIndex));
                messages.add(message);
//            }while (cursor.moveToNext());
            }while (cursor.moveToPrevious());
            cursor.close();
        }
        db.close();
        return messages;
    }
}
