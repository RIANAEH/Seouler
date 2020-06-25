package com.example.seouler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.activity_like_place.*

class PlaceLikingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_place)

        // 이미지와 이름, 설명 등의 정보 불러오기

        likeButton.setOnClickListener {
            // 좋아요 여부 반전 토글 후 저장

            // 알림 메시지 띄우고 나가기
            val txt = "I like this place!"
            Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}