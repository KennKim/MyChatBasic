package com.basic.mychat.mychatbasic.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceUtil {
    public static final String DEFAULT_SETTING = "MY CHAT BASIC";
    public static final String USER_NAME = "user_name";

    private static volatile SharedPreferenceUtil instance = null;

    private Context context;
    private SharedPreferences sharedPref;

    private SharedPreferenceUtil() {}

            public static SharedPreferenceUtil getInstance() {
                if(instance == null) {
            synchronized (SharedPreferenceUtil.class) {
                instance = new SharedPreferenceUtil();
            }
        }
        return instance;
    }


    public void init(Context _context) {
        context = _context;
        sharedPref = context.getSharedPreferences(DEFAULT_SETTING, context.MODE_PRIVATE);
    }

    public void putString(String _key, String _value) {
        if(sharedPref == null){
            return;
        }
        SharedPreferences.Editor e = sharedPref.edit();
        e.putString(_key, _value);
        e.commit();
    }

    public String getString(String _key, String _value) {
        if(sharedPref == null){
            return null;
        }
        return sharedPref.getString(_key, _value);
    }

    public void putBoolean(String _key, boolean _value) {
        if(sharedPref == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPref.edit();
        e.putBoolean(_key, _value);
        e.commit();
    }

    public boolean getBoolean(String _key, boolean _value) {
        if(sharedPref == null) {
            return false;
        }
        return sharedPref.getBoolean(_key, _value);
    }

    public void putInt(String _key, int _value) {
        if(sharedPref == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPref.edit();
        e.putInt(_key, _value);
        e.commit();
    }

    public int getInt(String _key, int _value) {
        if(sharedPref == null) {
            return -1;
        }
        return sharedPref.getInt(_key, _value);
    }

    public void putLong(String _key, long _value) {
        if(sharedPref == null) {
            return;
        }
        SharedPreferences.Editor e = sharedPref.edit();
        e.putLong(_key, _value);
        e.commit();
    }

    public long getLong(String _key, long _value) {
        if(sharedPref == null) {
            return -1;
        }
        return sharedPref.getLong(_key, _value);
    }

    public void clear() {
        SharedPreferences.Editor e = sharedPref.edit();
        e.clear();
        e.commit();
    }

}

