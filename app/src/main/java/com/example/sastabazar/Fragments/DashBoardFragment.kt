package com.example.sastabazar.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sastabazar.R
import com.example.sastabazar.activities.ProjectCatActivity
import com.example.sastabazar.adaptors.ProductAdapter
import com.example.sastabazar.databinding.FragmentDashBoardBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var adapter: ProductAdapter
    val moreProductsList = ArrayList<ProductModel>()
    private lateinit var moreProductsAdapter:ProductAdapter
    private var isFetchingMore = false
    private var lastVisibleProduct: DocumentSnapshot? = null
    private val firestore = Firebase.firestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleButtonClick()

        productList = ArrayList()
        adapter = ProductAdapter(requireContext(), productList)
        binding.mainRV.adapter = adapter

        moreProductsAdapter = ProductAdapter(requireContext(), moreProductsList)
        binding.moreProductsRV.adapter = moreProductsAdapter

        fetchingData(productList, adapter)
        fetchingData(moreProductsList, moreProductsAdapter)
        imageSlider()



        binding.mainRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val threshold = 5

                // For horizontally scrolled RecyclerView, check if the last visible item is close to the right edge
                val lastVisibleItemView = layoutManager.findViewByPosition(lastVisibleItemPosition)
                val isCloseToRightEdge = recyclerView.width - lastVisibleItemView?.right!! <= threshold

                if (!isFetchingMore && totalItemCount - 1 <= lastVisibleItemPosition && isCloseToRightEdge) {
                    fetchMoreProducts()
                }
            }
        })



        // Add scroll listener for endless scrolling
        binding.moreProductsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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





    }

    private fun imageSlider() {
//        Image slider ->  imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.sale1))
        imageList.add(SlideModel(R.drawable.sale2))
        imageList.add(SlideModel(R.drawable.sale1))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
    }

    private fun fetchingData(productList: ArrayList<ProductModel>, adapter: ProductAdapter) {
        val productsCollection = firestore.collection("Products").limit(20)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                val documents = productsCollection.get().await()

                productList.clear()
                for (document in documents) {
                    val tempProductModel = document.toObject<ProductModel>()
                    tempProductModel.id = document.id
                    productList.add(tempProductModel)
                }

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchMoreProducts() {
        isFetchingMore = true

        val productsCollection = if (lastVisibleProduct != null) {
            firestore.collection("Products")
                .startAfter(lastVisibleProduct!!)
                .limit(20)
        } else {
            firestore.collection("Products")
                .limit(20)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            try {
                val documents = productsCollection.get().await()

                for (document in documents) {
                    val product = document.toObject<ProductModel>()
                    product.id = document.id
                    moreProductsList.add(product)
                }

                moreProductsAdapter.notifyDataSetChanged()
                isFetchingMore = false
                if (!documents.isEmpty) {
                    lastVisibleProduct = documents.documents[documents.size() - 1]
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error getting documents: ${e.message}", Toast.LENGTH_SHORT).show()
                isFetchingMore = false
            }
        }
    }

    private fun handleButtonClick() {
        binding.apply {
            dress.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Dresses")
                )
            }
            jumpsuits.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "JumpSuits")
                )
            }
            bottoms.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Bottoms")
                )
            }

            saree.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Saree")
                )
            }
            tops.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Tops")
                )
            }
            kurta.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Kurti")
                )
            }
            shirt.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Shirt")
                )
            }
            tops.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Tops")
                )
            }

            accessories.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(), ProjectCatActivity::class.java
                    ).putExtra("Category", "Accessories")
                )
            }

            seeMore.setOnClickListener {
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
            }
            seeMore2.setOnClickListener {
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
            }
        }
    }


}