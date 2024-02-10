package com.example.sastabazar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var productItemAdapter:CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentCartBinding.inflate(layoutInflater,container,false)
        cartItemList = ArrayList()
        cartItemList.add(ProductModel(null, "One Shoulder \n Linen Dress",299.00,
            "Size : UK10","pink","https://i.imgur.com/tGbaZCY.jpg",null ))

        setRecyclerView()

        return binding.root
    }

    private fun setRecyclerView() {
//        var rv = binding.rvCart
//        rv.layoutManager = LinearLayoutManager(context)
//        rv.adapter = productItemAdapter

    }

    companion object {

    }
}