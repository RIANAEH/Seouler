package com.example.seouler.dataClass

import java.io.Serializable

data class Participation(var userId : Long, var roomId : Long, var uid : Long) : Serializable{
}