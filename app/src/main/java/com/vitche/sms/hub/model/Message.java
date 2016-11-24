package com.vitche.sms.hub.model;

/**
 * Created by Burmaka V on 24.11.2016.
 */
public class Message {
    private long id;
    private String body;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
