package com.example.seouler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.activity_chattinghome.*

class ChattingHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinghome)

        /* 챗봇 클릭했을 때의 리스너 */
        chatbotCellButton.setOnClickListener {

        }

        /* 채팅방 클릭했을 때의 리스너 */
        var adapter = ChattingHomeAdapter()
        adapter.listData = intent.extras!!["chattingroomList"] as ArrayList<ChattingRoom>
        adapter.chattingHomeContext = this
        chattinghomeRecyclerView.adapter = adapter
        chattinghomeRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}