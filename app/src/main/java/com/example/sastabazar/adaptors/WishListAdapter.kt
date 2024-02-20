package com.example.sastabazar.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.sastabazar.R
import com.example.sastabazar.activities.BuyDressActivity
import com.example.sastabazar.databinding.RvItemWishlistBinding
import com.example.sastabazar.model.ProductModel

class WishListAdapter(var context: Context , var productList:ArrayList<ProductModel>)
    : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    inner class ViewHolder(var binding:RvItemWishlistBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RvItemWishlistBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.productImage.load(productList.get(position).imageUrl)
        {
            placeholder(R.drawable.image)
        }
        holder.binding.dressname.text = productList.get(position).name
        holder.binding.coupancode.text = productList.get(position).id
        holder.binding.price.text = "Rs. " + productList.get(position).discountPrice.toString()
//        holder.binding.color.text = productList.get(position).color.toString()
//        holder.binding.size.text = productList.get(position).size.toString()

        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, BuyDressActivity::class.java)
                    .putExtra("PRODUCT_ID", productList.get(position).id)
            )

        }
    }

}