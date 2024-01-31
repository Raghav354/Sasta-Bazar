package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.sastabazar.databinding.ActivityDetailBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    var productModel = ProductModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var productId = intent.getStringExtra("PRODUCT_ID")
        Firebase.firestore.collection("Products").document(productId!!).get().addOnSuccessListener {

            productModel=it.toObject<ProductModel>()!!
            productModel.id=it.id
            binding.productImage.load(productModel.imageUrl)
            binding.productName.text = productModel.name
            binding.productDesc.text = productModel.disp
            binding.productPrice.text = productModel.price.toString()

        }
        binding.buyNow.setOnClickListener{
            startActivity(Intent(this@DetailActivity, ShippingActivity::class.java))
        }
        binding.addToWishlist.setOnClickListener{

        }

    }
}