package com.gosu.jungwon

import android.content.Intent
import android.content.IntentSender.OnFinished
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    //UI Views
    private lateinit var timerTv: TextView

    companion object{
        private const val TAG = "SPLASH_TAG"

        //Number of seconds to count down before showing the app open ad. This simulates the time needed to load the app.
        private const val COUNTER_TIMER: Long= 3//로딩화면 3초
    }

    private var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //init UI Views
        timerTv = findViewById(R.id.timerTv)

        createTimer(COUNTER_TIMER)
    }

    private fun createTimer(seconds: Long) {
        val counteDownTimer: CountDownTimer = object : CountDownTimer(seconds * 1000, 1000){
            override fun onTick(millisecondsFinished: Long) {
                Log.d(TAG, "onTick: $millisecondsFinished")
                secondsRemaining = millisecondsFinished / 1000 + 1
                timerTv.text = "Loading in $secondsRemaining"
            }

            override fun onFinish() {
                Log.d(TAG, "onFinish: ")
                secondsRemaining = 0
                timerTv.text = "Loaded..."

                val application = application
                if (application !is  MyApplication){
                    Log.d(TAG, "onFinish: Failed to cast application to MyApplications") //e.g. if you don;t register your MyApplications in manifest
                    startMainActivity()
                    return
                }

                //show the ad
                application.showAdIfAvailable(
                    this@SplashActivity,
                    object: MyApplication.OnShowAdcompleteListener{
                        override fun onShowAdComplete() {
                            Log.d(TAG, "onShowAdComplete: ")
                            startMainActivity()
                        }
                    }
                )
            }
        }

        //start timer
        counteDownTimer.start()
    }

    private fun startMainActivity() {
        //start main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}