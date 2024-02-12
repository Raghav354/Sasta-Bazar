package com.example.sastabazar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.adaptors.ProductAdapter
import com.example.sastabazar.databinding.FragmentCartBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private lateinit var cartItemList: ArrayList<ProductModel>
    private lateinit var cartAdapter:CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentCartBinding.inflate(layoutInflater,container,false)
        cartItemList=ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        cartAdapter = CartAdapter(requireContext(),cartItemList)
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())

        // Read cart data from Firebase
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseFirestore.collection("users")
            .document(userId)
            .collection("cart")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject<ProductModel>()!!
                    product.id = document.id
                    cartItemList.add(product)
                }
                cartAdapter.notifyDataSetChanged() // Update UI with cart items
            }
            .addOnFailureListener { e ->
                Log.w("CartFragment", "Error reading cart items: ${e.message}")
                Toast.makeText(requireContext(), "Error loading cart!", Toast.LENGTH_SHORT).show()
            }
    }








//    private fun setRecyclerView() {
//
//        arguments?.let { bundle ->
//            // Access data from the bundle and update cartItemList
//            val dressName = bundle.getString("DressName")
//            val dressPrize = bundle.getDouble("DiscountPrize", 0.0)
//            val dressImage = bundle.getString("DressImage")
//            val dressQuantity = bundle.getInt("DressQuantity", 1) // Use default if not present
//            val dressSize = bundle.getString("DressSize")
//
//            if (dressName != null && dressPrize > 0 && dressImage != null) {
//                cartItemList.add(ProductModel(
//                    null, dressName, dressPrize, dressSize, null, dressImage, null
//                ))
//            } else {
//                 Toast.makeText(context, "Missing data from BuyDressActivity", Toast.LENGTH_SHORT).show()
//            }
//
//
//        cartItemList.add(ProductModel(null , dressName , dressPrize?.toDouble() , null , null ,dressImage , "AVG45"))
//        productItemAdapter = CartAdapter(requireContext() , cartItemList)
//        recyclerView = binding.rvCart
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = productItemAdapter
//        }
//    }


}