package com.example.seouler.Search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.seouler.PlaceLikingActivity
import com.example.seouler.R
import com.example.seouler.dataClass.SearchPlace
import kotlinx.android.synthetic.main.activity_search_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern
import kotlin.reflect.typeOf

class SearchMainActivity : AppCompatActivity() {
    lateinit var mAdapter: NewPlaceRVAdapter
    var placelist: ArrayList<SearchPlace> = ArrayList<SearchPlace>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_main)

        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener { //검색버튼 눌렀을때
            override fun onQueryTextSubmit(query: String?): Boolean {
                //API 호출 및 어댑터 ......
                try {
                    // API에서 정보를 가져와 프래그먼트로 추가하기
                    val servUrl = "http://api.visitkorea.or.kr/openapi/service/rest/EngService/searchKeyword?"
                    val apiKey = "serviceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D"
                    val parms = "&numOfRows=50&MobileOS=AND&MobileApp=Seouler2020&listYN=Y&arrange=BareaCode=&sigunguCode=&cat1=&cat2=&cat3="
                    val keyword = "&keyword="+query
                    // JSON 받아오기 시도
                    val compUrl = servUrl + apiKey + parms + keyword +  "&_type=json"
                    val queue = Volley.newRequestQueue(this@SearchMainActivity)
                    val request =
                        StringRequest(
                            Request.Method.GET,
                            compUrl,
                            Response.Listener<String> { response ->
                                println("<ScompUrl> $compUrl")
                                println("<ScompUrl> $response")
                                // Response mlist에 추가 //
                                var response_json = JSONObject(response)
                                var jBody = response_json.getJSONObject("response").getJSONObject("body")
                                if(jBody.get("items") == ""){
                                    println("<S> Can't item..s....")
                                }
                                else {
                                    var jItems = jBody.getJSONObject("items")
                                    var jItem : Any
                                    var jItemArray = jItems.get("item")

                                    if(jItemArray.toString().startsWith('{')){ // 단일..
                                        jItem = jItems.getJSONObject("item") //OBJ
                                        println("<JSON> $jItem")
                                        var jimgurl: String
                                        var jaddr1: String

                                        if (jItem.isNull("firstimage")) {
                                            jimgurl = ""
                                        } else {
                                            jimgurl = jItem.getString("firstimage")
                                        }
                                        if (jItem.isNull("addr1")) {
                                            jaddr1 = ""
                                        } else {
                                            jaddr1 = jItem.getString("addr1")
                                        }
                                        placelist.add(
                                            SearchPlace(
                                                String(
                                                    jItem.getString("title").toByteArray((Charsets.ISO_8859_1)),
                                                    Charsets.UTF_8
                                                ),
                                                jaddr1,
                                                jItem.getInt("contentid").toString(),
                                                jimgurl,
                                                jItem.isNull("firstimage")
                                            )
                                        )

                                    }
                                    else {
                                        jItemArray = jItemArray as JSONArray
                                        for (i in 1..jItemArray.length() - 1) {
                                            var jItem = jItemArray[i] as JSONObject
                                            println("<JSON> $jItem")
                                            var jimgurl: String
                                            var jaddr1: String

                                            if (jItem.isNull("firstimage")) {
                                                jimgurl = ""
                                            } else {
                                                jimgurl = jItem.getString("firstimage")
                                            }
                                            if (jItem.isNull("addr1")) {
                                                jaddr1 = ""
                                            } else {
                                                jaddr1 = jItem.getString("addr1")
                                            }
                                            placelist.add(
                                                SearchPlace(
                                                    String(
                                                        jItem.getString("title").toByteArray((Charsets.ISO_8859_1)),
                                                        Charsets.UTF_8
                                                    ),
                                                    jaddr1,
                                                    jItem.getInt("contentid").toString(),
                                                    jimgurl,
                                                    jItem.isNull("firstimage")
                                                )
                                            )


                                        }
                                    }
                                    println("<BIND> placelist : $placelist")
                                    mAdapter.notifyDataSetChanged()
                                }
                                /////////////////////////
                            },
                            Response.ErrorListener {
                                println("<SEARCH> That didn't work!")
                            })
                    queue.add(request)
                }
                catch(e: Exception) {
                    Log.e("REST_API", "GET method failed: " + e.message);
                    e.printStackTrace();
                }
                sv_search.clearFocus()

                //searchMovie(query!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                placelist.clear()
                mAdapter.notifyDataSetChanged()
                return true
            }
        })


        mAdapter =NewPlaceRVAdapter(this, placelist)
        mAdapter.setOnItemClickListener(object : NewPlaceRVAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View?,
                position: Int
            ) {
                if (position != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
////////////////////상세정보로 넘어가기/////////////////////////////////////////////////////
                    val detail_intent = Intent(mAdapter.context, PlaceLikingActivity::class.java)
                    detail_intent.putExtra("contentId", placelist[position].contentid)
                    ContextCompat.startActivity(mAdapter.context, detail_intent, null)
                }
            }

        })
        rv_searchresult.setAdapter(mAdapter)
        rv_searchresult.setLayoutManager(LinearLayoutManager(this))

    }
}
