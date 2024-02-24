package com.example.sastabazar.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.R
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.adaptors.PaymentAdapter
import com.example.sastabazar.databinding.ActivityPaymentBinding
import com.example.sastabazar.databinding.PaymentsuccesDialogboxBinding
import com.example.sastabazar.model.ProductModel

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var productList:ArrayList<ProductModel>
    private lateinit var paymentAdapter:PaymentAdapter
    private lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productList =ArrayList()

        // Retrieve data source identifier
        val dataSource = intent.getStringExtra("DATA_SOURCE")
        when (dataSource) {
            "BUY_DRESS" -> handleDataFromBuyDressActivity()
            "CART_FRAGMENT" -> handleDataFromCartFragment()
            else -> {
                // Handle unknown data source or default case
                Toast.makeText(this, "Unknown data source", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.payBtn.setOnClickListener {
            val selectedRadioButtonId = binding.radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId == R.id.radioButton1) {
                Toast.makeText(this@PaymentActivity, "Redirected to Payment Gateway", Toast.LENGTH_SHORT).show()
            } else if (selectedRadioButtonId == R.id.radioButton2) {
                showPaymentSuccessDialog()
            } else {
                Toast.makeText(this@PaymentActivity, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun handleDataFromCartFragment() {
        val cartItems = intent.getSerializableExtra("CART_ITEMS") as ArrayList<ProductModel>
        var totalPrice = 0.0
        val totalQuantity = cartItems.size

        // Initialize RecyclerView and Adapter with cart items
        paymentAdapter = PaymentAdapter(this, cartItems)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = paymentAdapter

        for(product in cartItems){
            if(product.discountPrice != null){
                totalPrice += product.discountPrice!!
            }
        }

        // Set total price
        binding.totalPrizeTxt.text = "$totalPrice"
        binding.quantity.text = "$totalQuantity"
        binding.subTotalTxt.text = totalPrice.toString()
    }

    private fun handleDataFromBuyDressActivity() {
        val intent = intent
        val dressName = intent.getStringExtra("DressName")
        val dressPrize = intent.getStringExtra("DiscountPrize")
        val imageUrl = intent.getStringExtra("ImageUrl")
        val dressQuantity = intent.getIntExtra("DressQuantity", 1)
        val size = intent.getStringExtra("Size")
        val color = intent.getStringExtra("DressColor")
        val totalPrice = intent.getDoubleExtra("TotalPrice", 0.0)

        productList.add(
            ProductModel(
                null,
                dressName,
                dressPrize?.toDouble(),
                null,
                null,
                color,
                imageUrl.toString(),
                "AVG45",
                discountPercentage = null,
                size
            )
        )
        paymentAdapter = PaymentAdapter(this , productList)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = paymentAdapter

        //setting prize
        binding.subTotalTxt.text = dressPrize.toString()
        binding.totalPrizeTxt.text = totalPrice.toString()
        binding.quantity.text = dressQuantity.toString()

    }

    private fun showPaymentSuccessDialog() {
        val dialog = Dialog(this@PaymentActivity)
        val dialogBinding = PaymentsuccesDialogboxBinding.inflate(LayoutInflater.from(this@PaymentActivity))
        dialog.setContentView(dialogBinding.root)

        dialogBinding.startShopping.setOnClickListener {
            startActivity(Intent(this@PaymentActivity , HomeActivity::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }
}