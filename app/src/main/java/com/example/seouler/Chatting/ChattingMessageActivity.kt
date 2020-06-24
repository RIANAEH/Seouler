package com.example.seouler.Chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chattingmessage.*

class ChattingMessageActivity : AppCompatActivity(){
    var scrollingToBottom : Boolean = false
    var messageList : ArrayList<Message> = ArrayList()
    var adapter : ChattingMessageAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattingmessage)

        /*
        1. userId와 roomId를 알고 있어야함.
        - ChattingHome에서 putExtra로 userId, roomId 보내줌.
        - 현재 액티비티에서 intent.extras!!["key"] 로 값 받아옴.
        2. roomId를 가지고 message 테이블에서 데이터 가져옴.
        - 현재 액티비티에서 DB 읽기.
        3. 가져온 데이터를 adapter에 넣고
        4. adapter에서 userId를 가지고 본인의 메시지와 타인의 메시지를 구별하여 화면에 그려줌.
         */
        val roomId : Long = intent.extras!!["roomId"] as Long
        messageList = loadMessage(roomId)

        adapter = ChattingMessageAdapter(
            this,
            messageList
        )
        adapter!!.USERID = intent.extras!!["userId"] as Long
        chattingMessageRecyclerView.adapter = adapter
        chattingMessageRecyclerView.layoutManager = LinearLayoutManager(this)
        chattingMessageRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                chattingMessageRecyclerView.postDelayed(Runnable() {
                    if(messageList.size!=0){
                        chattingMessageRecyclerView.smoothScrollToPosition(messageList.size-1)
                    }
                }, 100)
            }
        }

        /*
        send 버튼을 누르면 DB에 데이터가 써지고 화면이 다시 그려짐.
         */
        sendMessageButton.setOnClickListener{
            var messageRef = FirebaseDatabase.getInstance().getReference("message")
            var uid = System.currentTimeMillis()
            messageRef.child("${uid}").setValue(Message(uid, roomId, intent.extras!!["userId"] as Long, "${inputMessageTextView.text}", uid, uid))

            var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom")
            var updateRoomTimestampListner = object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (roomSnapshot in dataSnapshot.children) {
                        if (roomId == roomSnapshot.child("roomId").value as Long) {
                            roomSnapshot.ref.child("timestamp").setValue(uid)

                            //roomRef.child(key).child("timestamp").setValue(uid)
                        }

                    }
                }

            }
            roomRef.addListenerForSingleValueEvent(updateRoomTimestampListner)
        }
    }

    fun loadMessage(roomId : Long) : ArrayList<Message>{
        var msgData = ArrayList<Message>()
        var msgRef = FirebaseDatabase.getInstance().getReference("message").orderByChild("timestamp")
        val msgEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                msgData.clear()
                for (msgSnapshot in dataSnapshot.children) {
                    Log.d("MainActivity", msgSnapshot.toString())
                    if (roomId == msgSnapshot.child("roomId").value as Long) {
                        msgData.add(
                            Message(
                                msgSnapshot.child("messageId").value as Long,
                                msgSnapshot.child("roomId").value as Long,
                                msgSnapshot.child("sender").value as Long,
                                msgSnapshot.child("text").value.toString(),
                                msgSnapshot.child("timestamp").value as Long,
                                msgSnapshot.child("uid").value as Long
                            )
                        )
                    }

                }
                msgData.sortBy {
                    it.timestamp
                }
                for(i in 0 until msgData.count()){
                    Log.d("ChattingMessageActivity", "${msgData[i].timestamp}")
                }
                chattingMessageRecyclerView.removeAllViewsInLayout()
                chattingMessageRecyclerView.adapter = adapter
                chattingMessageRecyclerView.scrollToPosition(messageList.size-1)
                inputMessageTextView.text.clear()


            }
        }
        msgRef.addValueEventListener(msgEventListener)
        return msgData
    }

}
