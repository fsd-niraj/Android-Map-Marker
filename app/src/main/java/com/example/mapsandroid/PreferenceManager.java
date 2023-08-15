package com.example.mapsandroid;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {
    private static final String PREFS_NAME = "MapsApp";
    private static final String LOC_LIST_KEY = "LocationList";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveTodoList(ArrayList<Location> todoListJson) {
        Gson gson = new Gson();
        sharedPreferences.getString(LOC_LIST_KEY, null);
        editor.putString(LOC_LIST_KEY, gson.toJson(todoListJson));
        editor.apply();
    }

    public ArrayList<Location> getTodoList() {
        Gson gson = new Gson();
        Type locationListType = new TypeToken<List<Location>>() {}.getType();
        String list = sharedPreferences.getString(LOC_LIST_KEY, null);
        return list == null ? new ArrayList<>() : gson.fromJson(list, locationListType);
    }
}