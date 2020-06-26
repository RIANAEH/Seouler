package com.example.seouler.Chatting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.dataClass.User
import kotlinx.android.synthetic.main.rvitem_chatting_info.view.*
import kotlinx.android.synthetic.main.rvitem_chattinghome.view.*

class ChattingInfoAdapter(val context: Context, var listData : ArrayList<String>) : RecyclerView.Adapter<ChattingInfoHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingInfoHolder {
        // 안에 담을 셀을 정의하고 홀더 생성.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chatting_info, parent, false)
        val holder = ChattingInfoHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ChattingInfoHolder, position: Int) {
        var name = listData[position]
        holder.setChattingRoom(name, position)
    }
}
class ChattingInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(name : String, position : Int){
        itemView.chattingInfoRecyclerViewParticipantNameTextView.text = name
    }
}