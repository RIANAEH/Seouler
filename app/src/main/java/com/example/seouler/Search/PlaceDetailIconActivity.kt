package com.example.seouler.Search

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import com.example.seouler.dataClass.Location

class PlaceDetailIconActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_icons)

        // 관광지 이름
        val placeName = findViewById<TextView>(R.id.placeName)
        val preintent = intent



        // 관광명소 버튼
        val lmb = findViewById<Button>(R.id.buttonLandmark)
        lmb.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Landmarks")
            intent.putExtra("typeId", "76")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)
            startActivity(intent)
        }

        // 맛집 버튼
        val rst = findViewById<Button>(R.id.buttonDining)
        rst.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Restaurants")
            intent.putExtra("typeId", "82")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)

            startActivity(intent)
        }

        // 숙소 버튼
        val inn = findViewById<Button>(R.id.buttonAccommodation)
        inn.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Accommodation")
            intent.putExtra("typeId", "80")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)

            startActivity(intent)
        }

        // 축제등 버튼
        val htr = findViewById<Button>(R.id.buttonEvents)
        htr.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Events")
            intent.putExtra("typeId", "85")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)

            startActivity(intent)
        }

        // 쇼핑 버튼
        val shp = findViewById<Button>(R.id.buttonShopping)
        shp.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Shopping")
            intent.putExtra("typeId", "79")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)

            startActivity(intent)
        }

        // 기타 버튼
        val etc = findViewById<Button>(R.id.buttonCultural)
        etc.setOnClickListener {
            val intent = Intent(this, PlaceDetailActivity::class.java)

            // 버튼에 해당하는 아이템명을 하위 인텐트로 보내기
            intent.putExtra("detailName", "Cultural")
            intent.putExtra("typeId", "78")
            intent.putExtra("myLocation", preintent.extras!!["myLocation"] as Location)

            startActivity(intent)
        }
    }
}