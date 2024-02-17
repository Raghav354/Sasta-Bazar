package com.example.sastabazar.model

import java.io.Serializable

data class ProductModel(
    var id:String? = null,
    var name: String?=null,
    var price: Double?=null,
    var disp: String?=null,
    var color:String?=null,
    var imageUrl: String?=null,
    var productCode:String?=null,
    var size:String?=null
): Serializable

