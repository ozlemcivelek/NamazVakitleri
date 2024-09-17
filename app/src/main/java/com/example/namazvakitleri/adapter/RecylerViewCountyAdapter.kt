package com.example.namazvakitleri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.namazvakitleri.databinding.RecyclerRowCountyBinding
import com.example.namazvakitleri.model.County

class RecylerViewCountyAdapter (private var myCountylist: ArrayList<County>) : RecyclerView.Adapter<RecylerViewCountyAdapter.MyCountyHolder>() {

    var onItemClicked: (County) -> Unit = {}

    class MyCountyHolder(val binding: RecyclerRowCountyBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCountyHolder {
        val binding = RecyclerRowCountyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCountyHolder(binding)
    }

    override fun getItemCount(): Int {
        return myCountylist.size
    }

    override fun onBindViewHolder(holder: MyCountyHolder, position: Int) {
        val item = myCountylist[position]
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.binding.recyclerViewTextViewCounty.text = item.countyName
        holder.binding.recyclerViewTextViewCity.text = item.cityName
    }
}