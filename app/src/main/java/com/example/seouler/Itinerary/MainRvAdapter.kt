package com.example.seouler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.dataClass.a_plan

class MainRvAdapter(val context: Context, val planlist : ArrayList<a_plan>) :
    RecyclerView.Adapter<MainRvAdapter.Holder>() {

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
        val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return planlist.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(planlist[position], context)
    }



    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val time = itemView?.findViewById<TextView>(R.id.tv_time)
        val dest = itemView?.findViewById<TextView>(R.id.tv_destination)

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

        fun bind (aplan: a_plan, context: Context) {
            /* 나머지 TextView와 String 데이터를 연결한다. */
            time?.text = aplan.time
            dest?.text = aplan.destination
        }
    }
}