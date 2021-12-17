package com.client.vpman.weatherwall.CustomeUsefullClass

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import java.util.*

object Utils {
    var vibrantLightColorList = arrayOf(
        ColorDrawable(Color.parseColor("#f57d13")),
        ColorDrawable(Color.parseColor("#9fa8a3")),
        ColorDrawable(Color.parseColor("#ff0000")),
        ColorDrawable(Color.parseColor("#C70039")),
        ColorDrawable(Color.parseColor("#817c7d")),
        ColorDrawable(Color.parseColor("#e5efde")),
        ColorDrawable(Color.parseColor("#000000")),
        ColorDrawable(Color.parseColor("#c3cec3")),
        ColorDrawable(Color.parseColor("#0f384d"))
    )
    @JvmStatic
    val randomDrawbleColor: ColorDrawable
        get() {
            val idx = Random().nextInt(vibrantLightColorList.size)
            return vibrantLightColorList[idx]
        }
}