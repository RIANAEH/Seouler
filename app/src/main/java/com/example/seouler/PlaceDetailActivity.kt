package com.example.seouler

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.fragments.DetailItemFragment
import kotlinx.android.synthetic.main.activity_chattinghome.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.zip.Inflater

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 불러온 상위 인텐트에서 스트링을 받아와 텍스트 설정하기
        val detailName = findViewById<TextView>(R.id.placeDetailName)
        detailName.text = intent.extras!!["detailName"].toString()

        // 테스트용으로 프래그먼트 불러오기
        val detailItemList = findViewById<LinearLayout>(R.id.detailItemCarrier)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
    }
}