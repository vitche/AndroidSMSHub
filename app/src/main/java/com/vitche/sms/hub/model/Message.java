package com.vitche.sms.hub.model;

/**
 * Created by Burmaka V on 24.11.2016.
 */
public class Message {
    private int id;
    private String body;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
