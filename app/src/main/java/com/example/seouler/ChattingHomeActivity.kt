package com.example.seouler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chatting_home.*

class ChattingHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_home)

        /* 챗봇 클릭했을 때의 리스너 */
        chatbotCellButton.setOnClickListener {

        }

        /* 채팅방 클릭했을 때의 리스너 */
        var adapter = ChattingHomeAdapter()
        adapter.listData = intent.extras!!["chattingroomList"] as ArrayList<ChattingRoom>
        chattingroomReciclerView.adapter = adapter
        chattingroomReciclerView.layoutManager = LinearLayoutManager(this)


    }
}