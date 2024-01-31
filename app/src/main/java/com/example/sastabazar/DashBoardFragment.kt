package com.example.sastabazar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.models.SlideModel
import com.example.sastabazar.activities.ProjectCatActivity
import com.example.sastabazar.adaptors.ProductAdapter
import com.example.sastabazar.databinding.FragmentDashBoardBinding
import com.example.sastabazar.model.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class DashBoardFragment : Fragment() {

    private lateinit var binding : FragmentDashBoardBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit  var adapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater,container,false)
        settingImageSlider()
        handleButtonClick()
//        settingDummyDataForFlashSale()
//        setRecycleView()
        onFlashSaleItemClickListner()




        productList = ArrayList()
        adapter = ProductAdapter(requireContext(),productList)
        binding.mainRV.adapter = adapter
//        getProductData()
        Firebase.firestore.collection("Products").limit(10).get().addOnSuccessListener {
            productList.clear()
            for(i in it.documents)
            {
                var tempProductModel = i.toObject<ProductModel>()
                tempProductModel?.productUUID = i.id
                productList.add(tempProductModel!!)
            }
            adapter.notifyDataSetChanged()
        }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

    private fun onFlashSaleItemClickListner() {

    }

//     fun onItemClick(productModel: ProductModel) {
//        // Handle item click here, for example, start DetailsActivity
//        val intent = Intent(requireContext(), BuyDressActivity::class.java)
//        intent.putExtra("PRODUCT_ID", productModel.productUUID) // Pass product ID to DetailsActivity
//        startActivity(intent)
//    }

//    private fun setRecycleView() {
//        var rv =binding.mainRV
//        rv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
//        adapter = GenericRecyclerViewAdapter(
//            {inflater , parent ,attatchToParent ->
//                RvItemBinding.inflate(inflater,parent,attatchToParent)
//            },
//
//            LayoutBinders.RvItemLayoutBinder(),
//            productList
//        )
//        rv.adapter = adapter
//    }

//    private fun settingDummyDataForFlashSale() {
//        productList = ArrayList()
//        productList.add(ProductModel(null,"One Shoulder Linen Dress",499.0,1299.0,20.3,"Gf0123","Uk12","pink",null,"https://i.imgur.com/tGbaZCY.jpg","1" , "400",true))
//        productList.add(ProductModel(null,"One Shoulder Linen Dress",499.0,1299.0,20.3,"Gf0123","Uk12","pink",null,"https://i.imgur.com/tGbaZCY.jpg","1" , "400",true))
//        productList.add(ProductModel(null,"One Shoulder Linen Dress",499.0,1299.0,20.3,"Gf0123","Uk12","pink",null,"https://i.imgur.com/tGbaZCY.jpg","1" , "400",true))
//        productList.add(ProductModel(null,"One Shoulder Linen Dress",499.0,1299.0,20.3,"Gf0123","Uk12","pink",null,"https://i.imgur.com/tGbaZCY.jpg","1" , "400",true))
//
//    }

    private fun handleButtonClick() {
        binding.apply {
            dress.setOnClickListener{
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","Dresses"))
            }
            jumpsuits.setOnClickListener{
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","JumpSuits"))
            }
            tops.setOnClickListener{
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","Tops"))
            }
            bottoms.setOnClickListener {
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java).putExtra("Category","Bottoms"))
            }
            seeMore.setOnClickListener {
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
            }
            seeMore2.setOnClickListener {
                startActivity(Intent(requireContext(), ProjectCatActivity::class.java))
            }

        }
    }

    private fun settingImageSlider() {
//      Image slider ->  imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.sale1))
        imageList.add(SlideModel(R.drawable.sale2))
        imageList.add(SlideModel(R.drawable.sale1))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
    }

}