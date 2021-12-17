package com.client.vpman.weatherwall.ui.Activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    var pref1: SharedPref1? = null
    var binding: ActivitySplashScreenBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        pref1 = SharedPref1(this@SplashScreen)
        when {
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE -> {
                val res = resources //resource handle
                val drawable =
                    res.getDrawable(R.drawable.splashlarge) //new Image that was added to the res folder
                binding!!.container.background = drawable
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_NORMAL -> {
                val res = resources //resource handle
                val drawable =
                    res.getDrawable(R.drawable.splash) //new Image that was added to the res folder
                binding!!.container.background = drawable
            }
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_SMALL -> {
                val res = resources //resource handle
                val drawable =
                    res.getDrawable(R.drawable.splashsmall) //new Image that was added to the res folder
                binding!!.container.background = drawable
            }
            else -> {
                val res = resources //resource handle
                val drawable =
                    res.getDrawable(R.drawable.splash) //new Image that was added to the res folder
                binding!!.container.background = drawable
            }
        }
        Handler().postDelayed({
            if (pref1!!.looadFirstState()) {
                val intent = Intent(this@SplashScreen, IntroActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            } else {
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }, 3000)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val animationStarted = false
        if (!hasFocus || animationStarted) {
            return
        }
        animate()
        super.onWindowFocusChanged(hasFocus)
    }

    private fun animate() {
        ViewCompat.animate(binding!!.logoImg)
            .translationY(-250f)
            .setStartDelay(STARTUP_DELAY.toLong())
            .setDuration(ANIM_ITEM_DURATION.toLong()).setInterpolator(
                DecelerateInterpolator(1.2f)
            ).start()
        for (i in 0 until binding!!.container.childCount) {
            val v = binding!!.container.getChildAt(i)
            val viewAnimator: ViewPropertyAnimatorCompat = if (v !is Button) {
                ViewCompat.animate(v)
                    .translationY(50f).alpha(1f)
                    .setStartDelay((ITEM_DELAY * i + 500).toLong())
                    .setDuration(1000)
            } else {
                ViewCompat.animate(v)
                    .scaleY(1f).scaleX(1f)
                    .setStartDelay((ITEM_DELAY * i + 500).toLong())
                    .setDuration(500)
            }
            viewAnimator.setInterpolator(DecelerateInterpolator()).start()
        }
    }

    companion object {
        const val STARTUP_DELAY = 500
        const val ANIM_ITEM_DURATION = 1000
        const val ITEM_DELAY = 500
    }
}