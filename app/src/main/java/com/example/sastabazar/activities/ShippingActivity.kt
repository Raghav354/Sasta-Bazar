package com.example.sastabazar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        productList.add(ProductModel(null , dressName , dressPrize?.toDouble() , null , null ,dressImage , "AVG45"))
        adapter = CartAdapter(this , productList)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



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