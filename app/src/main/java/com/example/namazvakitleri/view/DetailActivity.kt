package com.example.namazvakitleri.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.namazvakitleri.databinding.ActivityDetailBinding
import com.example.namazvakitleri.model.County
import com.example.namazvakitleri.model.PrayerModel
import com.example.namazvakitleri.service.LocationAPI
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val countryURL = "https://vakit.vercel.app/"
    private var timesList: ArrayList<String>? = null
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private var county: String? = null
    private var city: String? = null
    private lateinit var myCountylist: ArrayList<County>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //myCountylist = ArrayList<County>()

        getTimeFromPlace()
    }

    private fun updateButton(){
        val info = intent.getStringExtra("info")
        if (info.equals("old")){
            binding.saveBtn.visibility = View.GONE
        }
    }

    private fun intentMainActivityPrayerList() {
        val intent = Intent(this@DetailActivity, MainActivityPrayerList::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //bundan önce ne kadar activity varsa hepsini kapatır.
        startActivity(intent) //MainActivity e geri dönüyoruz.
    }

    private fun retrofitBuilder(): LocationAPI {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(countryURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitBuilder.create(LocationAPI::class.java)
    }

    private fun getTimeFromPlace() {
        city = intent.getStringExtra("city")
        county = intent.getStringExtra("county")
        if (!city.isNullOrEmpty() && !county.isNullOrEmpty()) {
            val retrofit = retrofitBuilder()
            val call = retrofit.getTimeFromPlace(region = county!!, city = city!!)
            call.enqueue(
                object : Callback<PrayerModel> {
                    override fun onResponse(
                        call: Call<PrayerModel>,
                        response: Response<PrayerModel>
                    ) {//if result true
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Log.d("TAG", "country response: ${response.body()}")
                                binding.textViewCounty.text = county!!.toUpperCase()
                                binding.textViewCity.text =
                                    city!!.toUpperCase() + " İÇİN NAMAZ \n VAKİTLERİ"
                                it.times.forEach { (key, value) ->
                                    timesList = ArrayList(value)
                                    //Log.d("TAG", "key: $key, value: $value")
                                    //Log.d("TAG", "valueList: ${timesList.toString()}")
                                }
                                timesList?.let {
                                    binding.textViewImsak.text = "İmsak : ${timesList!![0]}"
                                    binding.textViewGunes.text = "Güneş : ${timesList!![1]}"
                                    binding.textViewOgle.text = "Öğle : ${timesList!![2]}"
                                    binding.textViewIkindi.text = "İkindi : ${timesList!![3]}"
                                    binding.textViewAksam.text = "Akşam : ${timesList!![4]}"
                                    binding.textViewYatsi.text = "Yatsı : ${timesList!![5]}"

                                }

                                updateButton()
                            }
                        }
                    }

                    override fun onFailure(call: Call<PrayerModel>, t: Throwable) {
                        Log.d("TAG", "onFailure: ${t.message}\"", t)
                    }

                })
        }
    }

    fun saveButtonOnClicked(view: View) {
        val county = generateCounty()

        //myCountylist.add(County(county.countyName, county.cityName))

        sharedPreferences = getSharedPreferences("my_shared_pref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val counties = ArrayList<County>()
        sharedPreferences.getString("counties", null)?.let { countyJson ->
            val oldCounties = Gson().fromJson(countyJson, Array<County>::class.java)
            counties.addAll(oldCounties)
        }
        counties.add(county)
        val json = Gson().toJson(counties)
        editor.putString("counties", json)

//        editor.putString("county", county.countyName)
//        editor.putString("city", county.cityName)
        editor.apply()

        intentMainActivityPrayerList()
    }

    private fun generateCounty(): County {

        val countyName = county
        val cityName = city
        return County(countyName!!, cityName!!)
    }
}