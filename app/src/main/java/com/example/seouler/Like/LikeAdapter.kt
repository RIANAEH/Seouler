package com.example.seouler.Like

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seouler.PlaceInfoActivity
import com.example.seouler.R
import com.example.seouler.Recommend.ContentItem
import kotlinx.android.synthetic.main.list_like.view.*

class LikeAdapter(private val items: Array<ContentItem>) :
    RecyclerView.Adapter<LikeAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            LikeAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_like, parent, false)
        return LikeAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bind(listener: View.OnClickListener, item: ContentItem) {
            Glide.with(view!!.context).load(item.firstimage).into(view.like_image);
            view.like_title.text = item.title
            view.setOnClickListener(listener)
        }
    }

    override fun onBindViewHolder(holder: LikeAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            val detail_intent = Intent(it.context, PlaceInfoActivity::class.java)
            detail_intent.putExtra("contentId", item.contentid)
            ContextCompat.startActivity(it.context, detail_intent, null)
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }
}