package com.example.hospitalbank.Model;

public class RequestModel {
    private String boody;
    private String message_date;
    private String message_time;
    private String message_id;
    private String message_sender;
    private String blood_type;
    private String blood_amount;
    private String blood_recieved;

    public String getBlood_recieved() {
        return blood_recieved;
    }

    public void setBlood_recieved(String blood_recieved) {
        this.blood_recieved = blood_recieved;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getBlood_amount() {
        return blood_amount;
    }

    public void setBlood_amount(String blood_amount) {
        this.blood_amount = blood_amount;
    }

    public RequestModel(String boody, String message_date, String message_time, String message_id, String message_sender, String blood_type, String blood_amount, String blood_recieved) {
        this.boody = boody;
        this.message_date = message_date;
        this.message_time = message_time;
        this.message_id = message_id;
        this.message_sender = message_sender;
        this.blood_type = blood_type;
        this.blood_amount = blood_amount;
        this.blood_recieved = blood_recieved;
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

    public RequestModel() {
    }

}
