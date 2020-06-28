package com.example.seouler.Like

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seouler.R
import kotlinx.android.synthetic.main.activity_like_main.*

class LikeMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_main)


        recyclerView_like.adapter = LikeAdapter(map_like.values.toTypedArray())
    }

    override fun onRestart() {
        super.onRestart()

        recyclerView_like.adapter = LikeAdapter(map_like.values.toTypedArray())
    }
}
