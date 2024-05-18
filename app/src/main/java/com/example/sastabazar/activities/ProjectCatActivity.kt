package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // To fetch data -> category wise
        if (intent.hasExtra("Category")) {
            val category = intent.getStringExtra("Category")
            fetchInitialProducts(category)
        } else {
            // See more -> Fetch all the data
            fetchInitialProducts(null)
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
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun fetchInitialProducts(category: String?) {
        val query = if (category != null) {
            Firebase.firestore.collection("Products").whereEqualTo("category", category).limit(20)
        } else {
            Firebase.firestore.collection("Products").limit(20)
        }

        query.get().addOnSuccessListener {
            productList.clear()
            for (i in it.documents) {
                val tempProductModel = i.toObject<ProductModel>()
                tempProductModel?.id = i.id
                productList.add(tempProductModel!!)
            }
            adapter.notifyDataSetChanged()
            if (it.documents.isNotEmpty()) {
                lastVisibleProduct = it.documents[it.size() - 1]
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMoreProducts() {
        if (lastVisibleProduct == null) return

        isFetchingMore = true
        val category = intent.getStringExtra("Category")
        val query = if (category != null) {
            Firebase.firestore.collection("Products").whereEqualTo("category", category)
                .startAfter(lastVisibleProduct!!).limit(20)
        } else {
            Firebase.firestore.collection("Products").startAfter(lastVisibleProduct!!).limit(20)
        }

        query.get().addOnSuccessListener {
            for (i in it.documents) {
                val tempProductModel = i.toObject<ProductModel>()
                tempProductModel?.id = i.id
                productList.add(tempProductModel!!)
            }
            adapter.notifyDataSetChanged()
            if (it.documents.isNotEmpty()) {
                lastVisibleProduct = it.documents[it.size() - 1]
            }
            isFetchingMore = false
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error getting documents: ${exception.message}", Toast.LENGTH_SHORT).show()
            isFetchingMore = false
        }
    }
}
