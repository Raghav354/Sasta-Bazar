package com.example.sastabazar.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sastabazar.databinding.ActivitySignUpLoginBinding
import com.example.sastabazar.databinding.LoginsuccessdialogboxBinding
import com.example.sastabazar.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        textWatcherFun()
        btnHandle()

    }

    private fun textWatcherFun() {
        binding.firstName.addTextChangedListener(textWatcher)
        binding.lastName.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)
        binding.pass.addTextChangedListener(textWatcher)
        binding.comPass.addTextChangedListener(textWatcher)
    }

    private fun btnHandle() {
        binding.apply {
            switchText.setOnClickListener {
                startActivity(
                    Intent(
                        this@SignUpActivity,
                        LoginActivity::class.java
                    )
                )
            }
            signUp.setOnClickListener { createNewUser() }
        }

    }

    private fun createNewUser() {
        //To create the new User
        if (binding.pass.text.toString() != binding.comPass.text.toString()) {
            Toast.makeText(
                this@SignUpActivity,
                "Password and Confirm password must be same.",
                Toast.LENGTH_SHORT
            ).show()
        }

        Firebase.auth.createUserWithEmailAndPassword(
            binding.email.text.toString(),
            binding.pass.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {

                val user = Firebase.auth.currentUser
                sendVerificationEmail(user)
                saveData()
            } else {
                Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun sendVerificationEmail(user: FirebaseUser?) {
        if (user != null) {
            user.sendEmailVerification()
                .addOnSuccessListener {
                    Toast.makeText(this, "Please verify your email...", Toast.LENGTH_SHORT).show()
                    showRegistrationDialog()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@SignUpActivity,
                        it.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveData() {
        //User model to store the value of user
        val userModel = UserModel(
            binding.firstName.text.toString(),
            binding.lastName.text.toString(),
            binding.email.text.toString(),
            binding.pass.text.toString()
        )

        //To update user data on firebase
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser?.uid!!)
            .setValue(userModel).addOnSuccessListener {
                //If user successfully created
                Log.d("UserData", "User data saved successfully!")

            }
            .addOnFailureListener {
                //If user creation failed.
                Toast.makeText(this@SignUpActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun showRegistrationDialog() {

        val regsuccessdialog = Dialog(this@SignUpActivity)
        val bind: LoginsuccessdialogboxBinding =
            LoginsuccessdialogboxBinding.inflate(layoutInflater)
        regsuccessdialog.setContentView(bind.root)

        val donebtn = bind.donebtn

        bind.logincongrats.text = ""
        bind.success.text = "Verify your mail id to register... "


        donebtn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
        regsuccessdialog.show()
    }



    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.signUp.isEnabled = binding.firstName.text.toString().trim().isNotEmpty()
                    && binding.lastName.text.toString().trim().isNotEmpty()
                    && binding.pass.text.toString().trim().isNotEmpty()
                    && binding.comPass.text.toString().trim().isNotEmpty()
                    && binding.email.text.toString().trim().isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }


}