package com.example.seouler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* MainActivity에서 먼저 DB로부터 필요한 데이터 읽어오는 과정 필요 */


        /* Activity간 데이터 전달 테스트용 변수 */
        var value = 30
        mainChattingButton.setOnClickListener {
            val chattingRoomIntent = Intent(this, ChattingHomeActivity::class.java)
            //chattingRoomIntent.putExtra("", value)
            startActivity(chattingRoomIntent)
        }
    }
}
