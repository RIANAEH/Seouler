package com.example.seouler.Recommend

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class GetContentTask(val list_c: ArrayList<ContentItem>, val contentTypedId: String, val cat1: String, val cat2: String, val cat3: String?) : AsyncTask<Any?, Any?, Any?>() {
    override fun doInBackground(vararg params: Any?): Any? {
        // 현재 기본적으로 20개의 content를 가져오며 numOfRows를 변경하여 개수를 수정할 수 있다.
        var urlString: String
        when(cat3) {
            null -> urlString =
                "http://api.visitkorea.or.kr/openapi/service/rest/EngService/areaBasedList?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=Seouler&listYN=Y&arrange=P&contentTypedId="+contentTypedId+"&areaCode=1&cat1="+cat1+"&cat2="+cat2+"&_type=json"
            else -> urlString =
                "http://api.visitkorea.or.kr/openapi/service/rest/EngService/areaBasedList?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=20&pageNo=1&MobileOS=AND&MobileApp=Seouler&listYN=Y&arrange=P&contentTypedId="+contentTypedId+"&areaCode=1&cat1="+cat1+"&cat2="+cat2+"&cat3="+cat3+"&_type=json"

        }

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

        // json parsing
        try {
            val jobj = JSONObject(buffer)
            val robj = jobj.getJSONObject("response")
            val bobj = robj.getJSONObject("body")
            val iobj = bobj.getJSONObject("items")
            val jarray = iobj.getJSONArray("item")

            for(i in 0 until jarray.length()) {
                val obj = jarray.getJSONObject(i)
                val contentid = obj.getString("contentid")
                val firstimageurl = obj.getString("firstimage")
                val readcount = obj.getString("readcount").toInt()
                val title = obj.getString("title")

                // 이미지 url로부터 이미지를 bitmap으로 받아온다.
                val firstimage = BitmapFactory.decodeStream(URL(firstimageurl).openStream())


                list_c.add(ContentItem(contentid, firstimage, readcount, title))
            }
        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }

        return null
    }
}