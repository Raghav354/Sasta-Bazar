package com.example.sastabazar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.sastabazar.databinding.FragmentDashBoardBinding
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class DashBoardFragment : Fragment() {

    private lateinit var binding : FragmentDashBoardBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit  var adapter:ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater,container,false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList = ArrayList()
        adapter = ProductAdapter(requireContext(),productList)
        binding.mainRV.adapter = adapter
//        getProductData()
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

            .addOnFailureListener{
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        binding.dress.setOnClickListener{
            startActivity(Intent(requireContext(),ProjectCatActivity::class.java).putExtra("Category","Dresses"))
        }
        binding.jumpsuits.setOnClickListener{
            startActivity(Intent(requireContext(),ProjectCatActivity::class.java).putExtra("Category","JumpSuits"))
        }
        binding.tops.setOnClickListener{
            startActivity(Intent(requireContext(),ProjectCatActivity::class.java).putExtra("Category","Tops"))
        }
        binding.bottoms.setOnClickListener{
            startActivity(Intent(requireContext(),ProjectCatActivity::class.java).putExtra("Category","Bottoms"))
        }





    }


    //Getting data from firebase -> only 10 items
    private fun getProductData(){

    }


    companion object {

    }
}