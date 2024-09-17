package com.example.namazvakitleri.retrofit

import com.example.namazvakitleri.service.LocationAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {

    private val countryURL = "https://vakit.vercel.app/"

    fun retrofitBuilder() : LocationAPI {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(countryURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitBuilder.create(LocationAPI::class.java)
    }
}