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
import com.example.sastabazar.databinding.ActivityPaymentBinding
import com.example.sastabazar.databinding.PaymentsuccesDialogboxBinding
import com.example.sastabazar.model.ProductModel

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var productList:ArrayList<ProductModel>
    private lateinit var adapter:CartAdapter
    private lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productList =ArrayList()

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
        adapter = CartAdapter(this , productList)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //setting prize
        binding.subTotalTxt.text = dressPrize.toString()
        binding.totalPrizeTxt.text = totalPrice.toString()
        binding.quantity.text = dressQuantity.toString()


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