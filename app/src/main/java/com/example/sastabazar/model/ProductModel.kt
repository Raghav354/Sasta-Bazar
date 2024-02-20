package com.example.sastabazar.model

import java.io.Serializable

data class ProductModel(
    var id: String? = null,
    var name: String? = null,
    var discountPrice: Double? = null,
    var originalPrice: Double? = null,
    var disp: String? = null,
    var productColor: String? = null,
    var imageUrl: String? = null,
    var productCoupanCode: String? = null,
    var discountPercentage: Double? = null,
    var productSize: String? = null
): Serializable

