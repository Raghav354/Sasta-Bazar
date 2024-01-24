package com.example.sastabazar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.sastabazar.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //If new user is here
        binding.signUpText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpLoginActivity::class.java))
        }

        //User is already exist
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                Firebase.auth.signInWithEmailAndPassword(
                    email, pass
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this , "Empty fields are not allowed!!" , Toast.LENGTH_SHORT).show()
            }
        }

        binding.facebook.setOnClickListener{
            //sign in by facebook account
        }
        binding.google.setOnClickListener{
            //sign in by google account

        }

        //textWatcher on login button
        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    //only enables the button when all the required field are filled.
    private val textWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.login.isEnabled = binding.email.text.toString().trim().isNotEmpty()
                    && binding.password.text.toString().trim().isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {

        }


    }

    override fun onStart() {
        super.onStart()

    }
}