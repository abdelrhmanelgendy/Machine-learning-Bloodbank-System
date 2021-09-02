package com.example.hospitalbank.Classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class calculateLastDonation {

    public Long getDaysBetweenDates(String lastDate) {
        long daysBetween = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
//        String dateBeforeString = "31-01-2014";
//        String dateAfterString = "02-08-2014";
        try {
            //add 90 days to last date donation
            calendar.setTime(format.parse(lastDate));
            calendar.add(Calendar.DAY_OF_MONTH, 90);
            String newDate = format.format(calendar.getTime());

            //get number of remaining days from last donation
            Date dateLastDonation = format.parse(newDate);
            Date dateCurrent = format.parse(format.format(new Date()));
            long difference = dateLastDonation.getTime() - dateCurrent.getTime();
            daysBetween = (difference / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daysBetween;
    }

}