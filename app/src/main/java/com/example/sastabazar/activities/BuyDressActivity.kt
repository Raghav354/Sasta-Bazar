package com.example.sastabazar.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
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
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Check if the product is already in the cart
        firebaseFirestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(productModel.id!!) // Use product ID as document ID
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Product already exists, remove it from cart
                    removeFromCart()
                    updateButtonState(false)
                } else {
                    // Create the cart item document
                    addProductToCart() // Call creation function and update button
                }
            }
            .addOnFailureListener { e ->
                // Handle errors during existence check
                Toast.makeText(this, "Error checking cart: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromCart() {
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Remove the product document from the cart
         firebaseFirestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(productModel.id!!) // Use product ID as document ID
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Product removed from cart!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle errors during removal
                Toast.makeText(this, "Error removing product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
             // Wait for asynchronous operation to complete
    }


    private fun addProductToCart() {
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

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
//                            "quantity" to FieldValue.increment(selectedDressQuantity)
                // Your cart item data here
            ), SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()
                updateButtonState(true) // Set button state based on addition
            }
            .addOnFailureListener { e ->
                // Handle errors during cart item creation
                Toast.makeText(this, "Error adding item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateButtonState(isInCart: Boolean) {
        binding.addtocart.setBackgroundColor(if (isInCart) Color.RED else R.drawable.add_to_cart_bg)
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
            if(number in 1 .. 9)
            {
                binding.number.text=(number+1).toString()
                if(number == 9)
                {
                    Toast.makeText(this , "You can order maximum 10 at a time.",Toast.LENGTH_SHORT).show()
                }
            }
            selectedDressQuantity = number + 1
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun decreaseNumber() {
        binding.number.text.toString().toIntOrNull()?.let {number->
            if(number in 1 .. 10)
            {
                if(binding.number.text.toString() == "1")
                {
                    Toast.makeText(this , "You can order minimum 1 at a time.",Toast.LENGTH_SHORT).show()
                }
                else
                binding.number.text = (number-1).toString()
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