package com.example.sastabazar.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.sastabazar.R
import com.example.sastabazar.databinding.ActivityBuyDressBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class BuyDressActivity : AppCompatActivity() {
    private var selectedDressQuantity = 1
    private var selectedDressSize ="S"
    private var selectedDressColor = R.drawable.pink_color_item
//    private var flashSaleDressItem : ProductModel = ProductModel()
    private lateinit var binding: ActivityBuyDressBinding
    private var firebaseFirestore = Firebase.firestore
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

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

        val productId = intent.getStringExtra("PRODUCT_ID")
        productId?.let {
            getProductDetails(it)
            checkWishlistStatus(it)
            checkCartStatus(it)
        }

        binding.addtowishlist.setOnClickListener {
            productId?.let { toggleWishlistStatus(it) }
        }
        binding.addtocart.setOnClickListener {
            productId?.let { toggleCartStatus(it) }
        }


//        val navController = findNavController(R.id.nav_host_fragment_activity_home)
//        navController.navigate(R.id.navigation_cart)

    }

    private fun toggleCartStatus(productId: String) {
        val docRef = firebaseFirestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(productId)

        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    removeFromCart(docRef)
                } else {
                    addToCart(docRef)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error toggling cart status: $e")
            }

    }

    private fun removeFromCart(docRef: DocumentReference) {
        docRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Item removed from cart!", Toast.LENGTH_SHORT).show()
                updateButtonState(false)
                binding.addtocart.text = "Add to cart"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error removing item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToCart(docRef: DocumentReference) {
        docRef.set(
            hashMapOf(
                "productCoupanCode" to productModel.productCoupanCode,
                "name" to productModel.name,
                "imageUrl" to productModel.imageUrl,
                "discountPrice" to productModel.discountPrice,
                "productSize" to productModel.productSize,
                "productColor" to selectedDressColor.toString()
                // Add other product details as needed
            ), SetOptions.merge()
        )
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show()
                updateButtonState(true)
                binding.addtocart.text = "Tap to remove from cart"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkCartStatus(productId: String) {
        firebaseFirestore.collection("users")
            .document(userId)
            .collection("cart")
            .document(productId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    updateButtonState(true)
                    binding.addtocart.text = "Tap to remove from cart"
                } else {
                    updateButtonState(false)
                    binding.addtocart.text = "Add to cart"
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error checking wishlist status: $e")
            }
    }


    private fun getProductDetails(productId: String) {
        firebaseFirestore.collection("Products").document(productId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    productModel = documentSnapshot.toObject<ProductModel>()!!
                    productModel.id = documentSnapshot.id

                    // Load product details to UI
                    binding.productImage.load(productModel.imageUrl)
                    binding.dressname.text = productModel.name
                    binding.productDesc.text = productModel.disp
                    binding.discountprice.text = productModel.discountPrice.toString()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error getting product details: $e")
            }
    }

    private fun handleBtnClick() {
        binding.apply {
            add.setOnClickListener{increaseNumber()}
            subtract.setOnClickListener{decreaseNumber()}
            buynow.setOnClickListener { buyThisDress() }
//            addtocart.setOnClickListener { addToCart() }
            toolbar.setOnClickListener{backToHome()}

        }
    }

    private fun backToHome() {
        startActivity(Intent(this@BuyDressActivity , HomeActivity::class.java))
        finish()
    }

    private fun addToWishlist(docRef: DocumentReference) {
        docRef.set(
            hashMapOf(
                "productCoupanCode" to productModel.productCoupanCode,
                "name" to productModel.name,
                "imageUrl" to productModel.imageUrl,
                "discountPrice" to productModel.discountPrice,
                "productSize" to productModel.productSize,
                "productColor" to selectedDressColor.toString()
                // Add other product details as needed
            ), SetOptions.merge()
        )
            .addOnSuccessListener {
                Toast.makeText(this, "Item added to wishlist!", Toast.LENGTH_SHORT).show()
                binding.addtowishlist.setImageResource(R.drawable.icon_heart_filled)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to add item to wishlist: $e")
                Toast.makeText(this, "Error adding item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkWishlistStatus(productId: String) {
        firebaseFirestore.collection("users")
            .document(userId)
            .collection("wishlist")
            .document(productId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    binding.addtowishlist.setImageResource(R.drawable.icon_heart_filled)
                } else {
                    binding.addtowishlist.setImageResource(R.drawable.icon_heart)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error checking wishlist status: $e")
            }
    }

    private fun removeFromWishlist(docRef: DocumentReference) {
        docRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Item removed from wishlist!", Toast.LENGTH_SHORT).show()
                binding.addtowishlist.setImageResource(R.drawable.icon_heart)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to remove item from wishlist: $e")
                Toast.makeText(this, "Error removing item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleWishlistStatus(productId: String) {
        val docRef = firebaseFirestore.collection("users")
            .document(userId)
            .collection("wishlist")
            .document(productId)

        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    removeFromWishlist(docRef)
                } else {
                    addToWishlist(docRef)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error toggling wishlist status: $e")
            }
    }

    private fun updateButtonState(isInCart: Boolean) {
        binding.addtocart.setBackgroundColor(
            if (isInCart)
                Color.RED
            else
                R.drawable.add_to_cart_bg
        )
    }

    private fun buyThisDress() {
        val intent = Intent(this@BuyDressActivity , ShippingActivity::class.java)
        intent.putExtra("DressName" , binding.dressname.text.toString())
        intent.putExtra("DiscountPrize" , binding.discountprice.text.toString())
        val quantity = selectedDressQuantity
        intent.putExtra("DressQuantity", quantity)
        val totalPrice = quantity * binding.discountprice.text.toString().toDouble()
        intent.putExtra("TotalPrice", totalPrice)
        intent.putExtra("ImageUrl", productModel.imageUrl)
        intent.putExtra("DressColor",selectedDressColor)
        intent.putExtra("Size",selectedDressSize)
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
                selectedDressSize = "S"
                selectedTextView(sizeUk8)
            }
            sizeUk10.setOnClickListener{
                selectedDressSize = "M"
                selectedTextView(sizeUk10)
            }
            sizeUk12.setOnClickListener{
                selectedDressSize = "L"
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
                Toast.makeText(this@BuyDressActivity , "This color is not available for this dress...",Toast.LENGTH_SHORT).show()

            }
            yellowimage.setOnClickListener {
                selectedDressColor = R.color.dress_yellow
                selectedColor(yellowimage)
                Toast.makeText(this@BuyDressActivity , "This color is not available for this dress...",Toast.LENGTH_SHORT).show()

            }
            greenimage.setOnClickListener {
                selectedDressColor = R.color.dress_green
                selectedColor(greenimage)
                Toast.makeText(this@BuyDressActivity , "This color is not available for this dress...",Toast.LENGTH_SHORT).show()

            }
            blueimage.setOnClickListener {
                selectedDressColor = R.color.dress_blue
                selectedColor(blueimage)
                Toast.makeText(this@BuyDressActivity , "This color is not available for this dress...",Toast.LENGTH_SHORT).show()

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