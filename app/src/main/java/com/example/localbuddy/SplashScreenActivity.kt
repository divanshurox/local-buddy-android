package com.example.localbuddy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }
        setContentView(R.layout.splash_screen)

        val animation = AnimationUtils.loadAnimation(this,R.anim.splash_screen)
        val image = findViewById<ImageView>(R.id.image)
        val title = findViewById<TextView>(R.id.title)
        val slogan = findViewById<TextView>(R.id.slogan)
        image.startAnimation(animation)
        title.startAnimation(animation)
        slogan.startAnimation(animation)

        Timer("SplashScreenTimer",false).schedule(1500){
            Intent(this@SplashScreenActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }
    }
}