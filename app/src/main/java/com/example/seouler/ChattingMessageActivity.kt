package com.example.seouler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chattinghome.*
import kotlinx.android.synthetic.main.activity_chattingmessage.*

class ChattingMessageActivity : AppCompatActivity(){
    var scrollingToBottom : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattingmessage)

        val data: ArrayList<Message> = loadLog()
        var adapter = ChattingMessageAdapter(this, data)
        adapter.listData = data

        chattingMessageRecyclerView.adapter = adapter
        chattingMessageRecyclerView.layoutManager = LinearLayoutManager(this)
        chattingMessageRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                chattingMessageRecyclerView.postDelayed(Runnable() {
                    chattingMessageRecyclerView.smoothScrollToPosition(data.size-1)
                }, 100)
            }
        }
    }

    fun loadLog() : ArrayList<Message>{
        val USERID : Long = 1592656608691
        val ROOMID : Long = 1592656608641
        var messageData:ArrayList<Message> = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("message")
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (msgSnapshot in dataSnapshot.children) {
                    if(ROOMID == msgSnapshot.child("roomId").value as Long){
                        messageData.add(Message(msgSnapshot.child("messageId").value as Long,
                            msgSnapshot.child("roomId").value as Long,
                            msgSnapshot.child("sender").value as Long,
                            msgSnapshot.child("text").value.toString(),
                            msgSnapshot.child("timestamp").value as Long)
                        )
                    }

                }
            }

        }
        ref.addValueEventListener(valueEventListener)

        return messageData
    }

}
