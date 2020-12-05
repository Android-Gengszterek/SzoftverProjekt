package com.mygdx.game.ui.activitys

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.mygdx.game.R
import com.mygdx.game.databinding.ActivitySplashScreenBinding
import java.util.*
import kotlin.concurrent.schedule

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var doodle: ImageView
    private lateinit var title: TextView
    private lateinit var zsolt: TextView
    private lateinit var boti: TextView
    private lateinit var szanto: TextView
    private lateinit var developers: ImageView
    private lateinit var coordinator: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        doodle = binding.imageDoodle
        title = binding.textView
        zsolt = binding.name1TextView
        boti = binding.name2TextView
        developers = binding.imageView2
        szanto = binding.name3TextView
        coordinator = binding.imageView3

        animateLogo()

        val intent = Intent(this, MainActivity::class.java)
        Timer().schedule(5000){
            //while (!downloaded)

            startActivity(intent)
            overridePendingTransition(R.anim.fade_out, R.anim.splash_anim);
            finish()
        }
    }



    private fun animateLogo() {
        animateDoodle()
        animateTitle()
        animateDevelopers()
        animateCoordinator()
    }

    private fun animateDoodle() {
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.jump)
        bounceAnimation.repeatMode = Animation.REVERSE
        doodle.startAnimation(bounceAnimation)
    }

    private fun animateTitle(){
        val titleAnimation = AnimationUtils.loadAnimation(this, R.anim.fall_down)
        titleAnimation.startOffset = 1000
        title.startAnimation(titleAnimation)
    }
    private fun animateDevelopers() {
        val fromRightAnim = AnimationUtils.loadAnimation(this, R.anim.from_right)
        val fromLeftAnim = AnimationUtils.loadAnimation(this, R.anim.from_left)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeIn.startOffset = 1000
        zsolt.startAnimation(fromLeftAnim)
        boti.startAnimation(fromRightAnim)
        developers.startAnimation(fadeIn)
    }

    private fun animateCoordinator() {
        val rotateAndFade = AnimationUtils.loadAnimation(this, R.anim.rotate_fade)
        val fromRight = AnimationUtils.loadAnimation(this, R.anim.from_right)
        rotateAndFade.startOffset = 3000
        fromRight.startOffset = 2800
        szanto.startAnimation(rotateAndFade)
        coordinator.startAnimation(fromRight)

    }
}