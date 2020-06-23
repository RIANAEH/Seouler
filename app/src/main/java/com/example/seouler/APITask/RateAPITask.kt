package com.example.seouler

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

    var temp = LocalTime.of(11,0)

    var k = LocalTime.now().isBefore(temp)

    lateinit var response_json : JSONArray
    var testUrl = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=LHuF0WXP9Ie45wK4xO4j3iJZ9mMRsd4X&data=AP01"
    fun check11 (){

        println("<환율> Check11 k :: $k")
        println("<환율> $current ")
        if (k && !testUrl.contains("&searchdate")) {
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1) //변경하고 싶은 원하는 날짜 수를 넣어 준다.
            var TimeToDate = calendar.time
            var formatter = SimpleDateFormat("yyyyMMdd") //날짜의 모양을 원하는 대로 변경 해 준다.
            //formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
            var finalResultDate = formatter.format(TimeToDate)

            println(finalResultDate)
            testUrl+= "&searchdate=" + finalResultDate

        }
    }
    fun testVolley(context: Context, success: (Boolean) -> Unit) {

        val myJson = JSONObject()
        val requestBody = myJson.toString()
        /* myJson에 아무 데이터도 put 하지 않았기 때문에 requestBody는 "{}" 이다 */
        check11()

        val testRequest = object : StringRequest(Method.GET, testUrl , Response.Listener { response ->
            println("<환율> 서버 Response 수신: $response")
            println("<환율> url : $testUrl")
            /*내가만든..... 것....*/
            response_json = JSONArray(response)


            success(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "서버 Response 가져오기 실패: $error")
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

        Volley.newRequestQueue(context).add(testRequest)
    }

}