package com.example.seouler.Chatting

import android.os.Bundle
import android.renderscript.Sampler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chattinghome.*
import kotlinx.android.synthetic.main.activity_chattingroom_info.*

class ChattingInfoActivity : AppCompatActivity() {
    var roomId : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattingroom_info)
        roomId = intent.extras!!["roomId"] as Long



        var roomPartIdList = ArrayList<Long>()
        var roomPartNameList = ArrayList<String>()
        var adapter = ChattingInfoAdapter(this, roomPartNameList)
        var partRef = FirebaseDatabase.getInstance().getReference("participation")
        var partValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                roomPartIdList.clear()
                for(data in p0.children) {
                    if (roomId == data.child("roomId").value as Long) {
                        roomPartIdList.add(data.child("userId").value as Long)
                    }
                }
            }

        }
        partRef.addListenerForSingleValueEvent(partValueEventListener)

        var userRef = FirebaseDatabase.getInstance().getReference("user")
        var userValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    for(i in 0 until roomPartIdList.size){
                        if(roomPartIdList[i] == data.child("userId").value as Long){
                            roomPartNameList.add("${data.child("name").value.toString()}")
                        }
                    }
                }
                adapter.listData = roomPartNameList
                adapter.notifyDataSetChanged()
            }

        }
        userRef.addListenerForSingleValueEvent(userValueEventListener)
        chattingInfoRecyclerView.adapter = adapter
        chattingInfoRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}