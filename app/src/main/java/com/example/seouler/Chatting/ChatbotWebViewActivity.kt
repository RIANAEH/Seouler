package com.example.seouler.Chatting

import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.seouler.R
import kotlinx.android.synthetic.main.activity_chatbot_webview.*


class ChatbotWebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_webview)

        val url = intent.extras!!["url"].toString()
        val finalUrl = when(url) {
            null -> "https://naver.com"
            else -> url
        }

        Log.d("Final URL", finalUrl)

        chatbotSearchResultView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용을 허용한다.
        chatbotSearchResultView.webViewClient = WebViewClient() // 새로운 창을 띄우지 않고 내부에서 웹뷰를 실행시킨다.
        chatbotSearchResultView.loadUrl(finalUrl)
    }
}