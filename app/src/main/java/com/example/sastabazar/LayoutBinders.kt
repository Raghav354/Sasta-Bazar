//package com.example.sastabazar
//
//import androidx.recyclerview.widget.RecyclerView
//import coil.load
//import com.example.sastabazar.adapters.GenericRecyclerViewAdapter
//import com.example.sastabazar.databinding.RvItemBinding
//import com.example.sastabazar.model.ProductModel
//
//object LayoutBinders  {
//
//class RvItemLayoutBinder:GenericRecyclerViewAdapter.ItemBinder<ProductModel , RvItemBinding> {
//    override fun bindItem(binding: RvItemBinding, product: ProductModel) {
//        binding.apply {
//            product.productImage?.let { productImage.load(product.productImage) }
//            dressname.text = product.productName
//            productCode.text = product.productCouponCode
//            originalprice.text = "${product.productOriginalPrice} Rs"
//            discountpercent.text = "${product.productDiscountPercentage}  %Off"
//            discountprice.text   = "Rs ${product.productDiscountPercentage}"
//
//
//        }
//    }
//}
//
//}