package com.example.seouler.Chatting

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import com.example.seouler.dataClass.Location as LocationData
import kotlinx.android.synthetic.main.activity_create_room.*

class CreateRoomAcvitivy : AppCompatActivity() {
    var locationStringArray : ArrayList<String> = ArrayList()
    lateinit var myLocation : LocationData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)

        locationStringArray.add("Gangnam")
        locationStringArray.add("Seocho")

        /* 서울에 존재하는 지명들을 모두 긁어와 locationStringArray에 넣어줌 */
        createroomLocationSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locationStringArray)
    }


}