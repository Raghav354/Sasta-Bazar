package com.example.sastabazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sastabazar.R
import com.example.sastabazar.adaptors.CartAdapter
import com.example.sastabazar.databinding.ActivityShippingBinding
import com.example.sastabazar.model.ProductModel
import com.example.sastabazar.model.ShippingInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShippingActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityShippingBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter

    private lateinit var email: String
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var address: String
    private lateinit var city: String
    private lateinit var pinCode: String
    private lateinit var contact: String
    private lateinit var state: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        productList = ArrayList()


        handleBtnClick()
        productInformation()



//        val navController = findNavController(R.id.nav_host_fragment_content_shipping)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

    }



    private fun handleBtnClick() {
        binding.apply {
            checkBox.setOnClickListener {
                checkBox.isChecked
                saveShippinInformaiton() }
        }

    }

    private fun productInformation() {
        val intent = intent
        val dressName = intent.getStringExtra("DressName")
        val dressPrize = intent.getStringExtra("DiscountPrize")
        val imageUrl = intent.getStringExtra("ImageUrl")
        val dressQuantity = intent.getIntExtra("DressQuantity", 1)
        val totalPrice = intent.getDoubleExtra("TotalPrice", 0.0)
        val size = intent.getStringExtra("Size")
        val color = intent.getStringExtra("DressColor")

        if (size != null) {
            productList.add(
                ProductModel(
                    null,
                    dressName,
                    dressPrize?.toDouble(),
                    null,
                    null,
                    color,
                    imageUrl.toString(),
                    "AVG45",
                    discountPercentage = null,
                    size
                )
            )
        }
        adapter = CartAdapter(this, productList)
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //setting prize
        binding.subTotalTxt.text = dressPrize.toString()
        binding.totalPrizeTxt.text = totalPrice.toString()
        binding.quantity.text = dressQuantity.toString()

        binding.continueButton.setOnClickListener {
            email = binding.userEmail.text.toString().trim()
            state = binding.spinner.selectedItem.toString().trim()
            firstName = binding.firstName.text.toString().trim()
            lastName = binding.lastName.text.toString().trim()
            address = binding.address.text.toString().trim()
            city = binding.city.text.toString().trim()
            contact = binding.contactNumber.text.toString().trim()
            pinCode = binding.postalCode.text.toString().trim()
            if (email.isNotEmpty() && firstName.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty() && pinCode.isNotEmpty() && contact.isNotEmpty()) {
                // All required fields are filled, proceed with saving shipping information and starting PaymentActivity

                val intentToPayment = Intent(this@ShippingActivity, PaymentActivity::class.java)
                intentToPayment.putExtra("DressName", dressName)
                intentToPayment.putExtra("DiscountPrize", dressPrize)
                intentToPayment.putExtra("ImageUrl", imageUrl)
                intentToPayment.putExtra("DressQuantity", dressQuantity)
                intentToPayment.putExtra("Size", size)
                intentToPayment.putExtra("Color", color)
                intentToPayment.putExtra("TotalPrice", totalPrice)

                intentToPayment.putExtra("State",state)
                intentToPayment.putExtra("Email",email)
                intentToPayment.putExtra("FirstName" , firstName)
                intentToPayment.putExtra("LastName" , lastName)
                intentToPayment.putExtra("Address" , address)
                intentToPayment.putExtra("Contact" , contact)
                intentToPayment.putExtra("City" , city)
                intentToPayment.putExtra("Pin",pinCode)
                startActivity(intentToPayment)
            } else {
                // Show a toast indicating that all fields are required
                Toast.makeText(this@ShippingActivity, "Please fill all the details.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun saveShippinInformaiton() {
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val userEmail = email
        val state = state // get state from spinner
        val firstName = firstName
        val lastName = lastName
        val address = address
        val city = city
        val postalCode = pinCode
        val contactNumber = contact
        val saveInfoChecked = binding.checkBox.isChecked
        val shippingMethod = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioButton1 -> "Standard free delivery over 4500"
            R.id.radioButton2 -> "Cash on delivery over Rs:4500 (Free Delivery, COD processing fee only)"
            else -> ""
        }

        // Create a ShippingInfo object with the retrieved details
        val shippingInfo = ShippingInfo(
            userEmail,
            state,
            firstName,
            lastName,
            address,
            city,
            postalCode,
            contactNumber,
            saveInfoChecked,
            shippingMethod
        )

        firebaseFirestore.collection("users").document(userId).collection("shippingInfo")
            .document(userId).set(shippingInfo).addOnSuccessListener {
                Toast.makeText(
                    this@ShippingActivity, "Shipping details saved successfully", Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this@ShippingActivity,
                    "Error saving shipping details: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_shipping)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}