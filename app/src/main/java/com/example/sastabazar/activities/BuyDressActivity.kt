package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.sastabazar.R
import com.example.sastabazar.databinding.ActivityBuyDressBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class BuyDressActivity : AppCompatActivity() {
    private var selectedDressQuantity=1
    private var selectedDressSize ="UK 8"
    private var selectedDressColor = R.drawable.green_color_rectangel_bg
//    private var flashSaleDressItem : ProductModel = ProductModel()
    private lateinit var binding: ActivityBuyDressBinding

    var productModel = ProductModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBuyDressBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val rating = 4f
        binding.ratingBar.rating = rating

        handleBtnClick()
        handleSizeSelection()
        handleColorSelection()


        var productId = intent.getStringExtra("PRODUCT_ID")
        Firebase.firestore.collection("Products").document(productId!!).get().addOnSuccessListener {

            productModel = it.toObject<ProductModel>()!!
            productModel.productUUID = it.id
            binding.productImage.load(productModel.productImage)
            binding.dressname.text = productModel.productName
            binding.productDesc.text = productModel.productDisp
            binding.discountprice.text = productModel.productPrice.toString()

        }

    }

    private fun handleBtnClick() {
        binding.apply {
            add.setOnClickListener{increaseNumber()}
            subtract.setOnClickListener{decreaseNumber()}
            buyNow.setOnClickListener { buyThisDress() }
            addtocart.setOnClickListener { addToCart() }
            addtowishlist.setOnClickListener{addToWishList()}

        }
    }

    private fun addToWishList() {
        Toast.makeText(this@BuyDressActivity,"Successfully added to wishlist.",Toast.LENGTH_SHORT).show()
    }

    private fun addToCart() {
        Toast.makeText(this@BuyDressActivity,"Successfully added to cart.",Toast.LENGTH_SHORT).show()
    }

    private fun buyThisDress() {
        val intentbuynow = Intent(this@BuyDressActivity, ShippingActivity::class.java)
        startActivity(intentbuynow)
    }

    private fun increaseNumber() {
        binding.number.text.toString().toIntOrNull()?.let {number->
            if(number in 2 .. 10)
            {
                binding.number.text=(number+1).toString()
            }
            selectedDressQuantity = number + 1

        }
    }

    private fun decreaseNumber() {
        binding.number.text.toString().toIntOrNull()?.let {number->
            if(number in 2 .. 10)
            {
                binding.number.text=(number-1).toString()
            }
            selectedDressQuantity = number - 1
        }
    }
    private fun handleSizeSelection(){
        binding.apply {
            sizeUk8.setOnClickListener{
                selectedDressSize = "UK 8"
                selectedTextView(sizeUk8)
            }
            sizeUk10.setOnClickListener{
                selectedDressSize = "UK 10"
                selectedTextView(sizeUk10)
            }
            sizeUk12.setOnClickListener{
                selectedDressSize = "UK 12"
                selectedTextView(sizeUk12)
            }
        }
    }

    private fun selectedTextView(textView: TextView) {
        binding.sizeUk8.isSelected = false
        binding.sizeUk10.isSelected = false
        binding.sizeUk12.isSelected = false
        textView.isSelected = true
    }

    private fun handleColorSelection(){
        binding.apply {
            pinkimage.setOnClickListener {
                selectedDressColor = R.color.primary_pink
                selectedColor(pinkimage)
            }
            yellowimage.setOnClickListener {
                selectedDressColor = R.color.dress_yellow
                selectedColor(yellowimage)
            }
            greenimage.setOnClickListener {
                selectedDressColor = R.color.dress_green
                selectedColor(greenimage)
            }
            blueimage.setOnClickListener {
                selectedDressColor = R.color.dress_blue
                selectedColor(blueimage)
            }
        }
    }

    private fun selectedColor(imageView: ImageView) {
        binding.blueimage.isSelected = false
        binding.pinkimage.isSelected = false
        binding.yellowimage.isSelected = false
        binding.greenimage.isSelected = false
        imageView.isSelected = true
    }


}