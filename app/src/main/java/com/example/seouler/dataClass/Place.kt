package com.example.seouler.dataClass

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.net.URL


class Place (title : String, dist : String, contentid : String, imgUrl : String, isImgNull : Boolean) {
    private val contentid : String = contentid
    val imgUrl = imgUrl
    val title: String = title
    val dist: String = dist
    val isImgNull = isImgNull


}

