package com.example.seouler.Chatting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search_room.*
import java.util.*
import kotlin.collections.ArrayList

class SearchRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_room)



        val data:ArrayList<ChattingRoom> = loadSearchData()
        var adapter = SearchRoomAdapter()
        adapter.listData = data
        adapter.userId = intent.extras!!["userId"] as Long
        adapter.createRoomContext = this
        rv_searchroom_result.adapter = adapter
        rv_searchroom_result.layoutManager = LinearLayoutManager(this)
    }
    fun loadSearchData() : ArrayList<ChattingRoom>{
        var data:ArrayList<ChattingRoom> = ArrayList()
        for (i in 1..10) {
            val searched = ChattingRoom(1,
                title="${i}st Room",
                description = "${i}st Room",
                owner=2,
                locationX=1.0,
                locationY=2.0,
                locationCertified = false,
                timestamp = 20100210,
                uid = 123451234)
            data.add(searched)
        }
        return data
    }
}