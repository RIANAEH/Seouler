package com.example.seouler.Recommend

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class GetContentTask(val list_c: ArrayList<ContentItem>, val list_k: ArrayList<KeywordItem>, val contentTypedId: String, val cat1: String, val cat2: String, val cat3: String?, val view: Recommend_MainActivity) : AsyncTask<Any?, Any?, Any?>() {
    var firstimage: Bitmap? = null

    override fun doInBackground(vararg params: Any?): Any? {
        // 현재 기본적으로 10개의 content를 가져오며 numOfRows를 변경하여 개수를 수정할 수 있다.
        var urlString: String
        when(cat3) {
            null -> urlString =
                "http://api.visitkorea.or.kr/openapi/service/rest/EngService/areaBasedList?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=Seouler&listYN=Y&arrange=P&contentTypedId="+contentTypedId+"&areaCode=1&cat1="+cat1+"&cat2="+cat2+"&_type=json"
            else -> urlString =
                "http://api.visitkorea.or.kr/openapi/service/rest/EngService/areaBasedList?ServiceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=Seouler&listYN=Y&arrange=P&contentTypedId="+contentTypedId+"&areaCode=1&cat1="+cat1+"&cat2="+cat2+"&cat3="+cat3+"&_type=json"

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
                //val firstimageurl = obj.getString("firstimage")
                firstimage = BitmapFactory.decodeStream(URL(obj.getString("firstimage")).openStream())
                val readcount = obj.getString("readcount").toInt()
                val title = obj.getString("title")

                list_c.add(ContentItem(contentid, firstimage, readcount, title))
            }
        } catch (e: Exception) {
            Log.d("태그", "json error: $e")
        }

        return null
    }

    fun selector(p: ContentItem): Int? = p.readcount // 조회수로 정렬하기 위함

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        // 조회수가 큰 순서로 정렬한다.
        list_c.sortByDescending({selector(it)})

        when(contentTypedId) {
            "76" -> {
                list_keyword_attraction = list_k
                list_content_attraction = list_c

                //새롭게 디스플레이한다.
                view.findViewById<RecyclerView>(R.id.recyclerView_content_attraction).adapter = ContentAdapter(list_c)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_attraction).adapter = KeywordAdapter(list_k)
            }
            "75" -> {
                list_keyword_cultural = list_k
                list_content_cultural = list_c

                //새롭게 디스플레이한다.
                view.findViewById<RecyclerView>(R.id.recyclerView_content_cultural).adapter = ContentAdapter(list_c)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_cultural).adapter = KeywordAdapter(list_k)
            }
            "80" -> {
                list_keyword_accommodation = list_k
                list_content_accommodation = list_c

                //새롭게 디스플레이한다.
                view.findViewById<RecyclerView>(R.id.recyclerView_content_accommodation).adapter = ContentAdapter(list_c)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_accommodation).adapter = KeywordAdapter(list_k)
            }
            "79" -> {
                list_keyword_shopping = list_k
                list_content_shopping = list_c

                //새롭게 디스플레이한다.
                view.findViewById<RecyclerView>(R.id.recyclerView_content_shopping).adapter = ContentAdapter(list_c)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_shopping).adapter = KeywordAdapter(list_k)
            }
            "82" -> {
                list_keyword_cuisine = list_k
                list_content_cuisine  = list_c

                //새롭게 디스플레이한다.
                view.findViewById<RecyclerView>(R.id.recyclerView_content_cuisine ).adapter = ContentAdapter(list_c)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_cuisine ).adapter = KeywordAdapter(list_k)
            }
            //else ->
        }

    }
}