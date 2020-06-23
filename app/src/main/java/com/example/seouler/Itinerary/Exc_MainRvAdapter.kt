package com.example.seouler.Itinerary

import com.example.seouler.dataClass.a_exchange





import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R

class Exc_MainRvAdapter(val context: Context, val exclist : ArrayList<a_exchange>) :
    RecyclerView.Adapter<Exc_MainRvAdapter.Holder>() {

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
        val view = LayoutInflater.from(context).inflate(R.layout.exc_main_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return exclist.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(exclist[position], context)
    }



    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val nation = itemView?.findViewById<TextView>(R.id.tv_nation)
        val rate_unit = itemView?.findViewById<TextView>(R.id.tv_rateUnit)
        val excrate = itemView?.findViewById<TextView>(R.id.tv_exchangeRate)

        init{
            if (itemView != null) {
                itemView.setOnClickListener(View.OnClickListener {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener!!.onItemClick(it, pos)
                            //Toast.makeText(context,"asdf"+pos, Toast.LENGTH_SHORT).show()
                            //var intent = Intent(context, PlanModifyActivity::class.java)
                            //intent.putExtra("position",pos)
                        }
                    }
                })
            }



        }

        fun bind (
            aexchange: a_exchange,
            context: Context
        ) {
            /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
            이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/

            /* 나머지 TextView와 String 데이터를 연결한다. */
            nation?.text = aexchange.nation
            rate_unit?.text = aexchange.rateUnit
            excrate?.text = aexchange.exchangeRate.toString()
        }
    }
}