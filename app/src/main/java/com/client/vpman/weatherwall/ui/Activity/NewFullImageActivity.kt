package com.client.vpman.weatherwall.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivityNewFullImageBinding

class NewFullImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewFullImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_full_image)
    }
}