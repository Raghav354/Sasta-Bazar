package com.example.sastabazar.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sastabazar.activities.HomeActivity
import com.example.sastabazar.adaptors.WishListAdapter
import com.example.sastabazar.databinding.FragmentWishListBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class WishListFragment : Fragment() {

    private lateinit var binding:FragmentWishListBinding
    private lateinit var wishlistitemList:ArrayList<ProductModel>
    private lateinit var wishListAdapter:WishListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(layoutInflater)
        wishlistitemList = ArrayList()
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishListAdapter = WishListAdapter(requireContext() , wishlistitemList)
        binding.rvWishList.adapter = wishListAdapter
        binding.rvWishList.layoutManager = LinearLayoutManager(requireContext())

        //Reading wishlist data from Firebase
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        firebaseFirestore.collection("users")
            .document(userId)
            .collection("wishlist")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject<ProductModel>()
                    product.id = document.id
                    wishlistitemList.add(product)
                }
                wishListAdapter.notifyDataSetChanged() // Update UI with wishlist items
            }
            .addOnFailureListener { e ->
                Log.w("CartFragment", "Error reading cart items: ${e.message}")
                Toast.makeText(requireContext(), "Error loading cart!", Toast.LENGTH_SHORT).show()
            }

        binding.toolbar.setOnClickListener {
            startActivity(Intent(requireContext() , HomeActivity::class.java))
        }
    }



}