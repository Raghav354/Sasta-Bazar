package com.example.sastabazar.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sastabazar.R
import com.example.sastabazar.activities.BuyDressActivity
import com.example.sastabazar.databinding.RvItemBinding
import com.example.sastabazar.model.ProductModel

class ProductAdapter(var context: Context, var productList: ArrayList<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    inner class ViewHolder(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    //Uploading the products on the app from firebase and set the name and prize of the product
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.productImage.load(productList.get(position).imageUrl)
        {
            placeholder(R.drawable.image)
        }
        holder.binding.dressname.text = productList.get(position).name
        holder.binding.productCode.text = productList.get(position).productCoupanCode
        holder.binding.discountprice.text =
            "Rs. " + productList.get(position).discountPrice.toString()
        holder.binding.originalprice.text =
            "Rs." + productList.get(position).originalPrice.toString()
        holder.binding.discountpercent.text =
            productList.get(position).discountPercentage.toString() + "%Off"


        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, BuyDressActivity::class.java).putExtra(
                    "PRODUCT_ID",
                    productList.get(position).id
                )
            )

        }


    }
}