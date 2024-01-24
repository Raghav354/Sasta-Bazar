package com.example.sastabazar.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sastabazar.model.ProductModel
import com.example.sastabazar.R
import com.example.sastabazar.activities.DetailActivity
import com.example.sastabazar.databinding.RvItemCatBinding

class CategoryAdapter(var context: Context, var productList: ArrayList<ProductModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: RvItemCatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RvItemCatBinding.inflate(LayoutInflater.from(context), parent, false)
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
        holder.binding.productName.text = productList.get(position).name
        holder.binding.productCode.text = productList.get(position).id
        holder.binding.productPrice.text = productList.get(position).price.toString()

        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, DetailActivity::class.java).putExtra(
                    "PRODUCT_ID",
                    productList.get(position).id
                )
            )

        }
    }

}