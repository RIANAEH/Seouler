package com.example.seouler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom

class PlaceDetailIconActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_icons)

        // 관광지 이름
        val placeName = findViewById<TextView>(R.id.placeName)

        // 관광명소 버튼
        val lmb = findViewById<Button>(R.id.buttonLandmark)
        lmb.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Landmarks")

            startActivity(intent)
        }

        // 맛집 버튼
        val rst = findViewById<Button>(R.id.buttonRestaurant)
        rst.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Restaurants")

            startActivity(intent)
        }

        // 숙소 버튼
        val inn = findViewById<Button>(R.id.buttonResort)
        inn.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Inns")

            startActivity(intent)
        }

        // 역사 버튼
        val htr = findViewById<Button>(R.id.buttonHistory)
        htr.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "History")

            startActivity(intent)
        }

        // 쇼핑 버튼
        val shp = findViewById<Button>(R.id.buttonShopping)
        shp.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Shopping")

            startActivity(intent)
        }

        // 기타 버튼
        val etc = findViewById<Button>(R.id.buttonOthers)
        etc.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Others")

            startActivity(intent)
        }
    }
}