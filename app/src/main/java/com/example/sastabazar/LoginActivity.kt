package com.example.sastabazar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            } else {
                Toast.makeText(this , "Empty fields are not allowed!!" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}