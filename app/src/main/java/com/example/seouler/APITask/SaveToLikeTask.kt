package com.example.seouler.APITask

import android.os.AsyncTask
import android.util.Log
import com.example.seouler.Like.map_like
import com.example.seouler.Recommend.ContentItem
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SaveToLikeTask(val contentId: String) : AsyncTask<Any?, Any?, Any?>() {

    var buffer :String? = null
    var firstimage : String? = null
    var title :String? = null

    override fun doInBackground(vararg p0: Any?): Any? {

        var urlString =
            "http://api.visitkorea.or.kr/openapi/service/rest/EngService/detailCommon?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=Seouler&contentId="+contentId+"&defaultYN=Y&firstImageYN=Y&_type=json"
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
            Log.d("태그", "json error1: $e")
        }

        // json parsing
        try {
            val jobj = JSONObject(buffer)
            val robj = jobj.getJSONObject("response")
            val bobj = robj.getJSONObject("body")
            val iobj = bobj.getJSONObject("items")
            val obj = iobj.getJSONObject("item")

            if(obj.has("firstimage")) firstimage = obj.getString("firstimage")
            if(obj.has("title")) title = obj.getString("title")

        } catch (e: Exception) {
            Log.d("태그", "json error2: $e")
        }

        return null
    }


    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        map_like.put(contentId, ContentItem(contentId, firstimage, null, title))
    }
}