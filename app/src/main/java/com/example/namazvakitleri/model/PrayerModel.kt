package com.example.namazvakitleri.model

data class Place(
    val country: String,
    val countryCode: String,
    val city: String,
    val region: String,
    val latitude: Double,
    val longitude: Double
)

//data class Times(
//    val times: List<String>
//)

data class PrayerModel(
    val place: Place,
    val times: Map<String, List<String>>
)

