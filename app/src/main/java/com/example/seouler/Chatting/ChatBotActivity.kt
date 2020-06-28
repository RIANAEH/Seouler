package com.example.seouler.Chatting

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.seouler.*
import com.example.seouler.Like.LikeMainActivity
import kotlinx.android.synthetic.main.activity_chatbot.*
import com.example.seouler.dataClass.WeatherDaily
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.*

class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        // 메시지를 보낸 후 응답을 받아, 응답의 인텐트에 따른 메소드를 실행한다.
        sendChatbotMessageButton.setOnClickListener {
            // 메시지를 보내고
            ChatBotAdapter.initAssistant(this, chatbotInputMessageTextView.text.toString())

            // 인텐트를 받아오기
            val result = ChatBotAdapter.detectIntentText(123, chatbotInputMessageTextView.text.toString())
            chatbotResponse.text = result.fulfillmentText

            // 인텐트 종류에 따라 다양한 앱 기능 실행
            when(result.intent.displayName) {
                "SeoulerChatBotCurrentPlace" -> onSeoulerChatBotCurrentPlace()
                "SeoulerChatBotDestinationPlace" -> onSeoulerChatBotDestinationPlace()
                "SeoulerChatBotExchange" -> onSeoulerChatBotExchange()
                "SeoulerChatBotPlaceRecommendation" -> onSeoulerChatBotPlaceRecommendation()
                "SeoulerChatBotWeather" -> onSeoulerChatBotWeather()
                "SeoulerChatBotLike" -> onSeoulerChatBotLike()
            }
        }

        // 테스트용 버튼
        ChatBotTestButton.setOnClickListener {
            onSeoulerChatBotLike()
        }
    }

    private fun onSeoulerChatBotCurrentPlace() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotDestinationPlace() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotExchange() {
        // 구현 완료
        val go_to_exc_intent = Intent(this, Exc_Recycle_MainActivity::class.java)
        var rate_async = Rate_Async(this)
        rate_async.execute()

        Thread.sleep(2000) // 2초 대기

        startActivity(go_to_exc_intent)
    }

    private fun onSeoulerChatBotPlaceRecommendation() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotWeather() {
        // 구현 완료
        val WeatherDetailPreIntent = Intent(this, Weather_MainActivity::class.java)
        val weather_async = Weather_Async(this, WeatherDetailPreIntent, 37.5642135, 127.0016985) // API
        weather_async.execute()
    }

    private fun onSeoulerChatBotLike() {
        val intent = Intent(this, LikeMainActivity::class.java)
        startActivity(intent)
    }


    // 챗봇 전용 내부 클래스 =============================================================================================================

    class Weather_Async(mainActivity: Activity, preIntent: Intent, lat : Double, lon : Double) : AsyncTask<Int?, Int, List<String>>() {
        var con = mainActivity
        var preIntent = preIntent
        var lat = lat
        var lon = lon

        override fun doInBackground(vararg params: Int?): List<String>? {
            VolleyService_weather.setlatlon(lat, lon)

            VolleyService_weather.testVolley(con) { testSuccess ->
                if (testSuccess) {
                    Log.d("<Weather>","통신 성공!")
                    Log.d("<Weather>", "${VolleyService_weather.response_json.toString()}")

                    var jWeatherCurrent = VolleyService_weather.response_json.get("current") as JSONObject
                    var jWeatherDailyArray = VolleyService_weather.response_json.get("daily") as JSONArray
                    var wDailyList = listOf(WeatherDaily(), WeatherDaily(), WeatherDaily(), WeatherDaily(), WeatherDaily())

                    for (i in 0..4){
                        var jTmp = jWeatherDailyArray.getJSONObject(i+1) // 내일 날씨 정보
                        var jtTmp = jTmp.get("temp") as JSONObject
                        var jwTmp = jTmp.get("weather") as JSONArray

                        wDailyList[i].tMin = jtTmp.getDouble("min")
                        wDailyList[i].tMax = jtTmp.getDouble("max")
                        wDailyList[i].stricon = jwTmp.getJSONObject(0).getString("icon")
                        preIntent.putExtra(i.toString()+"day", LocalDate.now().plusDays((i+1).toLong()).dayOfWeek.toString())
                        preIntent.putExtra(i.toString()+"order", i)
                        preIntent.putExtra(i.toString()+"min", wDailyList[i].tMin)
                        preIntent.putExtra(i.toString()+"max", wDailyList[i].tMax)
                        preIntent.putExtra(i.toString()+"icon", wDailyList[i].stricon)
                    }

                    var strWeatherNowTemp = jWeatherCurrent.get("temp").toString() + "℃"
                    var jWeatherNowWeatherArray = jWeatherCurrent.get("weather") as JSONArray
                    var jWeatherNowWeather = jWeatherNowWeatherArray.getJSONObject(0)
                    var strWeatherNowIconNum = jWeatherNowWeather.get("icon") as String

                    preIntent.putExtra("nowDesc", jWeatherNowWeather.getString("description"))
                    preIntent.putExtra("nowTemp", strWeatherNowTemp)
                    preIntent.putExtra("nowIcon", strWeatherNowIconNum)
                    preIntent.putExtra("nowSunrise", Date(jWeatherCurrent.getInt("sunrise").toLong()).time)
                    preIntent.putExtra("nowSunset", Date(jWeatherCurrent.getInt("sunset").toLong()).time )
                    preIntent.putExtra("nowHumidity", jWeatherCurrent.get("humidity") as Int)

                    Log.d("preIntentExtras", preIntent.extras.toString())
                    startActivity(con, preIntent, preIntent.extras)
                } else {
                    println("통신 실패...!")
                }

            }
            return null
        }
    }

    class Rate_Async(mainActivity: Activity) :
        AsyncTask<Int?, Int, List<String>>() {
        var act = mainActivity

        override fun doInBackground(vararg params: Int?): List<String>? {
            VolleyService_rate.testVolley(act) { testSuccess ->
                if (testSuccess) {
                    var response_json = VolleyService_rate.response_json
                    Log.d("<Rate>","환율쓰2....JSON: $response_json")
                } else {
                    Log.d("<Rate>","환율 실패...")
                }
            }

            return null
        }
    }
}