package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.adaptors.CategoryAdapter
import com.example.sastabazar.databinding.ActivityProjectCatBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ProjectCatActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityProjectCatBinding.inflate(layoutInflater)
    }
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var adapter: CategoryAdapter
    private var isFetchingMore = false
    private var lastVisibleProduct: DocumentSnapshot? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productList = ArrayList()
        adapter = CategoryAdapter(this@ProjectCatActivity, productList)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

        //To fetch data -> category wise
        if (intent.hasExtra("Category")) {
            val category = intent.getStringExtra("Category")

            Firebase.firestore.collection("Products").whereEqualTo("category", category).get()
                .addOnSuccessListener {
                    productList.clear()
                    for (i in it.documents) {
                        var tempProductModel = i.toObject<ProductModel>()
                        tempProductModel?.id = i.id
                        productList.add(tempProductModel!!)
                    }
                    adapter.notifyDataSetChanged()
                }

        } else {
            //See more  -> Fetch all the data

            Firebase.firestore.collection("Products").limit(20).get().addOnSuccessListener {
                productList.clear()
                for (i in it.documents) {
                    var tempProductModel = i.toObject<ProductModel>()
                    tempProductModel?.id = i.id
                    productList.add(tempProductModel!!)
                }
                adapter.notifyDataSetChanged()
            }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isFetchingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    fetchMoreProducts()
                }
            }
        })

        binding.toolbar.setOnClickListener {
            startActivity(Intent(this , HomeActivity::class.java))
            finish()
        }
    }
    private fun fetchMoreProducts() {
        isFetchingMore = true

        val category = intent.getStringExtra("Category")

        Firebase.firestore.collection("Products").whereEqualTo("category", category).get()
            .addOnSuccessListener {
                productList.clear()
                for (i in it.documents) {
                    var tempProductModel = i.toObject<ProductModel>()
                    tempProductModel?.id = i.id
                    productList.add(tempProductModel!!)
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: ${exception.message}", Toast.LENGTH_SHORT).show()
                isFetchingMore = false
            }
    }
}