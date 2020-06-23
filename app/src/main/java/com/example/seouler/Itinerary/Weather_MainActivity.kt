package com.example.seouler


import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class Weather_MainActivity : FragmentActivity() {
    var url: String = "http://apis.data.go.kr/1360000/VilageFcstMsgService/getLandFcst"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_main)


        val queue = Volley.newRequestQueue(this)
        val url =
            "http://apis.data.go.kr/1360000/VilageFcstMsgService/getLandFcst?serviceKey=I23jHEEvrpNKlQiFjB8me4jV48AF6y2Sj0mGzBX0s4jce9OyTCgYqNi31f6LgQgncOTlVO6Wzal8vv96JHegig%3D%3D&numOfRows=10&pageNo=1&regId=11A00101"
        // WeatherAPITask("http://apis.data.go.kr/1360000/VilageFcstMsgService/getLandFcst").execute();
        val stringRequest =
            StringRequest(
                Request.Method.GET,
                url,
                Response.Listener<String> { response ->

                    // textView.text = strRes
                    //textView2.text = strRes

                },
                Response.ErrorListener {
                    Toast.makeText(
                        this,
                        "That didn't work!",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        queue.add(stringRequest)
    }


}