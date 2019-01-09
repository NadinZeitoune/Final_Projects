package com.project.nadin.androidproject_rides_clientside_app;

public class Message {
    private String senderFullName;
    private String time;
    private String context;

    public Message(String senderFullName, String time, String context) {
        this.senderFullName = senderFullName;
        this.time = time;
        this.context = context;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public String getTime() {
        return time;
    }

    public String getContext() {
        return context;
    }
}
