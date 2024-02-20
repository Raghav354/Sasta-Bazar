package com.example.sastabazar.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sastabazar.R
import com.example.sastabazar.activities.ProjectCatActivity
import com.example.sastabazar.adaptors.ProductAdapter
import com.example.sastabazar.databinding.FragmentDashBoardBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var adapter: ProductAdapter
    val moreProductsList = ArrayList<ProductModel>()


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

        Firebase.firestore.collection("Products").limit(10).get().addOnSuccessListener {
            productList.clear()
            for(i in it.documents)
            {
                var tempProductModel = i.toObject<ProductModel>()
                tempProductModel?.id = i.id
                productList.add(tempProductModel!!)
            }
            adapter.notifyDataSetChanged()
        }

            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        getMoreProducts()

        binding.dress.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ProjectCatActivity::class.java
                ).putExtra("Category", "Dresses")
            )
        }
        binding.jumpsuits.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ProjectCatActivity::class.java
                ).putExtra("Category", "JumpSuits")
            )
        }
        binding.tops.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ProjectCatActivity::class.java
                ).putExtra("Category", "Tops")
            )
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

    private fun getMoreProducts() {

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
            }.addOnFailureListener { exception ->
            Toast.makeText(
                requireContext(),
                "Error getting documents: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    companion object {

    }
}