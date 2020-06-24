package com.example.seouler

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.seouler.Chatting.ChattingHomeActivity
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.Message
import com.example.seouler.dataClass.Participation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import com.google.android.gms.location.*
import com.example.seouler.dataClass.Location as LocationData

var set_rate_index : Int = 0

class MainActivity : AppCompatActivity() {
    var myChattingRoomList : ArrayList<ChattingRoom> = ArrayList()
    var myChattingRoomMessageList : ArrayList<ArrayList<Message>> = ArrayList()
    val permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    var locationPermittedAll = false
    var myLocation : LocationData = LocationData(1.1, 1.1)
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
        /* 안드로이드 단말 번호를 UserId로 사용 */
        var tm : TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var androidId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        val USERID : Long = 1592656608691

        /* MainActivity에서 먼저 DB로부터 필요한 데이터 읽어오는 과정 필요 */
        //loadMyChattingRoom(USERID)
        //Thread.sleep(2000)

        mainChattingButton.setOnClickListener {
            val chattingRoomIntent = Intent(this, ChattingHomeActivity::class.java)
            chattingRoomIntent.putExtra("userId", USERID)
            chattingRoomIntent.putExtra("chattingroomList", myChattingRoomList)
            chattingRoomIntent.putExtra("myLocation", myLocation)
            startActivity(chattingRoomIntent)
        }

        mainItineraryButton.setOnClickListener {
            val iti_intent = Intent(this, Recycle_MainActivity::class.java)
            //cc.execute()

            startActivity(iti_intent)


        }
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
