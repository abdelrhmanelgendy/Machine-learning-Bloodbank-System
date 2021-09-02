package com.example.hospitalbank.Classes;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class RegionLanguage {

    public RegionLanguage(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        context.createConfigurationContext(configuration);
    }
}
