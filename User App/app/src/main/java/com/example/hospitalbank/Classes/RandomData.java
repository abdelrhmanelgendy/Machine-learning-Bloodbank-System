package com.example.hospitalbank.Classes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RandomData {

    static FirebaseDatabase mdatabase;
    static DatabaseReference mReference;


    public static String getRandom() {
        Random random = new Random();
        int randomNum1 = random.nextInt(9999);
        int randomNum2 = random.nextInt(9999);
        int randomNum3 = random.nextInt(9999);
        int randomNum4 = random.nextInt(9999);

        String data = randomNum1 + "-" + randomNum2 + "-" + randomNum3 + "-" + randomNum4;

        return data;
    }

    public static void settRequestID() {
        mdatabase = FirebaseDatabase.getInstance();
        mReference = mdatabase.getReference("Request").child("request").child("message_id");
        mReference.setValue(getRandom());

    }
}
