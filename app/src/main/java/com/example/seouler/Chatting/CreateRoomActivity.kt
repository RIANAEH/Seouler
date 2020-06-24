package com.example.seouler.Chatting

import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.LocationCustomData
import com.example.seouler.dataClass.Participation
import com.example.seouler.dataClass.StaticSeoulLocationData
import com.google.firebase.database.FirebaseDatabase
import com.example.seouler.dataClass.Location as LocationData
import kotlinx.android.synthetic.main.activity_create_room.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CreateRoomAcvitivy : AppCompatActivity() {
    var locationStringArray: ArrayList<String> = ArrayList()
    var selectedLocation : String = ""
    lateinit var myLocation : LocationData
    var locationCertified : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)
        myLocation = intent.extras!!["myLocation"] as LocationData
        setLocationStringArray()


        /* 서울에 존재하는 지명들을 모두 긁어와 locationStringArray에 넣어줌 */
        createroomLocationSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationStringArray)
        Log.d("Location",createroomLocationSpinner.selectedItem.toString())

        //spinner 리스너
        createroomLocationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                locationCertified = false
            }

        }
        
        //현재 위치 받아오기 버튼 리스너

        createroomLocationImageButton.setOnClickListener {
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
            }
            if(rResultList != null) {
//              createroomLocationSpinner.setSelection()
                locationCertified = certifyLocation()
                Log.d("location 역지오코딩", rResultList.toString())
            }
        }

        
        //채팅방 생성 버튼 리스너
        createroomConfirmButton.setOnClickListener {
            if(createroomTitleTextView.text.isBlank()){
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Please Input Title.")
                builder.show()
            }
            else if(createroomDescriptionTextView.text.isBlank()){
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Please Input Description.")
                builder.show()
            }
            else {
                var roomRef = FirebaseDatabase.getInstance().getReference("chattingRoom")
                var uid = System.currentTimeMillis()

                var thisLocation = LocationCustomData("", 1.1, 1.1)
                StaticSeoulLocationData.data.get(createroomLocationSpinner.selectedItem.toString())?.let { it1 ->
                    var newRoom = ChattingRoom(
                        uid,
                        createroomTitleTextView.text.toString(),
                        createroomDescriptionTextView.text.toString(),
                        intent.extras!!["userId"] as Long,
                        it1.locationX,
                        it1.locationY,
                        locationCertified,
                        uid,
                        uid
                    )
                    roomRef.child("${uid}").setValue(newRoom)
                    var partRef = FirebaseDatabase.getInstance().getReference("participation")
                    var partUid = System.currentTimeMillis()
                    var participation = Participation(intent.extras!!["userId"] as Long, uid, partUid)
                    partRef.child("${partUid}").setValue(participation)

                    var builder = AlertDialog.Builder(this)
                    builder.setMessage("New Chatting Room is Created!!")
                    var createroomDialogListner = object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            onBackPressed()
                        }

                    }
                    builder.setPositiveButton("OK", createroomDialogListner)
                    builder.show()
                }
            }
        }
    }
    fun certifyLocation() : Boolean{
        if(myLocation.toString() != LocationData(1.1, 1.1).toString()){
            myLocation = intent.extras!!["myLocation"] as LocationData
            return true
        }
        return false
    }
    fun setLocationStringArray(){
        locationStringArray = ArrayList(StaticSeoulLocationData.data.keys)
        locationStringArray.sort()
    }

}