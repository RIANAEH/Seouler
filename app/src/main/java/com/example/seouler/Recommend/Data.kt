package com.example.seouler.Recommend

import android.graphics.Bitmap

class ContentItem(val contentid: String?, val firstimage: Bitmap?, val readcount: Int?, val title: String?) {

}

class Cat(val cat1: String, val cat2: String, val cat3: String?) {

}

class KeywordItem(val keyword: String) {

}

// Tourist Attractions 76
var list_content_attraction = ArrayList<ContentItem>()
var list_keyword_attraction = arrayListOf(KeywordItem("Natural Sites"))
val map_attractionCat: Map<String, Cat> =
    mapOf("Natural Sites" to Cat("A01", "A0101", null),
        "Natural Resources" to Cat("A01", "A0102", null),
        "Historical Sites" to Cat("A02", "A0201", null),
        "Recreational Sites" to Cat("A02", "A0202", null),
        "Experience Programs" to Cat("A02", "A0203", null),
        "Industrial Sites" to Cat("A02", "A0204", null),
        "Architectural Sights" to Cat("A02", "A0205", null))
val category_attraction = map_attractionCat.keys.toTypedArray()
var checkItem_attraction = booleanArrayOf(true, false, false, false, false, false, false)

// Leisure/Sports 75 -> Cultural Facilities 78
var list_content_cultural = ArrayList<ContentItem>()
var list_keyword_cultural = arrayListOf(KeywordItem("Museums"))
val map_culturalCat: Map<String, Cat> =
    mapOf("Museums" to Cat("A02", "A0206", "A02060100"),
        "Memorial Halls" to Cat("A02", "A0206", "A02060200"),
        "Exhibition Halls" to Cat("A02", "A0206", "A02060300"),
        "Convention Centers" to Cat("A02", "A0206", "A02060400"),
        "Art Museums/Galleries" to Cat("A02", "A0206", "A02060500"),
        "Performance Halls" to Cat("A02", "A0206", "A02060600"),
        "Korean Cultural Centers" to Cat("A02", "A0206", "A02060700"),
        "Cultural Training Centers" to Cat("A02", "A0206", "A02061100"))
val category_cultural = map_culturalCat.keys.toTypedArray()
var checkItem_cultural = booleanArrayOf(true, false, false, false, false, false, false, false)

// Accommodation 80
var list_content_accommodation = ArrayList<ContentItem>()
var list_keyword_accommodation = arrayListOf(KeywordItem("Hotels"))
val map_accommodationCat: Map<String, Cat> =
    mapOf("Hotels" to Cat("B02", "B0201", "B02010100"),
        "Motels" to Cat("B02", "B0201", "B0209000"),
        "Hanok Stays" to Cat("B02", "B0201", "B02011600")
    )
val category_accommodation = map_accommodationCat.keys.toTypedArray()
var checkItem_accommodation = booleanArrayOf(true, false, false)

// Shopping 79
var list_content_shopping = ArrayList<ContentItem>()
var list_keyword_shopping = arrayListOf(KeywordItem("Traditional Markets"))
val map_shoppingCat: Map<String, Cat> =
    mapOf("Traditional Markets" to Cat("A04", "A0401", "A04010200"),
        "Department Stores" to Cat("A04", "A0401", "A04010300"),
        "Duty Free Shops" to Cat("A04", "A0401", "A04010400"),
        "Discount Shops" to Cat("A04", "A0401", "A04010500"),
        "Shops/Boutiques/Outlet Malls" to Cat("A04", "A0401", "A04010600"),
        "Souvenir Shops" to Cat("A04", "A0401", "A04010800"))
val category_shopping = map_shoppingCat.keys.toTypedArray()
var checkItem_shopping = booleanArrayOf(true, false, false, false, false, false)

// Shopping 82
var list_content_cuisine = ArrayList<ContentItem>()
var list_keyword_cuisine = arrayListOf(KeywordItem("Korean Restaurants"))
val map_cuisineCat: Map<String, Cat> =
    mapOf("Korean Restaurants" to Cat("A05", "A0502", "A05020100"),
        "Western Restaurants" to Cat("A05", "A0502", "A05020200"),
        "Japanese Restaurants" to Cat("A05", "A0502", "A05020300"),
        "Chinese Restaurants" to Cat("A05", "A0502", "A05020400"),
        "Asian Restaurants" to Cat("A05", "A0502", "A05020500"),
        "Unique Restaurants" to Cat("A05", "A0502", "A05020700"),
        "Vegetarian Restaurants" to Cat("A05", "A0502", "A05020800"),
        "Bars/Cafes" to Cat("A05", "A0502", "A05020900"))
val category_cuisine = map_cuisineCat.keys.toTypedArray()
var checkItem_cuisine = booleanArrayOf(true, false, false, false, false, false, false, false)