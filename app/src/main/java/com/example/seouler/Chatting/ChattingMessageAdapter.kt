package com.example.seouler.Chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R
import com.example.seouler.dataClass.Message
import kotlinx.android.synthetic.main.rvitem_chattingmessage.view.*
import kotlinx.android.synthetic.main.rvitem_chattingmessage_me.view.*

class ChattingMessageAdapter(val context: Context, var listData : ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var USERID : Long = 0
    override fun getItemCount(): Int {
        return listData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        var view : View
        if(viewType == 1){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattingmessage, parent, false)
            return MessageHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattingmessage_me, parent, false)
            return MyMessageHolder(view)
        }

    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val msg = listData.get(position)
        if(viewHolder is MessageHolder){
            viewHolder.setChattingRoom(msg)
        }
        else if(viewHolder is MyMessageHolder){
            viewHolder.setChattingRoom(msg)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(USERID != listData.get(position).sender){
            1
        } else {
            2
        }
    }

}
class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(msg : Message){
        itemView.chattingMessageNameTextView.text = "${msg.sender}"
        itemView.chattingMessageTextView.text = "${msg.text}"
        var time : Long = ((System.currentTimeMillis()-msg.timestamp)/1000)/60
        var timeMessage : String = ""
        //경과 시간이 60분보다 크다면
        if(time >= 60){
            time /= 60
            if(time >= 24){
                time /= 24
                if(time >= 3){
                    timeMessage = "Long ago"
                }
                else{
                    timeMessage = "${time} day ago"
                }
            }
            else {
                timeMessage = "${time} hour ago"
            }
        }
        else{
            timeMessage = "${time} min ago"
        }
        itemView.chattingMessageTimestampTextView.text = timeMessage

    }
}
class MyMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(msg : Message){
        itemView.chattingMessageMeTextView.text = "${msg.text}"
        var time : Long = ((System.currentTimeMillis()-msg.timestamp)/1000)/60
        var timeMessage : String = ""
        //경과 시간이 60분보다 크다면
        if(time >= 60){
            time /= 60
            if(time >= 24){
                time /= 24
                if(time >= 3){
                    timeMessage = "Long ago"
                }
                else{
                    timeMessage = "${time} day ago"
                }
            }
            else {
                timeMessage = "${time} hour ago"
            }
        }
        else{
            timeMessage = "${time} min ago"
        }
        itemView.chattingMessageMeTimestampTextView.text = timeMessage
    }
}