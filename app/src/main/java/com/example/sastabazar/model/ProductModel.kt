package com.example.sastabazar.model

import java.io.Serializable


class ProductModel : Serializable {
    var category: String?=null
    var productName: String = ""
    var productPrice: Double = 0.0
    var productCouponCode: String = ""
    var productSize: String = ""
    var productColor: String = ""
    var productDisp: String?=null
    var productImage: String = ""
    var productUUID: String = ""
    var productQuantity: String?=null
    var productTotalPrice: String?=null
    var productIsFavourite: Boolean? = false
    var productIsFlashSale : Boolean? = false
    var productOriginalPrice : Double?=null
    var productDiscountPercentage : Double?=null
    constructor()

    constructor(
        category: String?,
        productName: String,
        productPrice: Double,
        productCouponCode: String,
        productSize: String,
        productColor: String,
        productDisp: String?,
        productImage: String
    ) {
        this.category = category
        this.productName = productName
        this.productPrice = productPrice
        this.productCouponCode = productCouponCode
        this.productSize = productSize
        this.productColor = productColor
        this.productDisp = productDisp
        this.productImage = productImage
    }

    constructor(
        category: String?,
        productName: String,
        productPrice: Double,
        productCouponCode: String,
        productSize: String,
        productColor: String,
        productDisp: String?,
        productImage: String,
        productUUID: String
    ) {
        this.category = category
        this.productName = productName
        this.productPrice = productPrice
        this.productCouponCode = productCouponCode
        this.productSize = productSize
        this.productColor = productColor
        this.productDisp = productDisp
        this.productImage = productImage
        this.productUUID = productUUID
    }

    constructor(
        category: String?,
        productName: String,
        productPrice: Double,
        productCouponCode: String,
        productSize: String,
        productColor: String,
        productDisp: String?,
        productImage: String,
        productQuantity: String?,
        productTotalPrice: String?,
        productIsFavourite : Boolean?
    ) {
        this.category = category
        this.productName = productName
        this.productPrice = productPrice
        this.productCouponCode = productCouponCode
        this.productSize = productSize
        this.productColor = productColor
        this.productDisp = productDisp
        this.productImage = productImage
        this.productQuantity = productQuantity
        this.productTotalPrice = productTotalPrice
        this.productIsFavourite = productIsFavourite
    }

    constructor(
        category: String?,
        productName: String,
        productPrice: Double,
        productOriginalPrice : Double?,
        productDiscountPercentage : Double?,
        productCouponCode: String,
        productSize: String,
        productColor: String,
        productDisp: String?,
        productImage: String,
        productQuantity: String?,
        productTotalPrice: String?,
        productIsFlashSale : Boolean?
    ) {
        this.category = category
        this.productName = productName
        this.productPrice = productPrice
        this.productOriginalPrice = productOriginalPrice
        this.productDiscountPercentage =productDiscountPercentage
        this.productCouponCode = productCouponCode
        this.productSize = productSize
        this.productColor = productColor
        this.productDisp = productDisp
        this.productImage = productImage
        this.productQuantity = productQuantity
        this.productTotalPrice = productTotalPrice
        this.productIsFlashSale = productIsFlashSale
    }

}


