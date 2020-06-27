package com.example.seouler.Chatting

import android.content.Context
import android.util.Log
import com.example.seouler.R
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.protobuf.Struct
import com.google.protobuf.Value

object ChatBotAdapter {
    private var client: SessionsClient? = null
    private var session: SessionName? = null
    private const val LANGUAGE_CODE = "en-US"

    fun initAssistant(context: Context, sessionText: String) {
        try {
            val stream = context.resources.openRawResource(R.raw.seouler2020c6a67b80037a)
            val credentials = GoogleCredentials.fromStream(stream)
            val projectId = (credentials as ServiceAccountCredentials).projectId

            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build()

            Log.d("Dialogflow", settingsBuilder.toString())
            Log.d("Dialogflow", sessionsSettings.toString())

            this.client = SessionsClient.create(sessionsSettings)
            this.session = SessionName.of(projectId, sessionText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    fun detectIntentText(somePayload: Int, text: String): QueryResult {
        if(client == null || session == null){
            throw Exception("Error: no dialogflow client")
        }

        // Set the text (input) and language code (en) for the query
        val textInput = TextInput.newBuilder().setText(text).setLanguageCode(LANGUAGE_CODE)

        // Build the query with the TextInput
        val queryInput = QueryInput.newBuilder().setText(textInput).build()

        //Save received payload into a protobuf Struct
        val somePayload = Value.newBuilder().setNumberValue(somePayload.toDouble()).build()
        val struct = Struct.newBuilder()
        struct.putFields("somePayload", somePayload)

        //Set queryParameters
        val queryParameters = QueryParameters.newBuilder().setPayload(struct).build()

        //Build the request
        val request = DetectIntentRequest.newBuilder()
            .setSession(session.toString())
            .setQueryInput(queryInput)
            .setQueryParams(queryParameters)
            .build()

        // Performs the detect intent request
        val response = client!!.detectIntent(request)

        // Display the query result
        return response.queryResult
    }
}