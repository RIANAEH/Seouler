package com.example.seouler.Chatting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.Exc_Recycle_MainActivity
import com.example.seouler.R
import com.example.seouler.Weather_MainActivity
import kotlinx.android.synthetic.main.activity_chatbot.*

class ChatBotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

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
        /*
        val intent = Intent(this, Exc_Recycle_MainActivity::class.java)
        Thread.sleep(1000) // 1초 대기
        startActivity(intent)
         */
    }

    private fun onSeoulerChatBotPlaceRecommendation() {
        // 아직 미구현
    }

    private fun onSeoulerChatBotWeather() {
        /*
        val intent = Intent(this, Weather_MainActivity::class.java)
        Thread.sleep(1000) // 1초 대기
        startActivity(intent)
         */
    }
}