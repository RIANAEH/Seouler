package com.example.seouler.dataClass

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.*

data class ChattingRoom(var roomId : Long,
                        var title : String,
                        var description : String,
                        var owner : Long,
                        var locationX : Double,
                        var locationY : Double,
                        var locationCertified : Boolean,
                        var timestamp : Long,
                        var uid : Long) : Serializable {

}