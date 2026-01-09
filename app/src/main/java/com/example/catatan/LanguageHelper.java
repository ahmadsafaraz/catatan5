package com.example.catatan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageHelper {

    private static final String PREF_NAME = "language_pref";
    private static final String KEY_LANGUAGE = "app_language";

    public static void setLanguage(Context context, String languageCode) {
        saveLanguage(context, languageCode);
        updateResources(context, languageCode);
    }

    public static void applyLanguage(Context context) {
        String languageCode = getSavedLanguage(context);
        updateResources(context, languageCode);
    }

    private static void saveLanguage(Context context, String language) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, language).apply();
    }

    private static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "id"); // default Indonesia
    }

    private static void updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);

        context.getResources().updateConfiguration(
                config,
                context.getResources().getDisplayMetrics()
        );
    }
}
