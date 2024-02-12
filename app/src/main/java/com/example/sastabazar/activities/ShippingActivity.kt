package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import com.example.sastabazar.R
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.databinding.ActivityShippingBinding
import com.example.sastabazar.model.ProductModel

class ShippingActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityShippingBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        productList = ArrayList()


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


        binding.continueButton.setOnClickListener {
            val intentToPayment = Intent(this@ShippingActivity , PaymentActivity::class.java)
            intent.putExtra("DressName" , dressName)
            intent.putExtra("DiscountPrize" , dressPrize)
            intent.putExtra("DressImage" , dressImage)
            intent.putExtra("DressQuantity",dressQuantity)
            intent.putExtra("Size",size)
            intent.putExtra("Color",color)
            startActivity(intentToPayment)
        }


//        val navController = findNavController(R.id.nav_host_fragment_content_shipping)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_shipping)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}