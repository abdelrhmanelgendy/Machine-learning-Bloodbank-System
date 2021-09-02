package com.example.hospitalbank.Model;

public class Message {
    private String boody;
    private String message_date;
    private String message_time;
    private String message_id;
    private String message_sender;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getBoody() {
        return boody;
    }

    public void setBoody(String boody) {
        this.boody = boody;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_sender() {
        return message_sender;
    }

    public void setMessage_sender(String message_sender) {
        this.message_sender = message_sender;
    }

    public Message(String boody, String message_date, String message_time, String message_id, String message_sender) {
        this.boody = boody;
        this.message_date = message_date;
        this.message_time = message_time;
        this.message_id = message_id;
        this.message_sender = message_sender;
    }

    public Message(String boody, String message_date, String message_time, String message_id) {
        this.boody = boody;
        this.message_date = message_date;
        this.message_time = message_time;
        this.message_id = message_id;
    }

    public Message() {
    }
}
