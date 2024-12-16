package com.example.notionary_v1.fragments.data;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String KEY_TOKEN = "JWT_TOKEN";
    private static String KEY_ID = "";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public void saveId(String id) {
        editor.putString(KEY_ID, id);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getId() {
        return sharedPreferences.getString(KEY_ID, null);
    }

    public void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_ID);
        editor.apply();
    }
}
