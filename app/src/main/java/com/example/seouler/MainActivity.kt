package com.example.seouler

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.seouler.Chatting.ChattingHomeActivity
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Message
import com.example.seouler.dataClass.Participation
import com.example.seouler.dataClass.a_exchange
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.example.seouler.dataClass.Location as LocationData

var set_rate_index : Int = 0

class MainActivity : AppCompatActivity() {
    var myChattingRoomList : ArrayList<ChattingRoom> = ArrayList()
    var myChattingRoomMessageList : ArrayList<ArrayList<Message>> = ArrayList()
    val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    var locationPermittedAll = false
    var myLocation : LocationData = LocationData(0.0, 0.0)
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationCallback:LocationCallback
    var iti_intent = Intent() // Itinerary Activity send

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //위치정보 권한 요청
        checkPermission()
        //위치정보 1초마다 업데이트해서 변수 myLocation에 넣음.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()

        var cc = Weather_Async(this) // API Weather
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager //GPS
        val gpsLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var provider = location?.provider;
                var longitude = location?.longitude;
                var latitude = location?.latitude;
                myLocation.locationX = longitude!!
                myLocation.locationY = latitude!!
                Log.d("MainActivity_Location","location : ${myLocation.locationX},${myLocation.locationY}")
                /*txtResult.setText(
                    "위치 : " + provider + "\n"
                            + "위도 : " + longitude + "\n"
                            + "경도 : " + latitude + "\n"
                )*/
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}

        }


        /* 임시 UserId 사용 */
        val USERID : Long = 1592656608691

        /* MainActivity에서 먼저 DB로부터 필요한 데이터 읽어오는 과정 필요 */
        loadMyChattingRoom(USERID)
        Thread.sleep(2000)

        mainChattingButton.setOnClickListener {
            val chattingRoomIntent = Intent(this, ChattingHomeActivity::class.java)
            chattingRoomIntent.putExtra("userId", USERID)
            chattingRoomIntent.putExtra("chattingroomList", myChattingRoomList)
            chattingRoomIntent.putExtra("myLocation", myLocation)
            startActivity(chattingRoomIntent)
        }

        btn_itinerary.setOnClickListener {
            val iti_intent = Intent(this, Recycle_MainActivity::class.java)
            //cc.execute()

            startActivity(iti_intent)


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
                                partSnapshot.child("roomId").value as Long,
                                partSnapshot.child("uid").value as Long
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
                                    partSnapshot.child("timestamp").value as Long,
                                    partSnapshot.child("uid").value as Long
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

    fun checkPermission(){
        var permitted_all = true
        for (permission in permissions){
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED){
                permitted_all = false
                requestPermission()
                break
            }
        }
        locationPermittedAll = permitted_all
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions, 99)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            99 -> {
                var granted_all = true
                for (result in grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        granted_all = false
                        break
                    }
                }
            }
        }
    }
    fun updateLocation(){
        val locationRequest = LocationRequest.create()
        locationRequest.run{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 100
        }
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let{
                    for((i, location) in it.locations.withIndex()){
                        Log.d("Location", "$i ${location.latitude}, ${location.longitude}")
                        myLocation.locationX = location.longitude
                        myLocation.locationY = location.latitude
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
}
