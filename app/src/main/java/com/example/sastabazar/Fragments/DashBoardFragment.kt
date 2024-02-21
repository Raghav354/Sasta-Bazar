package com.example.sastabazar.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var adapter: ProductAdapter
    val moreProductsList = ArrayList<ProductModel>()
    private lateinit var moreProductsAdapter:ProductAdapter
    private var isFetchingMore = false


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

        productList = ArrayList()
        adapter = ProductAdapter(requireContext(),productList)
        binding.mainRV.adapter = adapter

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


        moreProductsList.clear() // Ensure initial clear state
        moreProductsAdapter = ProductAdapter(requireContext(), moreProductsList)
        binding.moreProductsRV.adapter = moreProductsAdapter

        // Add scroll listener for endless scrolling
        binding.moreProductsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isFetchingMore && lastVisibleItemPosition == totalItemCount - 1) {
                    // Reached the end of the list, fetch more products
                    fetchMoreProducts()
                }
            }
        })



        Firebase.firestore.collection("Products").limit(20).get().addOnSuccessListener { documents ->
            productList.clear()
            for (i in documents) {
                val tempProductModel = i.toObject<ProductModel>()
                tempProductModel.id = i.id
                productList.add(tempProductModel)
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }




        binding.dress.setOnClickListener {
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category", "Dresses"))
        }
        binding.jumpsuits.setOnClickListener {
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category", "JumpSuits"))
        }
        binding.tops.setOnClickListener { startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category", "Tops"))
        }
        binding.bottoms.setOnClickListener{
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","Bottoms"))
        }
        binding.saree.setOnClickListener{
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","Saree"))
        }
        binding.seeMore.setOnClickListener {
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
        }
        binding.seeMore2.setOnClickListener {
            startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
        }


//      Image slider ->  imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.sale1))
        imageList.add(SlideModel(R.drawable.sale2))
        imageList.add(SlideModel(R.drawable.sale1))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)



    }

    private fun fetchMoreProducts() {
        isFetchingMore = true
        val productAdapter = ProductAdapter(requireContext(), moreProductsList)
        binding.moreProductsRV.adapter = productAdapter

        Firebase.firestore.collection("Products").limit(20).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject<ProductModel>()
                    product.id = document.id
                    moreProductsList.add(product)
                }
                productAdapter.notifyDataSetChanged()
                isFetchingMore = false
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error getting documents: ${exception.message}", Toast.LENGTH_SHORT).show()
                isFetchingMore = false
            }
    }


}