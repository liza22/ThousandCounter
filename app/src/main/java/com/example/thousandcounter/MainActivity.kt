package com.example.thousandcounter

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val timer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(1)) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            val intent = Intent(this@MainActivity, CounterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        timer.start()
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        timer.cancel()
        super.onBackPressed()
    }
}

