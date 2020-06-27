package com.example.seouler.Search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.seouler.R
import com.example.seouler.dataClass.Location
import com.example.seouler.dataClass.Place
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import org.json.JSONObject


class PlaceDetailActivity : AppCompatActivity() {
    lateinit var myLocation : Location
    lateinit var mAdapter: PlaceRVAdapter
    var placelist: ArrayList<Place> = ArrayList<Place>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        // typeId
        // 불러온 상위 인텐트에서 스트링을 받아와 텍스트 설정하기
        val detailName = findViewById<TextView>(R.id.placeDetailName)
        detailName.text = intent.extras!!["detailName"].toString()

        //val detailItemList = findViewById<LinearLayout>(R.id.detailItemCarrier)


        myLocation = intent.extras!!["myLocation"] as Location
        try {
            // API에서 정보를 가져와 프래그먼트로 추가하기
            val servUrl = "http://api.visitkorea.or.kr/openapi/service/rest/EngService/locationBasedList?"
            val apiKey = "serviceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D"
            val parms = "&numOfRows=50&MobileOS=AND&MobileApp=Seouler2020&listYN=Y&arrange=B&radius=1000"//50개, 목록, 인기순정렬, 1000m 반경 내
            val locparm = "&mapX="+myLocation.locationX+"&mapY="+myLocation.locationY
            val typeId = "&contentTypeId="+ intent.getStringExtra("typeId")

            // JSON 받아오기 시도
            val compUrl = servUrl + apiKey + parms + locparm+ typeId+  "&_type=json"
            val queue = Volley.newRequestQueue(this)
            val request =
                StringRequest(
                    Request.Method.GET,
                    compUrl,
                    Response.Listener<String> { response ->
                        println("<compUrl> $compUrl")
                        println("<compUrl> $response")
                        // Response mlist에 추가 //
                        var response_json = JSONObject(response)
                        var jItemArray = JSONArray(response_json.getJSONObject("response").getJSONObject("body").getJSONObject("items").getString("item"))
                        for (i in 1..jItemArray.length()-1){
                            var jItem = jItemArray[i] as JSONObject
                            println("<JSON> $jItem")
                            var jimgurl: String
                            if(jItem.isNull("firstimage")){
                                 jimgurl= ""
                            }
                            else{
                                jimgurl = jItem.getString("firstimage")
                            }
                            placelist.add(Place(String(jItem.getString("title").toByteArray((Charsets.ISO_8859_1)), Charsets.UTF_8),
                                jItem.getInt("dist").toString()+"m", jItem.getInt("contentid").toString(), jimgurl ,jItem.isNull("firstimage")) )



                        }
                        println("<BIND> placelist : $placelist")
                        mAdapter.notifyDataSetChanged()
                        /////////////////////////
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "That didn't work!", Toast.LENGTH_SHORT).show()
                    })
            queue.add(request)
        }
        catch(e: Exception) {
            Log.e("REST_API", "GET method failed: " + e.message);
            e.printStackTrace();
        }

        mAdapter = PlaceRVAdapter(this, placelist)
        mAdapter.setOnItemClickListener(object : PlaceRVAdapter.OnItemClickListener {
            override fun onItemClick(
                v: View?,
                position: Int
            ) {
                if (position != RecyclerView.NO_POSITION) { // 리스너 객체의 메서드 호출.
////////////////////상세정보로 넘어가기/////////////////////////////////////////////////////

                }
            }

        })

        rv_placelist.setAdapter(mAdapter)
        rv_placelist.setLayoutManager(LinearLayoutManager(this))


    }
}

