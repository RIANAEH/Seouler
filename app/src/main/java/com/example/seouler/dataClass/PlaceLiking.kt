package com.example.seouler.dataClass

data class PlaceLiking(var placeId: Long,
                       var placeName: String, var placeDescription: String, var placeImgUrl: String,
                       var locationX: Double, var locationY: Double, var placeRate: Float, var placeLike: Boolean) {
}