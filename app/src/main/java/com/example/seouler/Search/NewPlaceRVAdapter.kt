package com.example.seouler.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seouler.R
import com.example.seouler.dataClass.SearchPlace

class NewPlaceRVAdapter(context: Context, val placelist: ArrayList<SearchPlace>) :
    RecyclerView.Adapter<NewPlaceRVAdapter.Holder>() {

    var context = context

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    // 리스너 객체 참조를 저장하는 변수
    private var mListener: OnItemClickListener? = null

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.new_search_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return placelist.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(placelist[position], context)
    }


    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val time = itemView?.findViewById<TextView>(R.id.tv_title)
        val addr = itemView?.findViewById<TextView>(R.id.tv_addr)
        val img = itemView?.findViewById<ImageView>(R.id.iv_icon)

        init {
            if (itemView != null) {
                itemView.setOnClickListener(View.OnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener!!.onItemClick(it, pos)
                        }
                    }
                })
            }


        }

        fun bind(aplace: SearchPlace, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            println("<BIND> MainRVAdapter_ ${aplace.title}")
            if(!aplace.isImgNull){
                this.img?.let { Glide.with(context).load(aplace.imgUrl).into(it) }

             }
            time?.text = aplace.title
            addr?.text = aplace.addr
        }
    }

}



