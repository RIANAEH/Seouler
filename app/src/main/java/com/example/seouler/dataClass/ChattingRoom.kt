package com.example.seouler.dataClass

import java.io.Serializable

data class ChattingRoom(
    var roomId: Long,
    var title: String,
    var description: String,
    var owner: Long,
    var locationX: Double,
    var locationY: Double,
    var locationName : String,
    var locationCertified: Boolean,
    var timestamp: Long,
    var uid: Long) : Serializable {

}