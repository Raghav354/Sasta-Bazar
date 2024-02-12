package com.example.sastabazar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.R
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.databinding.ActivityPaymentBinding
import com.example.sastabazar.databinding.FragmentCartBinding
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
        val dressImage = intent.getStringExtra("DressImage")
        val dressQuantity = intent.getIntExtra("DressQuantity",1)
        val size = intent.getStringExtra("Size")
        val color = intent.getStringExtra("DressColor")

        productList.add(ProductModel(null , dressName , dressPrize?.toDouble() , null , null ,dressImage , "AVG45"))
        adapter = CartAdapter(this , productList)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //setting prize
        binding.subTotalTxt.text = dressPrize.toString()
        binding.totalPrizeTxt.text = dressPrize.toString()

    }
}