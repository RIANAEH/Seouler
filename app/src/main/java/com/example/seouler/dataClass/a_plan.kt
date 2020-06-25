package com.example.seouler.dataClass

import com.google.firebase.firestore.GeoPoint
import java.time.LocalTime


class a_plan (var time: LocalTime, val destination:String, val documentId : String, val geo : GeoPoint)
