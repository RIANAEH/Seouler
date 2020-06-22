package com.example.seouler

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.dataClass.ChattingRoom
import kotlinx.android.synthetic.main.rvitem_chattinghome.view.*

class ChattingHomeAdapter : RecyclerView.Adapter<Holder>(){
    var listData = ArrayList<ChattingRoom>()
    lateinit var chattingHomeContext : Context

    override fun getItemCount(): Int {
        return listData.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvitem_chattinghome, parent, false)
        val holder = Holder(view)
        holder.chattingHomeContext = chattingHomeContext
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val room = listData.get(position)
        holder.setChattingRoom(room)
    }
}
class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
    lateinit var chattingHomeContext : Context
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
        itemView.chattingroomCellButton.setOnClickListener {
            val roomIntent = Intent(chattingHomeContext, ChattingMessageActivity::class.java)
            startActivity(chattingHomeContext, roomIntent, null)
        }
    }
}