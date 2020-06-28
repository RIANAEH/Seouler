package com.example.seouler.Recommend

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.recyclerview.widget.RecyclerView
import com.example.seouler.R

class DisplayTask(val contentTypedId: String?, val view: Recommend_MainActivity)  : AsyncTask<Any?, Any?, Any?>() {

    override fun doInBackground(vararg p0: Any?): Any? {

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)

        when(contentTypedId) {
            "76" -> {
                view.findViewById<RecyclerView>(R.id.recyclerView_content_attraction).adapter = ContentAdapter(list_content_attraction)
                view.findViewById<RecyclerView>(R.id.recyclerView_keyword_attraction).adapter = KeywordAdapter(list_keyword_attraction)
            }
            "75" -> {
                view?.findViewById<RecyclerView>(R.id.recyclerView_content_cultural)?.adapter = ContentAdapter(list_content_cultural)
                view?.findViewById<RecyclerView>(R.id.recyclerView_keyword_cultural)?.adapter = KeywordAdapter(list_keyword_cultural)
            }
            "80" -> {
                view?.findViewById<RecyclerView>(R.id.recyclerView_content_accommodation)?.adapter = ContentAdapter(list_content_accommodation)
                view?.findViewById<RecyclerView>(R.id.recyclerView_keyword_accommodation)?.adapter = KeywordAdapter(list_keyword_accommodation)
            }
            "79" -> {
                view?.findViewById<RecyclerView>(R.id.recyclerView_content_shopping)?.adapter = ContentAdapter(list_content_shopping)
                view?.findViewById<RecyclerView>(R.id.recyclerView_keyword_shopping)?.adapter = KeywordAdapter(list_keyword_shopping)
            }
            "82" -> {
                view?.findViewById<RecyclerView>(R.id.recyclerView_content_cuisine )?.adapter = ContentAdapter(list_content_cuisine)
                view?.findViewById<RecyclerView>(R.id.recyclerView_keyword_cuisine )?.adapter = KeywordAdapter(list_keyword_cuisine)
            }
            //else ->
        }
    }
}