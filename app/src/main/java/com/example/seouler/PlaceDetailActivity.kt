package com.example.seouler

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.activity_chatting_home.*

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailName = findViewById<TextView>(R.id.placeDetailName)

        // 불러온 상위 인텐트에서 스트링을 받아와 텍스트 설정하기
        detailName.text = intent.extras!!["itemName"].toString()
    }
}