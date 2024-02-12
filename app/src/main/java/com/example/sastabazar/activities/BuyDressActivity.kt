package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import coil.load
import com.example.sastabazar.CartFragment
import com.example.sastabazar.R
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.databinding.ActivityBuyDressBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue.increment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class BuyDressActivity : AppCompatActivity() {
    private var selectedDressQuantity = 1
    private var selectedDressSize ="UK 8"
    private var selectedDressColor = R.drawable.pink_color_item
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
            productModel.id = it.id
            binding.productImage.load(productModel.imageUrl)
            binding.dressname.text = productModel.name
            binding.productDesc.text = productModel.disp
            binding.discountprice.text = productModel.price.toString()

        }

//        val navController = findNavController(R.id.nav_host_fragment_activity_home)
//        navController.navigate(R.id.navigation_cart)






    }

    private fun handleBtnClick() {
        binding.apply {
            add.setOnClickListener{increaseNumber()}
            subtract.setOnClickListener{decreaseNumber()}
            buynow.setOnClickListener { buyThisDress() }
            addtocart.setOnClickListener { addToCart() }
            addtowishlist.setOnClickListener{addToWishList()}
            toolbar.setOnClickListener{backToHome()}

        }
    }

    private fun backToHome() {
        startActivity(Intent(this@BuyDressActivity , HomeActivity::class.java))
        finish()
    }

    private fun addToWishList() {
        binding.addtowishlist.setOnClickListener {
            val firebaseFirestore = Firebase.firestore
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            // Create/update wishlist item document
            firebaseFirestore.collection("users")
                .document(userId)
                .collection("wishlist")
                .document(productModel.id!!) // Use product ID as document ID
                .set(hashMapOf(
                    "id" to productModel.id,
                    "name" to productModel.name,
                    "imageUrl" to productModel.imageUrl,
                    "price" to productModel.price,
                    "size" to selectedDressSize, // Optional if you store size
                ), SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Item added to wishlist!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding item: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addToCart() {
        binding.addtocart.setOnClickListener {
            val firebaseFirestore = Firebase.firestore
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            // Create/update cart item document
            firebaseFirestore.collection("users")
                .document(userId)
                .collection("cart")
                .document(productModel.id!!) // Use product ID as document ID
                .set(hashMapOf(
                    "id" to productModel.id,
                    "name" to productModel.name,
                    "imageUrl" to productModel.imageUrl,
                    "price" to productModel.price,
                    "size" to selectedDressSize, // Optional if you store size
//                    "quantity" to FieldValue.increment(selectedDressQuantity)
                ), SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding item: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

//        // Replace "navigation_cart" with your actual destination ID
//        val navController = findNavController(R.id.nav_host_fragment_activity_home)
//        navController.navigate(R.id.navigation_cart, bundle)

    }
    private fun buyThisDress() {
        val intent = Intent(this@BuyDressActivity , ShippingActivity::class.java)
        intent.putExtra("DressName" , binding.dressname.text.toString())
        intent.putExtra("DiscountPrize" , binding.discountprice.text.toString())
        intent.putExtra("DressImage" , binding.productImage.toString())
        startActivity(intent)
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