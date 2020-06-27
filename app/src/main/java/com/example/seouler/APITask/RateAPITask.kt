package com.example.seouler

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.seouler.dataClass.a_exchange
import kotlinx.android.synthetic.main.activity_recycle_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


object VolleyService_rate{
    val current = LocalTime.now()
    val formatter = DateTimeFormatter.ISO_LOCAL_TIME
    val formatted = current.format(formatter)
    var baseday = LocalDateTime.now()
    var temp = LocalTime.of(11,0) // 기준점 . 주말도 포함해야함..

    //var k = baseday.toLocalTime().isBefore(temp) // 11시 이전 확인용
    var l = setBaseday()



    fun setBaseday() : Int{
        if (baseday.toLocalDate().dayOfWeek.toString() == "SATURDAY") {
            //baseday = baseday.minusDays(1)
            return -1

        } else if (baseday.toLocalDate().dayOfWeek.toString() == "SUNDAY") {
            baseday = baseday.minusDays(2)
            return -2
        }
        else if(baseday.toLocalTime().isBefore(LocalTime.of(11,0))) {
            if (baseday.toLocalDate().dayOfWeek.toString() == "MONDAY")
                return -3
            else
                return -1
        }
        else
            return -0

        }

    lateinit var response_json : JSONArray

    var testUrl = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=LHuF0WXP9Ie45wK4xO4j3iJZ9mMRsd4X&data=AP01"
    fun check11 (){
        //println("<환율> Check11 k :: $k")
        println("<환율> $current ")
        if (!testUrl.contains("&searchdate")){
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, l) //변경하고 싶은 원하는 날짜 수를 넣어 준다.
            //calendar
            var TimeToDate = calendar.time
            var formatter = SimpleDateFormat("yyyyMMdd") //날짜의 모양을 원하는 대로 변경 해 준다.
            //formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            var finalResultDate = formatter.format(TimeToDate)

            println(finalResultDate)
            testUrl+= "&searchdate=" + finalResultDate
        }
    }
    fun testVolley(act: Activity, success: (Boolean) -> Unit) {

        val myJson = JSONObject()
        /* myJson에 아무 데이터도 put 하지 않았기 때문에 requestBody는 "{}" 이다 */
        val requestBody = myJson.toString()
        check11()
        println("<환율> url : $testUrl")

        Recycle_MainActivity.HttpsTrustManager.allowAllSSL()

        val testRequest = object : StringRequest(Method.GET, testUrl , Response.Listener { response ->
            println("<환율> 서버 Response 수신: $response")
            /*내가만든..... 것....*/
            response_json = JSONArray(response)


            println("환율쓰....JSON: $response_json")
            for (i in 0 until response_json.length()){
                var response_json_obj : JSONObject = response_json.get(i) as JSONObject
                exclist.add(jsonToExc(response_json_obj.get("cur_unit") as String, response_json_obj.get("kftc_deal_bas_r") as String))
            }




            success(true)

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "<환율> 서버 Response 가져오기 실패: $error")
            var tmptmp = error.networkResponse
            Log.d("ERROR", "$tmptmp")
            success(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
            /* getBodyContextType에서는 요청에 포함할 데이터 형식을 지정한다.
             * getBody에서는 요청에 JSON이나 String이 아닌 ByteArray가 필요하므로, 타입을 변경한다. */
        }

        Volley.newRequestQueue(act).add(testRequest)
    }


    fun jsonToExc(cur_unit: String, kftc: String) : a_exchange {
        val exc = a_exchange(cur_unit, kftc)
        return exc
    }


}

