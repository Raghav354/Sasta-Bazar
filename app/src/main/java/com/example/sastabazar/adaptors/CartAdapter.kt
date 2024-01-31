package com.example.sastabazar.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.databinding.RvItemCartBinding
import com.example.sastabazar.model.ProductModel

class CartAdapter(var context:Context , var productList:ArrayList<ProductModel>)
    : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
        inner class ViewHolder(var binding:RvItemCartBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RvItemCartBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

}