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
import com.example.sastabazar.adaptors.CartAdapter
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
                    val product = document.toObject<ProductModel>()
                    product.id = document.id
                    cartItemList.add(product)
                }
                cartAdapter.notifyDataSetChanged() // Update UI with cart items
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