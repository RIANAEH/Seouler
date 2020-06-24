package com.example.seouler.Chatting

import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.LocationCustomData
import com.example.seouler.dataClass.Participation
import com.example.seouler.dataClass.StaticSeoulLocationData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_room.*
import kotlinx.android.synthetic.main.activity_search_room.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import com.example.seouler.dataClass.Location as LocationData

class SearchRoomActivity : AppCompatActivity() {
    var locationStringArray : ArrayList<String> = ArrayList()
    var resultList : ArrayList<ChattingRoom> = ArrayList()
    lateinit var myLocation : LocationData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_room)

        myLocation = intent.extras!!["myLocation"] as LocationData
        setLocationString()
        searchroomLocationSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationStringArray)


        //채팅방 검색 위치 받아오기 버튼 리스너
        searchroomLocationCertifyImageButton.setOnClickListener {
            var rGeocoder = Geocoder(applicationContext, Locale.US)
            var rResultList : List<Address>? = null
            try{
                rResultList = rGeocoder.getFromLocation(
                    myLocation.locationY,
                    myLocation.locationX,
                    1
                )

            } catch(e: IOException){
                e.printStackTrace()
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Failed to Find Your Location")
                var searchroomDialogListner = object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                    }

                }
                builder.setPositiveButton("OK", searchroomDialogListner)
                builder.show()
            }
            if(rResultList != null && rResultList.count() > 0) {
                Log.d("usbin", "${rResultList[0].subLocality}")
                createroomLocationSpinner.setSelection(locationStringArray.indexOf("${rResultList[0].subLocality}"))
            }
            else{
            }
        }
        
        //채팅방 검색 확인 버튼 리스너
        var adapter = SearchRoomAdapter()
        searchroomConfirmButton.setOnClickListener {
            resultList.clear()
            var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom")
            StaticSeoulLocationData.data.get(searchroomLocationSpinner.selectedItem.toString())?.let { it1 ->
                var roomListner = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var rGeocoder = Geocoder(applicationContext, Locale.US)
                        var rResultList : List<Address>? = null

                        for (roomSnapshot in dataSnapshot.children) {
                            //createroomLocationSpinner.setSelection(locationStringArray.indexOf("${rResultList[0].subLocality}"))
                            //locationCertified = true
                            //createroomLocationSpinner.isEnabled = false
                            if (roomSnapshot.child("locationName").value.toString() == searchroomLocationSpinner.selectedItem.toString()) {
                                resultList.add(
                                    ChattingRoom(
                                        roomSnapshot.child("roomId").value as Long,
                                        roomSnapshot.child("title").value.toString(),
                                        roomSnapshot.child("description").value.toString(),
                                        roomSnapshot.child("owner").value as Long,
                                        roomSnapshot.child("locationX").value as Double,
                                        roomSnapshot.child("locationY").value as Double,
                                        roomSnapshot.child("locationName").value.toString(),
                                        roomSnapshot.child("locationCertified").value as Boolean,
                                        roomSnapshot.child("timestamp").value as Long,
                                        roomSnapshot.child("uid").value as Long
                                    )
                                )
                            }
                        }
                        if(!searchroomTitleTextview.text.isBlank() && !resultList.isEmpty()){
                            //위치+제목으로 검색할 경우 제목으로 한 번 더 필터링
                            for(i in resultList.count()-1 .. 0)
                                if(searchroomTitleTextview.text.toString() != resultList[i].title){
                                    resultList.removeAt(i)
                                }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                roomRef.addListenerForSingleValueEvent(roomListner)
            }


        }


        adapter.listData = resultList
        adapter.userId = intent.extras!!["userId"] as Long
        adapter.searchRoomContext = this
        searchroomResultRecyclerView.adapter = adapter
        searchroomResultRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    fun setLocationString(){
        locationStringArray = ArrayList(StaticSeoulLocationData.data.keys)
        locationStringArray.sort()
    }
}