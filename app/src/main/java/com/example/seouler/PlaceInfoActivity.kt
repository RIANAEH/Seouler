package com.example.seouler

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.APITask.GetDetailTask
import com.example.seouler.APITask.SaveToLikeTask
import com.example.seouler.Like.LikeMainActivity
import com.example.seouler.Like.map_like
import kotlinx.android.synthetic.main.activity_like_place.*

class PlaceInfoActivity : AppCompatActivity() {
    lateinit var contentId: String
    var like_check: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_place)

        contentId = intent.extras!!["contentId"].toString()
        // contentId로 api호출 -> 세부정보 받아서 디스플레이
        GetDetailTask(contentId, this).execute()

        // 찜리스트를 검사해 리스트에 존재하느냐에 따라 결정한다.
        if(map_like.containsKey(contentId)) {
            lp_btn_like.setBackgroundResource(R.drawable.heart_like)
            like_check = true
        }
        else {
            lp_btn_like.setBackgroundResource(R.drawable.heart_unlike)
            like_check = false
        }

        lp_btn_like.setOnClickListener {
            // 좋아요 여부 반전 토글 후 저장
            if(like_check) {
                it.setBackgroundResource(R.drawable.heart_unlike)
                like_check = false
                map_like.remove(contentId)
                val txt = "I don't like this place!"
                Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()

            }
            else {
                it.setBackgroundResource(R.drawable.heart_like)
                like_check = true
                showLikeDialog(contentId)
                SaveToLikeTask(contentId).execute().get()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        if(map_like.containsKey(contentId)) {
            lp_btn_like.setBackgroundResource(R.drawable.heart_like)
            like_check = true
        }
        else {
            lp_btn_like.setBackgroundResource(R.drawable.heart_unlike)
            like_check = false
        }
    }

    fun showLikeDialog(contentId: String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        builder.setTitle("Go to Like page??")

        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            // LikeMainActivity로 이동
            val intent = Intent(this, LikeMainActivity::class.java)
            intent.putExtra("contentId", contentId)
            startActivity(intent)
        }

        builder.setNegativeButton("No", null)

        val dialog : AlertDialog = builder.create()

        dialog.show()
    }

}
