package com.example.seouler

import android.location.Location
import java.util.*

data class ChattingRoom(var roomId : Long,
                        var title : String,
                        var description : String,
                        var owner : Long,
                        var locationX : Double,
                        var locationY : Double,
                        var locationCertified : Boolean,
                        var timestamp : Long) {}