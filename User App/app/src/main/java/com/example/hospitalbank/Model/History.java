package com.example.hospitalbank.Model;

public class History {
    String ID;
    String Date;
    String Time;
    String Email;
    String BloodType;
    String Username;

    public History() {
    }

    public History(String ID, String date, String time, String email, String bloodType, String username) {
        this.ID = ID;
        Date = date;
        Time = time;
        Email = email;
        BloodType = bloodType;
        Username = username;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBloodType() {
        return BloodType;
    }

    public void setBloodType(String bloodType) {
        BloodType = bloodType;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        Username = Username;
    }
}
