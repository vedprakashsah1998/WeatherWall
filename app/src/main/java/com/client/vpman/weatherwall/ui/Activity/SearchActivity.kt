package com.client.vpman.weatherwall.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivitySearchBinding
import com.client.vpman.weatherwall.ui.Fragment.SearchFragment

class SearchActivity : AppCompatActivity() {
    var binding: ActivitySearchBinding? = null
    var sharedPref1: SharedPref1? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, SearchFragment())
            .commit()
        sharedPref1 = SharedPref1(this@SearchActivity)
        if (sharedPref1!!.theme == "Light") {
            binding!!.searchrel.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else if (sharedPref1!!.theme == "Dark") {
            binding!!.searchrel.setBackgroundColor(Color.parseColor("#000000"))
        } else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> binding!!.searchrel.setBackgroundColor(
                    Color.parseColor(
                        "#000000"
                    )
                )
                Configuration.UI_MODE_NIGHT_NO -> binding!!.searchrel.setBackgroundColor(
                    Color.parseColor(
                        "#FFFFFF"
                    )
                )
            }
        }
    }
}