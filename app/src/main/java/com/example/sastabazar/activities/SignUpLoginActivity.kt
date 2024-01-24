package com.example.sastabazar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.sastabazar.model.UserModel
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

        binding.switchText.setOnClickListener {
            startActivity(Intent(this@SignUpLoginActivity , LoginActivity::class.java))
        }

        binding.signUp.setOnClickListener {

            //To create the new User

            if(binding.pass.text.toString() != binding.comPass.text.toString())
            {
                Toast.makeText(this@SignUpLoginActivity , "Password and Confirm password must be same." , Toast.LENGTH_SHORT).show()
            }
            else
            {
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
                                startActivity(Intent(this@SignUpLoginActivity , LoginActivity::class.java))
                                finish()

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

        binding.firstName.addTextChangedListener(textWatcher)
        binding.lastName.addTextChangedListener(textWatcher)
        binding.email.addTextChangedListener(textWatcher)
        binding.pass.addTextChangedListener(textWatcher)
        binding.comPass.addTextChangedListener(textWatcher)
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