package com.example.sastabazar.model

data class ShippingInfo(
    val userEmail: String,
    val state: String,
    val firstName: String,
    val lastName: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val contactNumber: String,
    val saveInfoChecked: Boolean,
    val shippingMethod: String
)