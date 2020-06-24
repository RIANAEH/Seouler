package com.example.seouler.dataClass

data class PlaceLiking(var placeId: Long,
                       var placeName: String, var placeDescription: String, var placeImgUrl: String,
                       var placeCoords: Pair<Float, Float>, var placeRate: Float, var placeLike: Boolean) {
}