package com.example.hospitalbank.Classes;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
 public   boolean isconnected(ConnectivityManager mConnectivityManager) {

            NetworkInfo wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo data = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnected() || data.isConnected()) {
                return true;
            } else {
                return false;
        }
    }
}
