package com.example.seouler.APITask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.example.seouler.Recommend.ContentItem
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class GetDetailTask(val contentId: String) : AsyncTask<Any?, Any?, Any?>() {
    var buffer:String? = null
    var firstimage: Bitmap? = null
    //var firstimage: String? = null
    var title: String? = null
    var introduction: String? = null
    var addr1: String? = null
    //var addr2: String? = null
    var tel: String? = null
    var homepage: String? = null
    var directions: String? = null

    override fun doInBackground(vararg params: Any?): Any? {

        // 현재 기본적으로 20개의 content를 가져오며 numOfRows를 변경하여 개수를 수정할 수 있다.
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
                //Log.d("태그", "$buffer")
            }
        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }


        // json parsing
        try {
            val jobj = JSONObject(buffer)
            //Log.d("태그", "OK")
            val robj = jobj.getJSONObject("response")
            val bobj = robj.getJSONObject("body")
            val iobj = bobj.getJSONObject("items")
            //val jarray = iobj.getJSONArray("item")
            val obj = iobj.getJSONObject("item")

            //val firstimageurl = obj.getString("firstimage")
            if(obj.has("firstimage")) firstimage = BitmapFactory.decodeStream(URL(obj.getString("firstimage")).openStream())
            //Log.d("태그", "image: $firstimageurl")
            //firstimage = obj.getString("firstimage")
            if(obj.has("title"))title = obj.getString("title")
            Log.d("태그", "title: $title")
            if(obj.has("overview"))introduction = obj.getString("overview")
            Log.d("태그", "introduction: $introduction")
            if(obj.has("addr1"))addr1 = obj.getString("addr1")
            if(obj.has("addr2")) addr1 = addr1+" "+obj.getString("addr2")
            if(obj.has("tel")) tel = obj.getString("tel")
            Log.d("태그", "tel: $tel")
            if(obj.has("homepage")) homepage = obj.getString("homepage")
            Log.d("태그", "homepage: $homepage")
            if(obj.has("directions")) directions = obj.getString("directions")
            Log.d("태그", "directions: $directions")

            //firstimage = BitmapFactory.decodeStream(URL(firstimageurl).openStream())

            /*
            for(i in 0 until jarray.length()) {
                val obj = jarray.getJSONObject(i)
                val firstimageurl = obj.getString("firstimage")
                title = obj.getString("title")
                introduction = obj.getString("overview")
                addr1 = obj.getString("addr1")
                addr2 = obj.getString("addr2")
                tel = obj.getString("tel")
                homepage = obj.getString("homepage")
                directions = obj.getString("directions")

                // 이미지 url로부터 이미지를 bitmap으로 받아온다.
                firstimage = BitmapFactory.decodeStream(URL(firstimageurl).openStream())


                //list_c.add(ContentItem(contentid, firstimage, readcount, title))
            } */
        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }

        return null
    }
}