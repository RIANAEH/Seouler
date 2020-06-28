package com.example.seouler.Chatting

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Participation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.seouler.dataClass.Location as LocationData
import kotlinx.android.synthetic.main.activity_chattinghome.*
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class ChattingHomeActivity : AppCompatActivity() {
    var chattingRoomList : ArrayList<ChattingRoom> = ArrayList()
    lateinit var adapter : ChattingHomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattinghome)

        supportActionBar!!.title = "CHATTING"



        /* 챗봇 클릭했을 때의 리스너 */
        chatbotCellButton.setOnClickListener {
            val chatBotIntent = Intent(this, ChatBotActivity::class.java)
            val myLocation = intent.extras!!["myLocation"] as LocationData
            val locationX = myLocation.locationX
            val locationY = myLocation.locationY
            chatBotIntent.putExtra("locationX", locationX)
            chatBotIntent.putExtra("locationY", locationY)
            Log.d("ChatbotExtra", chatBotIntent.extras!!["locationX"].toString())
            Log.d("ChatbotExtra", chatBotIntent.extras!!["locationY"].toString())
            startActivity(chatBotIntent)
        }

        /* 채팅방 클릭했을 때의 리스너 */
        val scope = CoroutineScope(Dispatchers.Main)
        CoroutineScope(Dispatchers.Default).launch {
            //delay(2000)
        }

        scope.launch(Dispatchers.Default) {
            // 기존 CoroutineScope 는 유지하되, 작업만 백그라운드로 처리
        }


        loadChattingRoom(intent.extras!!["userId"] as Long)

        adapter = ChattingHomeAdapter()
        adapter.userId = intent.extras!!["userId"] as Long
        adapter.listData = chattingRoomList
        adapter.chattingHomeContext = this
        chattinghomeRecyclerView.adapter = adapter
        chattinghomeRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    override fun onResume() {
        super.onResume()
        loadChattingRoom(intent.extras!!["userId"] as Long)
        adapter.notifyDataSetChanged()
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chattinghome, menu)
        return true
    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menuitem_createroom -> {
                //검색 버튼 눌렀을 때
                val nextIntent = Intent(this, CreateRoomAcvitivy::class.java)
                nextIntent.putExtra("userId", intent.extras!!["userId"] as Long)
                nextIntent.putExtra("myLocation", intent.extras!!["myLocation"] as LocationData)
                startActivity(nextIntent)
                return super.onOptionsItemSelected(item)
            }
            R.id.menuitem_searchroom -> {
                //공유 버튼 눌렀을 때
                val nextIntent = Intent(this, SearchRoomActivity::class.java)
                nextIntent.putExtra("userId", intent.extras!!["userId"] as Long)
                nextIntent.putExtra("myLocation", intent.extras!!["myLocation"] as LocationData)
                startActivity(nextIntent)
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    fun loadChattingRoom(userId : Long){
        var partData : ArrayList<Participation> = ArrayList()
        var myRoomIDList : ArrayList<Long> = ArrayList()



        val partRef = FirebaseDatabase.getInstance().getReference("participation")
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (partSnapshot in dataSnapshot.children) {
                    Log.d("ChattingHomeActivity", partSnapshot.toString())
                    if(userId == partSnapshot.child("userId").value as Long){
                        Log.d("ChattingHomeActivity","C read userId : ${partSnapshot.child("userId").value as Long}")
                        partData.add(
                            Participation(
                                partSnapshot.child("userId").value as Long,
                                partSnapshot.child("roomId").value as Long,
                                partSnapshot.child("uid").value as Long
                            )
                        )
                    }

                }
                for (i in 0 until partData.count()){
                    //본인의 채팅방만 불러오기
                    if(partData[i].userId == userId){
                        myRoomIDList.add(partData[i].roomId)
                        Log.d("ChattingHomeActivity", "D ${myRoomIDList[i]}")
                    }
                }
            }

        }
        partRef.addListenerForSingleValueEvent(valueEventListener)

        var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom").orderByChild("timestamp")
        val roomValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chattingRoomList.clear()
                for (partSnapshot in dataSnapshot.children) {
                    for(i in 0 until myRoomIDList.count()){
                        if(myRoomIDList[i] == partSnapshot.child("roomId").value as Long){
                            Log.d("ChattingHomeActivity", "F Yes")
                            chattingRoomList.add(
                                ChattingRoom(
                                    partSnapshot.child("roomId").value as Long,
                                    partSnapshot.child("title").value.toString(),
                                    partSnapshot.child("description").value.toString(),
                                    partSnapshot.child("owner").value as Long,
                                    partSnapshot.child("locationX").value as Double,
                                    partSnapshot.child("locationY").value as Double,
                                    partSnapshot.child("locationName").value.toString(),
                                    partSnapshot.child("locationCertified").value as Boolean,
                                    partSnapshot.child("timestamp").value as Long,
                                    partSnapshot.child("uid").value as Long
                                )
                            )
                        }
                    }
                }
                chattingRoomList.reverse()
                val PUBLIC_ROOM_ID : Long = 1
                for(i in 0 .. chattingRoomList.size-1){
                    if(chattingRoomList[i].roomId == PUBLIC_ROOM_ID){
                        chattingRoomList.add(0, chattingRoomList[i])
                        chattingRoomList.removeAt(i+1)
                        break
                    }
                }
                Log.d("ChattingHomeActivity","G room count : ${chattingRoomList.count()}")
                adapter.notifyDataSetChanged()
            }

        }
        roomRef.addListenerForSingleValueEvent(roomValueEventListener)


    }
}