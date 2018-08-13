package com.online.meeting

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.online.meeting.viewmodel.VMFirstActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this@SplashActivity, VMFirstActivity::class.java)

        startActivity(intent)
    }
}
