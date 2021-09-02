package com.example.hospitalbank.Classes;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseOfflineData extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        FirebaseDatabase.keppSynced(true); //t or f


    }
}
