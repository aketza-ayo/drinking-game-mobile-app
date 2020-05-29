package com.devapp.drinkinggame

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.concurrent.thread

class SplashActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        var constrainLayout = findViewById<ConstraintLayout>(R.id.splashLayout)
        var animationDrawable:AnimationDrawable = constrainLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        var imageView = findViewById<ImageView>(R.id.splashImageViewMurcielago)
        var animation = AnimationUtils.loadAnimation(applicationContext, R.anim.murcielago_splash_fade)
        var flyingMurcielago = imageView.drawable as AnimationDrawable
        imageView.startAnimation(animation)
        flyingMurcielago.start()


        thread{
            Thread.sleep(4000)

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


    }
}