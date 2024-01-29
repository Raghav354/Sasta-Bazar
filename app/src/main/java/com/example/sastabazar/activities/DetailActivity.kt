package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.sastabazar.databinding.ActivityBuyDressBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {
    //initialize fireabase database and auth references
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth:FirebaseAuth

    val binding by lazy {
        ActivityBuyDressBinding.inflate(layoutInflater)
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




            databaseReference = FirebaseDatabase.getInstance().reference
            auth = FirebaseAuth.getInstance()



        }
        binding.buyNow.setOnClickListener{

            val intent = Intent(this@DetailActivity, ShippingActivity::class.java)
            startActivity(intent)
        }


        binding.addToWishlist.setOnClickListener{

        }
        binding.addToCart.setOnClickListener {

        }

    }
}