package com.client.vpman.weatherwall.ui.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.client.vpman.weatherwall.Adapter.CustomePagerAdapter
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivityIntroBinding
import com.client.vpman.weatherwall.model.OnBoardingModel
import java.util.*

class IntroActivity : AppCompatActivity() {
    private var customePagerAdapter: CustomePagerAdapter? = null
    var binding: ActivityIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(
            layoutInflater
        )
        val view1: View = binding!!.root
        setContentView(view1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val pref = SharedPref1(this)
        pref.setFirstState(false)
        setupOnBoardingItem()
        binding!!.onBoardingViewPager.adapter = customePagerAdapter
        setupOnBoardingIndicator()
        setCurrentBoardingIndicator(0)
        binding!!.onBoardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentBoardingIndicator(position)
            }
        })
        binding!!.buttonOnBoardingAction.setOnClickListener { v: View? ->
            if (binding!!.onBoardingViewPager.currentItem + 1 < customePagerAdapter!!.itemCount) {
                binding!!.onBoardingViewPager.currentItem =
                    binding!!.onBoardingViewPager.currentItem + 1
            } else {
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupOnBoardingItem() {
        val onBoardingModels: MutableList<OnBoardingModel> = ArrayList()
        val onBoardingModel = OnBoardingModel()
        onBoardingModel.title = "Weather Forecast"
        onBoardingModel.description = "A small information about Weather of your current Location"
        onBoardingModel.image = R.drawable.ic_undraw_weather_app_i5sm
        val quotesItem = OnBoardingModel()
        quotesItem.title = "Quotes for inspiration"
        quotesItem.description = "Quotes that inspire you and motivate you for your life"
        quotesItem.image = R.drawable.ic_undraw_social_bio_8pql
        val wallpaper = OnBoardingModel()
        wallpaper.title = "Beautiful images"
        wallpaper.description = "Amazing images for your screen wallpaper"
        wallpaper.image = R.drawable.ic_undraw_inspiration_lecl
        onBoardingModels.add(onBoardingModel)
        onBoardingModels.add(quotesItem)
        onBoardingModels.add(wallpaper)
        customePagerAdapter = CustomePagerAdapter(onBoardingModels)
    }

    private fun setupOnBoardingIndicator() {
        val indicator = arrayOfNulls<ImageView>(
            customePagerAdapter!!.itemCount
        )
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(applicationContext)
            indicator[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_inactive
                )
            )
            indicator[i]!!.layoutParams = layoutParams
            binding!!.onBoardingIndicator.addView(indicator[i])
        }
    }

    private fun setCurrentBoardingIndicator(index: Int) {
        val childCount = binding!!.onBoardingIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding!!.onBoardingIndicator.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onborard_indicator
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_inactive
                    )
                )
            }
        }
        if (index == customePagerAdapter!!.itemCount - 1) {
            binding!!.buttonOnBoardingAction.text = "Start"
        } else {
            binding!!.buttonOnBoardingAction.text = "Next"
        }
    }
}