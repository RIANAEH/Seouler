package com.example.seouler.dataClass

import java.io.Serializable

data class Message(var messageId : Long, var roomId : Long, var sender : Long, var text : String, var timestamp : Long, var uid : Long) : Serializable{
}