package com.example.sastabazar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.adaptors.ProductAdapter
import com.example.sastabazar.databinding.FragmentCartBinding
import com.example.sastabazar.model.ProductModel


class CartFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    private lateinit var binding:FragmentCartBinding
    private lateinit var cartItemList: ArrayList<ProductModel>
    private lateinit var productItemAdapter:ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    companion object {

    }
}