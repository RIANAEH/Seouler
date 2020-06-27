package com.example.seouler.Chatting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
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
            val dn: String = result.intent.displayName

            // 인텐트 종류에 따라 다양한 앱 기능 실행
        }
    }
}