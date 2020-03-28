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
}
