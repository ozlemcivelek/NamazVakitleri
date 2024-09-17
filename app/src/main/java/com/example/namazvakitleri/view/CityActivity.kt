package com.example.namazvakitleri.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namazvakitleri.adapter.RecyclerViewAdapter
import com.example.namazvakitleri.databinding.ActivityCityBinding
import com.example.namazvakitleri.service.LocationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityBinding
    private val countryURL = "https://vakit.vercel.app/"
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var city: ArrayList<String>? = null
    private var searchView: AppCompatEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //searchView
        searchView = binding.searchView

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        getCity()
        searchCity()
    }

    private fun searchCity(){
        searchView?.doOnTextChanged { text, start, before, count ->
            filterList(text.toString())
        }
    }

    private fun filterList(query: String?) {
        query?.let {
            val filteredList = ArrayList<String>()
            for (i in city!!) {
                if (i.lowercase().contains(query)) {
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty())
            {
                Toast.makeText(this,"No found data", Toast.LENGTH_LONG).show()
            }
            else
            {
                recyclerViewAdapter?.setFilteredList(filteredList)
            }

        }
    }

    private fun retrofitBuilder() : LocationAPI {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(countryURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitBuilder.create(LocationAPI::class.java)
    }

    private fun getCity() {
        val county = intent.getStringExtra("county")
        county?.let {
            val retrofit = retrofitBuilder()
            val call = retrofit.getCities(region = county)
            call.enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                )
                {//if result true
                    if (response.isSuccessful) {
                        response.body()?.let { it ->
                            city = ArrayList(it)
                            city?.let { result ->
                                recyclerViewAdapter = RecyclerViewAdapter(result)
                                recyclerViewAdapter?.let {
                                    it.onItemClicked = { city ->
                                        val intent = Intent(this@CityActivity, DetailActivity::class.java)
                                        intent.putExtra("city", city)
                                        intent.putExtra("county", county)
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                        /*Toast.makeText(
                                            this@CityActivity,
                                            city,
                                            Toast.LENGTH_LONG
                                        ).show()*/
                                    }
                                }

                                binding.recyclerView.adapter = recyclerViewAdapter
                            }
                        }
                    }
                    /*Toast.makeText(
                        this@MainActivity,
                        "Clicked on ${response.body()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("TAG", "onResponse: ${response.body()}")*/
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.d("TAG", "onFailure: ")
                }

            })}



    }
}