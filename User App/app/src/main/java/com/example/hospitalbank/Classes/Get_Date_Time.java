package com.example.hospitalbank.Classes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Get_Date_Time {

    public static String gettime() {

        DateFormat Time_format = new SimpleDateFormat("hh.mm aa");

        String time = Time_format.format(new Date()).toString();

        return time;
    }

    public static String getdate() {

        DateFormat Date_format = new SimpleDateFormat("dd-MM-yyyy");

        String date = Date_format.format(new Date()).toString();

        return date;
    }

}
