package com.example.mostafaeisam.movieschallenge.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by messam on 9/17/2018.
 */

public class SavePrefs<T> {
    private String USER_PREFS_FILE_NAME;
    private final String DATA = "DATA";
    private SharedPreferences prefs;
    private Class<?> cls;

    public SavePrefs(Context activity, Class<?> cls) {
        super();
        this.cls = cls;
        USER_PREFS_FILE_NAME = cls.getName();
        prefs = activity.getSharedPreferences(USER_PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
    }

    public void save(T obj) {
        SharedPreferences.Editor editor = prefs.edit();
        String data = new Gson().toJson(obj);

        editor.putString(DATA, data);
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public T load() {
        String data = prefs.getString(DATA, "");
        return (T) new Gson().fromJson(data, cls);
    }

    public void clear() {
        prefs.edit().clear().commit();
    }

}