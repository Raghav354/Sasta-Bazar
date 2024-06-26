package com.example.sastabazar.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() , PaymentResultListener {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var productList:ArrayList<ProductModel>
    private lateinit var paymentAdapter:PaymentAdapter
    private lateinit var recyclerView:RecyclerView
    private var totalAmount=0.0
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

            if (selectedRadioButtonId == R.id.razorPayButton) {
                Toast.makeText(this@PaymentActivity, "Redirected to Payment Gateway", Toast.LENGTH_SHORT).show()
                makePayment()
                binding.mainLayout.visibility = View.GONE
            } else if (selectedRadioButtonId == R.id.COD) {
                binding.mainLayout.visibility = View.GONE
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
        val coupanCode = intent.getStringExtra("CoupanCode")
        totalAmount = totalPrice

        productList.add(
            ProductModel(
                null,
                dressName,
                dressPrize?.toDouble(),
                null,
                null,
                color,
                imageUrl.toString(),
                coupanCode,
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

    private fun makePayment() {
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","R Industries")
            options.put("description","Dress Prize")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount",totalAmount*100)//pass amount in currency subunits


//            val retryObj = JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(p0: String?) {
        showPaymentSuccessDialog()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this , "Payment failed...$p1",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this , PaymentActivity::class.java))
    }
}