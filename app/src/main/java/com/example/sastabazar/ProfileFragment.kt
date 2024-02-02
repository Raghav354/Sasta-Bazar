package com.example.sastabazar

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.sastabazar.activities.LoginActivity
import com.example.sastabazar.databinding.FragmentProfileBinding
import com.example.sastabazar.databinding.LogoutdialogboxBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ProfileFragment : Fragment() {

    private var profileImageUri: Uri?=null
    private lateinit var binding:FragmentProfileBinding
    private lateinit var graybackground:View
    private var isEnabled:Boolean = false
    private lateinit var btmnavview: BottomNavigationView


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

        return binding.root
    }

    private fun buttonClickHandler() {
        binding.apply {
            logOut.setOnClickListener{
                showLogDialogBox()
            }

            editProfile.setOnClickListener {
                makeEditable()
                if(isEnabled)
                {
                    editProfile.text = "Save Profile"
                }
                else
                {
                    editProfile.text = "Edit Profile"
                }
            }

            profilepic.setOnClickListener {
                if(profilepic.isEnabled)
                {
                    setProfileImage()
                }
            }

        }
    }

    private fun setProfileImage() {
        val pickProfileimg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeprofileimg.launch(pickProfileimg)
    }
    private val changeprofileimg =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                binding.profilepic.setImageURI(imgUri)
                profileImageUri = imgUri
            }
        }

    private fun setUpUi() {
        graybackground.visibility = View.INVISIBLE
        isEnabled = false
        makeUnEditable()
        btmnavview = activity?.findViewById(R.id.nav_view)!!
        val parentLayout = binding.root as ConstraintLayout
        parentLayout.addView(graybackground)
    }

    private fun showLogDialogBox() {
        makeViewInvisible()
        val logoutdialog = Dialog(requireContext())
        val bind: LogoutdialogboxBinding = LogoutdialogboxBinding.inflate(layoutInflater)
        logoutdialog.setContentView(bind.root)

        val dialoglogoutvbtn = bind.logout
        val dialogcancelbtn = bind.cancelbtn
        val dialogprofilepic = bind.profilepic
        if (profileImageUri!=null)
            dialogprofilepic.setImageURI(profileImageUri)


        dialoglogoutvbtn.setOnClickListener {
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