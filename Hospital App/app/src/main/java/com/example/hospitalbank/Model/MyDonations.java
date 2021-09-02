package com.example.hospitalbank.Model;

public class MyDonations {
    private String Tottalamount;
    private String date;
    private String address;

    public MyDonations() {
    }

    public MyDonations(String tottalamount, String date, String address) {
        Tottalamount = tottalamount;
        this.date = date;
        this.address = address;
    }

    public String getTottalamount() {
        return Tottalamount;
    }

    public void setTottalamount(String tottalamount) {
        Tottalamount = tottalamount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
