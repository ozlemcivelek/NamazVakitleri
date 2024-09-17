package com.example.namazvakitleri.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.namazvakitleri.databinding.RecyclerRowBinding

class RecyclerViewAdapter(private var countrylist: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.CountryHolder>() {

    var onItemClicked: (String) -> Unit = {}

    class CountryHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    fun setFilteredList(filteredList: ArrayList<String>) {
        this.countrylist = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val item = countrylist[position]
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.binding.recyclerViewTextView.text = item
    }

    override fun getItemCount(): Int {
        return countrylist.size
    }


}