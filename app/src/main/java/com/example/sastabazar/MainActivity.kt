package com.example.sastabazar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun GotoNext(view: View) {
        if(FirebaseAuth.getInstance().currentUser == null)
        {
            startActivity(Intent(this,SignUpLoginActivity::class.java))
            finish()
        }
        else
        {
            startActivity(Intent(this,HomeActivity ::class.java))
            finish()
        }


    }
}