package com.example.seouler.Recommend

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seouler.APITask.SaveToLikeTask
import com.example.seouler.Like.LikeMainActivity
import com.example.seouler.Like.map_like
import com.example.seouler.PlaceInfoActivity
import com.example.seouler.R
import kotlinx.android.synthetic.main.list_content.view.*

class ContentAdapter(private val items: ArrayList<ContentItem>) :
    RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ContentAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            val detail_intent = Intent(it.context, PlaceInfoActivity::class.java)
            detail_intent.putExtra("contentId", item.contentid)
            startActivity(it.context, detail_intent, null)
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ContentAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_content, parent, false)
        return ContentAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        var like_check: Boolean = false


        fun bind(listener: View.OnClickListener, item: ContentItem) {
            // 찜리스트를 검사해 리스트에 존재하느냐에 따라 결정한다.
            if(map_like.containsKey(item.contentid)) {
                view.favorite.setBackgroundResource(R.drawable.heart_like)
                like_check = true
            }
            else {
                view.favorite.setBackgroundResource(R.drawable.heart_unlike)
                like_check = false
            }

            view.favorite.setOnClickListener {
                // 좋아요 여부 반전 토글 후 저장
                if(like_check) {
                    it.setBackgroundResource(R.drawable.heart_unlike)
                    like_check = false
                    map_like.remove(item.contentid)
                    val txt = "I don't like this place!"
                    Toast.makeText(view!!.context, txt, Toast.LENGTH_SHORT).show()

                }
                else {
                    it.setBackgroundResource(R.drawable.heart_like)
                    like_check = true
                    item.contentid?.let { it1 -> showLikeDialog(it1) }
                    item.contentid?.let { it1 -> SaveToLikeTask(it1).execute().get() }
                }
            }

            Glide.with(view!!.context).load(item.firstimage).into(view.thumbnail);

            view.title.text = item.title
            view.setOnClickListener(listener)
        }

        fun showLikeDialog(contentId: String) {
            val builder = AlertDialog.Builder(view.context, R.style.AlertDialogTheme)

            builder.setTitle("Go to Like page??")

            builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                // LikeMainActivity로 이동
                val intent = Intent(view.context, LikeMainActivity::class.java)
                intent.putExtra("contentId", contentId)
                startActivity(view.context, intent, null)
            }

            builder.setNegativeButton("No", null)

            val dialog : AlertDialog = builder.create()

            dialog.show()
        }
    }


}