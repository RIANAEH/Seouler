package com.example.seouler

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
<<<<<<< Updated upstream
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seouler.dataClass.ChattingRoom
import com.example.seouler.fragments.DetailItemFragment
import kotlinx.android.synthetic.main.activity_chatting_home.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.zip.Inflater
=======
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONArray
>>>>>>> Stashed changes

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 불러온 상위 인텐트에서 스트링을 받아와 텍스트 설정하기
        val detailName = findViewById<TextView>(R.id.placeDetailName)
        detailName.text = intent.extras!!["detailName"].toString()

        val detailItemList = findViewById<LinearLayout>(R.id.detailItemCarrier)

        /*
        // 테스트용으로 프래그먼트 불러오기
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        layoutInflater.inflate(R.layout.fragment_detail_item, detailItemList, true)
        */

        try {
            // API에서 정보를 가져와 프래그먼트로 추가하기
            val servUrl = "http://api.visitkorea.or.kr/openapi/service/rest/EngService/searchStay?"
            val apiKey = "serviceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D"
            val parms = "&numOfRows=10&pageNo=1&MobileOS=AND&MobileApp=Seouler2020&listYN=Y&arrange=A&areaCode=1&sigunguCode=1&hanOk=&benikia=&goodStay=&modifiedtime="

            // JSON 받아오기 시도
            val compUrl = servUrl + apiKey + parms + "&_type=json"
            val queue = Volley.newRequestQueue(this)
            val request =
                StringRequest(
                    Request.Method.GET,
                    compUrl,
                    Response.Listener<String> { response ->
                        Log.d("REST_API_RESULT", response)
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

    }
}