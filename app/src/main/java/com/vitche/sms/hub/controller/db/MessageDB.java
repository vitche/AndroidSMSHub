package com.vitche.sms.hub.controller.db;

import android.content.Context;

import com.vitche.sms.hub.model.Message;

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
                BODY + " text ," +
                DATE + " text " +
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

    public static void insertMessage(Context ctx, String sourceUID, int id, String body, String date){
//        TODO
    }
    private static void deleteMessage(Context ctx, String soureceUID, int id) {
//    TODO
//        EventsDB db = new EventsDB(ctx);
//        db.open();
//        int delCount = db.mDB.delete(DB_TABLE, UID + " = " + "\"" +uid + "\"", null);
////        db.logAllData();
//        db.close();
    }

    public static ArrayList<Message> getAllMessages(String telNumber) {
//        TODO
        ArrayList<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setId(i);
            message.setDate("test date " + i);
            message.setBody("test body " + i);
            messages.add(message);
        }
        return messages;
    }
}
