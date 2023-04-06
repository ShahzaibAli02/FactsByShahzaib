package com.facts.factsbyshahzaib.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.facts.factsbyshahzaib.R
import com.facts.factsbyshahzaib.activities.main.MainActivity

class SplashActivity : AppCompatActivity()
{


    private lateinit var handler: Handler
    private val SPLASH_TIME=3000L //3 Seconds
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }, SPLASH_TIME)

    }
}