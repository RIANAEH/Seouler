package com.example.seouler.Chatting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Location as LocationData
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
        adapter.userId = intent.extras!!["userId"] as Long
        adapter.listData = intent.extras!!["chattingroomList"] as ArrayList<ChattingRoom>
        adapter.chattingHomeContext = this
        chattinghomeRecyclerView.adapter = adapter
        chattinghomeRecyclerView.layoutManager = LinearLayoutManager(this)

        chattingHomeHeaderMenuButton.setOnClickListener {
            var menuPopup = PopupMenu(this, chattingHomeHeaderMenuButton)
            menuPopup.menuInflater.inflate(R.menu.menu_chattinghome, menuPopup.menu)

            menuPopup.setOnMenuItemClickListener {
                val item = it.itemId

                when(item){
                    R.id.menuitem_createroom -> {
                        val nextIntent = Intent(this, CreateRoomAcvitivy::class.java)
                        nextIntent.putExtra("myLocation", intent.extras!!["myLocation"] as LocationData)
                        startActivity(nextIntent)
                    }
                    R.id.menuitem_searchroom -> {
                        val nextIntent = Intent(this, SearchRoomActivity::class.java)
                        nextIntent.putExtra("userId", intent.extras!!["userId"] as Long)
                        nextIntent.putExtra("myLocation", intent.extras!!["myLocation"] as LocationData)
                        startActivity(nextIntent)

                    }
                }
                true


            }
            menuPopup.show()


        }
    }
}