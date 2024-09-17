package com.example.namazvakitleri.view

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namazvakitleri.R
import com.example.namazvakitleri.adapter.RecyclerViewAdapter
import com.example.namazvakitleri.databinding.ActivityMainBinding
import com.example.namazvakitleri.service.LocationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val countryURL = "https://vakit.vercel.app/"
    private var country: ArrayList<String>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var searchView: AppCompatEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //searchView
        searchView = binding.searchView


        //recyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        getCounty()
        searchCounty()
    }

    private fun searchCounty(){
        searchView?.doOnTextChanged { text, start, before, count ->
            filterList(text.toString())
        }
    }

    private fun filterList(query: String?) {
        query?.let {

            val filteredList = ArrayList<String>()
            for (i in country!!) {
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

    private fun getCounty() {
        val retrofit = retrofitBuilder()
        val call = retrofit.getCounties()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(
                call: Call<List<String>>,
                response: Response<List<String>>
            ) {//if result true
                if (response.isSuccessful) {
                    response.body()?.let { it ->
                        //Log.d("TAG", "country response: ${response.body()}")
                        country = ArrayList(it)
                        country?.let { result ->
                            recyclerViewAdapter = RecyclerViewAdapter(result)
                            recyclerViewAdapter?.let {
                                it.onItemClicked = { county ->

                                    val intent = Intent(this@MainActivity, CityActivity::class.java)
                                    intent.putExtra("county", county)
                                    startActivity(intent)
                                    /*Toast.makeText(
                                  this@MainActivity,
                                  "Clicked on ${county}",
                                  Toast.LENGTH_LONG
                               ).show()*/

                                }
                            }

                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }
}


/*//api/regions?country=Turkey

//https://vakit.vercel.app/api/regions?country=Turkey
//https://vakit.vercel.app/api/cities??country=Turkey&region=Adana

/*val countries = ArrayAdapter(
    this@MainActivity,
    android.R.layout.simple_spinner_dropdown_item,
    ArrayList(it)
)*/

//binding.countryId.adapter = countries
/*binding.countryId.onItemSelectedListener =
object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        /*Toast.makeText(
            this@MainActivity,
            "Clicked on $position",
            Toast.LENGTH_LONG
        ).show()*/

        selectedRegion = parent?.getItemAtPosition(position).toString()
        getCity()

        /*Toast.makeText(
            this@MainActivity,
            "Clicked on $selectedCountry",
            Toast.LENGTH_LONG
        ).show()*/
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(
            this@MainActivity,
            "Nothing selected....",
            Toast.LENGTH_LONG
        ).show()
    }

}*/*/
