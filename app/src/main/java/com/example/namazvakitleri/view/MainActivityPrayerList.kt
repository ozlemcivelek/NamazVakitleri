package com.example.namazvakitleri.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namazvakitleri.R
import com.example.namazvakitleri.adapter.RecyclerViewAdapter
import com.example.namazvakitleri.adapter.RecylerViewCountyAdapter
import com.example.namazvakitleri.databinding.ActivityMainPrayerListBinding
import com.example.namazvakitleri.model.County
import com.example.namazvakitleri.service.LocationAPI
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivityPrayerList : AppCompatActivity() {

    private lateinit var binding: ActivityMainPrayerListBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var recyclerViewAdapter: RecylerViewCountyAdapter
    private lateinit var myCountyList: ArrayList<County>
    private val countryURL = "https://vakit.vercel.app/"
    private var city: ArrayList<String>? = null
    private var searchView: AppCompatEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPrayerListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        searchView = binding.searchView

        myCountyList = ArrayList()
        sharedPreferences = getSharedPreferences("my_shared_pref", MODE_PRIVATE)

        sharedPreferences.getString("counties", null)?.let { countyJson ->
            val counties = Gson().fromJson(countyJson, Array<County>::class.java)
            myCountyList.addAll(counties)
        }
        recyclerViewAdapter = RecylerViewCountyAdapter(myCountyList)

        //recyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerViewCounty.layoutManager = layoutManager


        binding.recyclerViewCounty.adapter = recyclerViewAdapter
        binding.recyclerViewCounty.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerViewAdapter.notifyDataSetChanged()

        recyclerViewAdapter.onItemClicked = {
            val intent = Intent(this@MainActivityPrayerList, DetailActivity::class.java)
            intent.putExtra("info", "old")
            intent.putExtra("city", it.cityName)
            intent.putExtra("county", it.countyName)
            startActivity(intent)
        }

        recyclerViewAdapter.onDeleteClicked = {
            myCountyList.remove(it)
            recyclerViewAdapter.notifyItemRemoved(myCountyList.indexOf(it))
        }

        searchCity()
    }

    override fun onStart() {
        super.onStart()
        binding.searchView.text = null
    }
    private fun searchCity(){
        searchView?.doOnTextChanged { text, start, before, count ->
            filterList(text.toString())
        }
    }

    private fun filterList(query: String?) {
        query?.let {
            val filteredList = ArrayList<County>()
            for (i in myCountyList) {
                if (i.countyName.lowercase().contains(query)) {
                    filteredList.add(i)
                }
                else if (i.cityName.lowercase().contains(query)) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflater --> If we want to connect xml and code together
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.county_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    /*override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenu.ContextMenuInfo
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.county_menu, menu)
    }*/

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.new_county -> { // ascending
            val intent = Intent(this@MainActivityPrayerList, MainActivity::class.java)
            intent.putExtra("info", "new")
            startActivity(intent)
            myCountyList.sortBy { it.countyName }
            recyclerViewAdapter.notifyItemRangeChanged(0, myCountyList.size)
            true
        }

        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}

