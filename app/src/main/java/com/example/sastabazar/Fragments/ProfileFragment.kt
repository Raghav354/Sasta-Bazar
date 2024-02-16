package com.example.sastabazar.Fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sastabazar.R
import com.example.sastabazar.activities.LoginActivity
import com.example.sastabazar.databinding.FragmentProfileBinding
import com.example.sastabazar.databinding.LogoutdialogboxBinding
import com.example.sastabazar.model.DetailsModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileFragment : Fragment() {

    private var profileImageUri: Uri? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var graybackground: View
    private var isEnabled: Boolean = false
    private lateinit var btmnavview: BottomNavigationView
    private val detailsModel = DetailsModel()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        graybackground = inflater.inflate(R.layout.greybackground,container,false)
        setUpUi()
        buttonClickHandler()
        fetchUserDetails()

        return binding.root
    }

    private fun buttonClickHandler() {
        binding.apply {
            logOut.setOnClickListener {
                showLogDialogBox()
            }

            editProfile.setOnClickListener {
                makeEditable()
            }
            saveProfile.setOnClickListener {
                saveUserDetails()
                makeUnEditable()
            }

            profilepic.setOnClickListener {
                if (profilepic.isEnabled) {
                    setProfileImage()
                }
            }

        }
    }

    private fun saveProfileImage(uri: Uri, userId: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImageRef = storageRef.child("profile_images").child(userId)

        profileImageRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL of the uploaded image
                profileImageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Once image is uploaded, update the user details with the image URL
                    val imageUrlString = imageUrl.toString()
                    uploadProfileImage(userId, imageUrlString)
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Error getting image URL: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error uploading image: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadProfileImage(userId: String, imageUrl: String) {
        val userDetailsRef =
            db.collection("users").document(userId).collection("userDetails").document(userId)

        userDetailsRef.set(mapOf("profileImageUrl" to imageUrl), SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Profile image uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error uploading profile image: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun setProfileImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeprofileimg.launch(intent)
    }

    private val changeprofileimg =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imageUri = data?.data
                // 2. Real-time Updates:
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                saveProfileImage(imageUri!!, userId!!)
            }
        }

    private fun saveUserDetails() {
        val firebaseFirestore = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        setDetails()

        firebaseFirestore.collection("users")
            .document(userId)
            .collection("userDetails")
            .document(userId)
            .set(
                hashMapOf(
                    "firstname" to detailsModel.firstname,
                    "lastname" to detailsModel.lastname,
                    "phoneNumber" to detailsModel.phoneNumber,
                    "email" to detailsModel.email,
                    "address" to detailsModel.address,
                    "profileImageUrl" to detailsModel.profileImageUrl
                ), SetOptions.merge()
            )
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "User details saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error adding item: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setDetails() {
        detailsModel.firstname = binding.firstName.text.toString()
        detailsModel.lastname = binding.lastName.text.toString()
        detailsModel.phoneNumber = binding.userPhoneNumber.text.toString()
        detailsModel.email = binding.userEmail.text.toString()
        detailsModel.address = binding.userAddress.text.toString()
    }

    private fun fetchUserDetails() {
        userId?.let { uid ->
            db.collection("users")
                .document(uid)
                .collection("userDetails")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userDetails = document.toObject(DetailsModel::class.java)
                        userDetails?.let { details ->
                            // Populate UI with user details
                            binding.apply {
                                firstName.setText(details.firstname)
                                lastName.setText(details.lastname)
                                userEmail.setText(details.email)
                                userPhoneNumber.setText(details.phoneNumber)
                                userAddress.setText(details.address)
                            }

                            // Load profile image using Glide
                            if (!details.profileImageUrl.isNullOrEmpty()) {
                                Glide.with(requireContext())
                                    .load(details.profileImageUrl)
                                    .into(binding.profilepic)
                            }


                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "User details object is null",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "User details not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error fetching user details: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    private fun setUpUi() {
        graybackground.visibility = View.INVISIBLE
        isEnabled = false
        makeUnEditable()
        btmnavview = activity?.findViewById(R.id.nav_view)!!
        val parentLayout = binding.root
        parentLayout.addView(graybackground)
    }

    private fun showLogDialogBox() {
        makeViewInvisible()
        val logoutdialog = Dialog(requireContext())
        val bind: LogoutdialogboxBinding = LogoutdialogboxBinding.inflate(layoutInflater)
        logoutdialog.setContentView(bind.root)

        val dialoglogoutbtn = bind.logout
        val dialogcancelbtn = bind.cancelbtn
        val dialogprofilepic = bind.profilepic
        if (profileImageUri!=null)
            dialogprofilepic.setImageURI(profileImageUri)


        dialoglogoutbtn.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            GoogleSignIn.getClient(requireContext(), gso).signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        dialogcancelbtn.setOnClickListener {
            makeViewVisible()
            logoutdialog.dismiss()
        }
        logoutdialog.show()
    }

    private fun makeViewVisible()
    {
        graybackground.visibility = View.INVISIBLE
        binding.logOut.visibility = View.VISIBLE
        binding.editProfile.visibility = View.VISIBLE
        btmnavview.visibility = View.VISIBLE
        binding.profilepic.visibility = View.VISIBLE
    }

    private fun makeViewInvisible() {
        binding.logOut.visibility = View.INVISIBLE
        binding.editProfile.visibility = View.INVISIBLE
        btmnavview.visibility = View.INVISIBLE
        graybackground.visibility = View.VISIBLE
        binding.profilepic.visibility=View.INVISIBLE
    }

    private fun makeUnEditable() {
        binding.apply {
            firstName.isEnabled = false
            lastName.isEnabled = false
            userEmail.isEnabled = false
            userPhoneNumber.isEnabled=false
            userAddress.isEnabled = false
            profilepic.isEnabled = false
        }

    }

    private fun makeEditable()
    {
        isEnabled = ! isEnabled
        binding.apply {
            firstName.isEnabled = isEnabled
            lastName.isEnabled = isEnabled
            userEmail.isEnabled = isEnabled
            userPhoneNumber.isEnabled = isEnabled
            userAddress.isEnabled = isEnabled
            profilepic.isEnabled =isEnabled
        }
        if (isEnabled)
        {
            binding.firstName.requestFocus()
        }
    }


}