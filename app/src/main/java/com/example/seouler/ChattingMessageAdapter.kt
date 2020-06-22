package com.example.seouler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.dataClass.Message
import kotlinx.android.synthetic.main.rvitem_chattingmessage.view.*
import kotlinx.android.synthetic.main.rvitem_chattingmessage_me.view.*

class ChattingMessageAdapter(val context: Context, var listData : ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int {
        return listData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        var view : View
        if(viewType == 1){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattingmessage, parent, false)
            return LogHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattingmessage, parent, false)
            return MyLogHolder(view)
        }

    }


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val log = listData.get(position)
        if(viewHolder is LogHolder){
            viewHolder.setChattingRoom(log)
        }
        else if(viewHolder is MyLogHolder){
            viewHolder.setChattingRoom(log)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position%2 == 0){
            1
        } else {
            2
        }
    }

}
class LogHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(msg : Message){
        itemView.chattingMessageNameTextView.text = "${msg.sender}"
        itemView.chattingMessageTextView.text = "${msg.text}"
        itemView.chattingMessageTimestampTextView.text = "${msg.timestamp +1} min ago"

    }
}
class MyLogHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(msg : Message){
        itemView.chattingMessageMeTextView.text = "${msg.text}"
        itemView.chattingMessageMeTimestampTextView.text = "${msg.timestamp +1} min ago"
    }
}