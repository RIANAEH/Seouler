package com.example.seouler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(2000) // 2초 대기
        } catch (e: InterruptedException) {
            Log.d("태그", "$e")
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
