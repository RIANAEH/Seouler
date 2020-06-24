package com.example.seouler

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.fragments.DetailItemFragment
import kotlinx.android.synthetic.main.activity_chatting_home.*
import kotlinx.android.synthetic.main.activity_detail.*

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 불러온 상위 인텐트에서 스트링을 받아와 텍스트 설정하기
        val detailName = findViewById<TextView>(R.id.placeDetailName)
        detailName.text = intent.extras!!["itemName"].toString()

        // 테스트용으로 프래그먼트 불러오기
        val tmpFrag = findViewById<FrameLayout>(R.id.detailItem)
        detailItemCarrier.addView(tmpFrag)
        detailItemCarrier.addView(tmpFrag)
        detailItemCarrier.addView(tmpFrag)
    }
}