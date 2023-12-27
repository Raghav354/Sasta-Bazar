package com.example.sastabazar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sastabazar.databinding.ActivitySignUpLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpLoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySignUpLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {

            //To create the new User
            Firebase.auth.createUserWithEmailAndPassword(
                binding.email.text.toString(),
                binding.pass.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {

                    //User model to store the value of user

                    var userModel = UserModel(
                        binding.firstName.text.toString(),
                        binding.lastName.text.toString(),
                        binding.email.text.toString(),
                        binding.pass.text.toString()
                    )

                    //To update user data on firebase

                    Firebase.database.reference.child("Users").child(Firebase.auth.currentUser?.uid!!)
                        .setValue(userModel).addOnSuccessListener {
                            //If user successfully created
                            startActivity(Intent(this@SignUpLoginActivity , HomeActivity::class.java))

                        }
                        .addOnFailureListener {
                            //If user creation failed.
                            Toast.makeText(this@SignUpLoginActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
                else {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }


    }
}