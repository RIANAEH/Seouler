package com.example.seouler.APITask

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Dimension
import com.example.seouler.PlaceLikingActivity
import com.example.seouler.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class GetDetailTask(val contentId: String, val view: PlaceLikingActivity) : AsyncTask<Any?, Any?, Any?>() {
    var buffer:String? = null
    var firstimage: Bitmap? = null
    var title: String? = null
    var introduction: String? = null
    var addr1: String? = null
    var tel: String? = null
    var homepage: String? = null
    var directions: String? = null

    override fun doInBackground(vararg params: Any?): Any? {

        var urlString =
            "http://api.visitkorea.or.kr/openapi/service/rest/EngService/detailCommon?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=Seouler&contentId="+contentId+"&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&overviewYN=Y&transGuideYN=Y&_type=json"
        Log.d("태그", "url: $urlString")
        try {
            val url: URL = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(
                    InputStreamReader(
                        connection.inputStream,
                        "UTF-8"
                    )
                )
                buffer = reader.readLine()
            }
        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }


        // json parsing
        try {
            val jobj = JSONObject(buffer)
            val robj = jobj.getJSONObject("response")
            val bobj = robj.getJSONObject("body")
            val iobj = bobj.getJSONObject("items")
            val obj = iobj.getJSONObject("item")

            if(obj.has("firstimage")) firstimage = BitmapFactory.decodeStream(URL(obj.getString("firstimage")).openStream())
            if(obj.has("title"))title = obj.getString("title")
            if(obj.has("overview"))introduction = obj.getString("overview")
            if(obj.has("addr1"))addr1 = obj.getString("addr1")
            if(obj.has("addr2")) addr1 = addr1+" "+obj.getString("addr2")
            if(obj.has("tel")) tel = obj.getString("tel")
            if(obj.has("homepage")) homepage = obj.getString("homepage")
            if(obj.has("directions")) directions = obj.getString("directions")


        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }

        return null
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        // 받아온 정보 보여주기
        view.findViewById<ImageView>(R.id.lp_firstimage).setImageBitmap(firstimage)
        view.findViewById<TextView>(R.id.lp_title).setText(title)
        view.findViewById<TextView>(R.id.lp_introduction).setText(Html.fromHtml(introduction, Html.FROM_HTML_MODE_LEGACY))
        view.findViewById<TextView>(R.id.lp_addr1).setText(addr1)
        if(tel!=null) view.findViewById<TextView>(R.id.lp_tel).setText(tel)
        else view.findViewById<TextView>(R.id.lp_tel).setText("no data")
        view.findViewById<TextView>(R.id.lp_homepage).setText(Html.fromHtml(homepage, Html.FROM_HTML_MODE_LEGACY))
        view.findViewById<TextView>(R.id.lp_directions).setText(Html.fromHtml(directions, Html.FROM_HTML_MODE_LEGACY))
    }
}