package com.client.vpman.weatherwall.CustomeUsefullClass;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import java.util.Random;

public class Utils {
    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#f57d13")),
                    new ColorDrawable(Color.parseColor("#9fa8a3")),
                    new ColorDrawable(Color.parseColor("#ff0000")),
                    new ColorDrawable(Color.parseColor("#C70039")),
                    new ColorDrawable(Color.parseColor("#817c7d")),
                    new ColorDrawable(Color.parseColor("#e5efde")),
                    new ColorDrawable(Color.parseColor("#000000")),
                    new ColorDrawable(Color.parseColor("#c3cec3")),
                    new ColorDrawable(Color.parseColor("#0f384d"))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }
}
