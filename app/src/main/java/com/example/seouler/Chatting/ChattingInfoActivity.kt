package com.example.seouler.Chatting

import android.opengl.Visibility
import android.os.Bundle
import android.renderscript.Sampler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Participation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chattinghome.*
import kotlinx.android.synthetic.main.activity_chattingroom_info.*

class ChattingInfoActivity : AppCompatActivity() {
    var userId : Long = 0
    var roomId : Long = 0
    var ownerId : Long = 0
    var ownerName : String = ""
    lateinit var roomInfo : ChattingRoom
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattingroom_info)

        chattingInfoParticipateButton.isActivated = true
        chattingInfoParticipateButton.visibility = View.VISIBLE
        chattingInfoAlreadyParticipatedTextView.visibility = View.INVISIBLE

        roomId = intent.extras!!["roomId"] as Long
        userId = intent.extras!!["userId"] as Long

        //채팅방 정보 띄우기 위해 DB 읽는 부분.
        var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom")
        var roomValueEventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children){
                    if(roomId == data.child("roomId").value as Long){
                        roomInfo = ChattingRoom(
                            data.child("roomId").value as Long,
                            data.child("title").value.toString(),
                            data.child("description").value.toString(),
                            data.child("owner").value as Long,
                            data.child("locationX").value as Double,
                            data.child("locationY").value as Double,
                            data.child("locationName").value.toString(),
                            data.child("locationCertified").value as Boolean,
                            data.child("timestamp").value as Long,
                            data.child("uid").value as Long
                        )
                        chattingInfoTitleView.text = roomInfo.title
                        if(data.child("locationCertified").value as Boolean){
                            chattingInfoLocationCertifiedTextView.text = "Location(Certified) : ${data.child("locationName").value.toString()}"
                        }
                        else{
                            chattingInfoLocationCertifiedTextView.text = "Location(Not Certified) : ${data.child("locationName").value.toString()}"
                        }

                        chattingInfoDescriptionTextView.text = roomInfo.description
                        ownerId = data.child("owner").value as Long
                        break
                    }
                }
            }

        }
        roomRef.addListenerForSingleValueEvent(roomValueEventListener)

        //참여자 목록 보여주기 위해 DB 읽는 부분.
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
                        if(ownerId == data.child("userId").value as Long){
                            ownerName = data.child("name").value.toString()
                            chattingInfoOwnerTextView.text = ownerName
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


        var checkPartRef = FirebaseDatabase.getInstance().getReference("participation")
        var checkPartValueEventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    if(data.child("userId").value as Long == userId && data.child("roomId").value as Long == roomId){
                        chattingInfoParticipateButton.isActivated = false
                        chattingInfoAlreadyParticipatedTextView.visibility = View.VISIBLE
                        chattingInfoParticipateButton.visibility = View.INVISIBLE
                        break
                    }
                }
            }

        }
        checkPartRef.addListenerForSingleValueEvent(checkPartValueEventListener)

        //참여 버튼 핸들러
        chattingInfoParticipateButton.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setMessage("You Participated in Room : ${roomInfo.title}")
            builder.show()
            var newPartRef = FirebaseDatabase.getInstance().getReference("participation")
            var newPartUid = System.currentTimeMillis()
            newPartRef.child("${newPartUid}").setValue(Participation(userId, roomId, newPartUid))

            chattingInfoParticipateButton.isActivated = false
            chattingInfoAlreadyParticipatedTextView.visibility = View.VISIBLE
            chattingInfoParticipateButton.visibility = View.INVISIBLE


        }

    }
}