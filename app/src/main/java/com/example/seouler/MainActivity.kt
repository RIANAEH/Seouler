package com.example.seouler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Message
import com.example.seouler.dataClass.Participation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var myChattingRoomList : ArrayList<ChattingRoom> = ArrayList()
    var myChattingRoomMessageList : ArrayList<ArrayList<Message>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* 임시 UserId 사용 */
        val USERID : Long = 1592656608691

        /* MainActivity에서 먼저 DB로부터 필요한 데이터 읽어오는 과정 필요 */
        loadMyChattingRoom(USERID)
        loadMyChattingRoomMessage(USERID)
        Thread.sleep(2000)

        mainChattingButton.setOnClickListener {
            val chattingRoomIntent = Intent(this, ChattingHomeActivity::class.java)
            chattingRoomIntent.putExtra("userId", USERID)
            chattingRoomIntent.putExtra("chattingroomList", myChattingRoomList)
            startActivity(chattingRoomIntent)
        }
    }
    fun loadMyChattingRoom(USERID : Long){
        var partData : ArrayList<Participation> = ArrayList()
        var myRoomIDList : ArrayList<Long> = ArrayList()
        val partRef = FirebaseDatabase.getInstance().getReference("participation")
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (partSnapshot in dataSnapshot.children) {
                    Log.d("MainActivity", partSnapshot.toString())
                    if(USERID == partSnapshot.child("userId").value as Long){
                        Log.d("MainActivity","C read userId : ${partSnapshot.child("userId").value as Long}")
                        partData.add(
                            Participation(
                                partSnapshot.child("userId").value as Long,
                                partSnapshot.child("roomId").value as Long
                            )
                        )
                    }

                }
                for (i in 0 until partData.count()){
                    //본인의 채팅방만 불러오기
                    if(partData[i].userId == USERID){
                        myRoomIDList.add(partData[i].roomId)
                        Log.d("MainActivity", "D ${myRoomIDList[i]}")
                    }
                }
            }

        }
        partRef.addValueEventListener(valueEventListener)


        var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom")
        val roomValueEventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myChattingRoomList.clear()
                for (partSnapshot in dataSnapshot.children) {
                    for(i in 0 until myRoomIDList.count()){
                        if(myRoomIDList[i] == partSnapshot.child("roomId").value as Long){
                            Log.d("MainActiviry", "F Yes")
                            myChattingRoomList.add(
                                ChattingRoom(
                                    partSnapshot.child("roomId").value as Long,
                                    partSnapshot.child("title").value.toString(),
                                    partSnapshot.child("description").value.toString(),
                                    partSnapshot.child("owner").value as Long,
                                    partSnapshot.child("locationX").value as Double,
                                    partSnapshot.child("locationY").value as Double,
                                    partSnapshot.child("locationCertified").value as Boolean,
                                    partSnapshot.child("timestamp").value as Long
                                )
                            )
                        }
                    }
                }
                Log.d("MainActiviry","G room count : ${myChattingRoomList.count()}")
            }

        }
        roomRef.addValueEventListener(roomValueEventListener)
    }

    fun loadMyChattingRoomMessage(USERID: Long){
        /* myChattingRoomList에서 roomId를 Key값으로, Message 객체를 value값으로 하여 DB로부터 데이터 가져옴. */
        var msgRef = FirebaseDatabase.getInstance().getReference("message")
        val msgValueEventListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myChattingRoomMessageList.clear()
                for(i in 0 until myChattingRoomList.count()){
                    for(msgSnapshot in dataSnapshot.children){
                        if(myChattingRoomList[i].roomId == msgSnapshot.child("roomId") as Long){
                            myChattingRoomMessageList[i].add(
                                Message(
                                msgSnapshot.child("messageId") as Long,
                                msgSnapshot.child("roomId") as Long,
                                msgSnapshot.child("sender") as Long,
                                msgSnapshot.child("text").value.toString(),
                                msgSnapshot.child("timestamp") as Long)
                            )
                        }
                    }
                }

            }

        }
        msgRef.addValueEventListener(msgValueEventListener)
    }
}
