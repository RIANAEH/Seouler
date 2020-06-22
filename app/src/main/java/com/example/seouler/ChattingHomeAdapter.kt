package com.example.seouler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.rvitem_chattingroom.view.*

class ChattingHomeAdapter : RecyclerView.Adapter<Holder>(){
    var listData = ArrayList<ChattingRoom>()

    override fun getItemCount(): Int {
        return listData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattingroom, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val room = listData.get(position)
        holder.setChattingRoom(room)
    }
}
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setChattingRoom(room : ChattingRoom){
        itemView.chattingroomCellButton.text = "${room.title}"
        var time : Long = ((System.currentTimeMillis()-room.timestamp)/1000)/60
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
        itemView.chattingroomCellTimestampTextView.text = timeMessage
    }
}