package com.example.seouler

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.APITask.GetDetailTask
import kotlinx.android.synthetic.main.activity_like_place.*

class PlaceLikingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_place)
      
        // contentId로 api호출 -> 세부정보 받아오기
        val dt = GetDetailTask(intent.extras!!["contentId"].toString())
        dt.execute().get()

        // 받아온 정보 보여주기
        findViewById<ImageView>(R.id.lp_firstimage).setImageBitmap(dt.firstimage)
        findViewById<TextView>(R.id.lp_title).setText(dt.title)
        findViewById<TextView>(R.id.lp_introduction).text = dt.introduction
        findViewById<TextView>(R.id.lp_addr1).text = "Address\n"+dt.addr1
        findViewById<TextView>(R.id.lp_tel).text = "Tel\n"+dt.tel
        findViewById<TextView>(R.id.lp_homepage).text = "Homepage\n"+dt.homepage
        findViewById<TextView>(R.id.lp_directions).text = "Directions\n"+dt.directions

        // 찜리스트를 검사해 리스트에 존재하느냐에 따라 결정한다.
        var like_check = false

        lp_btn_like.setOnClickListener {
            // 좋아요 여부 반전 토글 후 저장
            if(like_check) {
                it.setBackgroundResource(R.drawable.heart_unlike)
                val txt = "I don't like this place!"
                Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
                like_check = false
            }
            else {

                it.setBackgroundResource(R.drawable.heart_like)
                val txt = "I like this place!"
                Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
                like_check = true
            }
            //it.setBackgroundResource(R.drawable.heart_like)
            // 알림 메시지 띄우고 나가기
            //val txt = "I like this place!"
            //Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
            //finish()
        }
    }
}
