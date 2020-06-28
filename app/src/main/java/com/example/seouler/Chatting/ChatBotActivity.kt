package com.example.seouler.Chatting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.Exc_Recycle_MainActivity
import com.example.seouler.R
import com.example.seouler.Recycle_MainActivity
import com.example.seouler.VolleyService_weather.lat
import com.example.seouler.VolleyService_weather.lon
import com.example.seouler.Weather_MainActivity
import kotlinx.android.synthetic.main.activity_chatbot.*
import com.example.seouler.dataClass.WeatherDaily
import com.example.seouler.dataClass.a_exchange

class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        //API 활성화// 날씨, 환율



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
            }
        }
    }

    private fun onSeoulerChatBotCurrentPlace() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotDestinationPlace() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotExchange() {
        val go_to_exc_intent = Intent(this, Exc_Recycle_MainActivity::class.java)
        var rate_async = Recycle_MainActivity.Rate_Async(this, 0)
        rate_async.execute()

        Thread.sleep(2000) // 2초 대기

        startActivity(go_to_exc_intent)
    }

    private fun onSeoulerChatBotPlaceRecommendation() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotWeather() {
        var WeatherDetailPreIntent = Intent(this, Weather_MainActivity::class.java)
        var weather_async = Recycle_MainActivity.Weather_Async(this, WeatherDetailPreIntent, 37.5642135, 127.0016985) // API
        weather_async.execute()

        Thread.sleep(2000) // 2초 대기

        startActivity(WeatherDetailPreIntent)
    }
}