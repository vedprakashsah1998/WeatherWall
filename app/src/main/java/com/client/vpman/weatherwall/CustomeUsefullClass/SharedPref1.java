package com.client.vpman.weatherwall.CustomeUsefullClass;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref1 {
    SharedPreferences mySharedPref;

    public SharedPref1(Context context) {
        mySharedPref = context.getSharedPreferences("filename1", Context.MODE_PRIVATE);

    }

    public void setFirstState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("FIRST", state);
        editor.commit();
    }

    public boolean looadFirstState() {
        Boolean state = mySharedPref.getBoolean("FIRST", true);
        return state;
    }

    public void setImageQuality(String state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putString("image_quality", state);
        editor.commit();
    }

    public String getImageQuality() {
        return mySharedPref.getString("image_quality", "");
    }

    public void setImageLoadQuality(String state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putString("load_quality", state);
        editor.commit();
    }

    public String getImageLoadQuality() {
        return mySharedPref.getString("load_quality", "");
    }
}
