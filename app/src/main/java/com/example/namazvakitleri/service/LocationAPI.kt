package com.example.namazvakitleri.service

import com.example.namazvakitleri.model.PrayerModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LocationAPI {

    @GET("api/regions")
    fun getCounties(
        @Query("country") country: String = "Turkey"
    ): Call<List<String>>

    @GET("api/cities")
    fun getCities(
        @Query("country") country: String = "Turkey",
        @Query("region") region: String
    ): Call<List<String>>

    @GET("api/timesFromPlace")
    fun getTimeFromPlace(
        @Query("country") country: String = "Turkey",
        @Query("timezoneOffset") timezoneOffset: Int = 180,
        @Query("city") city: String,
        @Query("region") region: String,
    ): Call<PrayerModel>
}