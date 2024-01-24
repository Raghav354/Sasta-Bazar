package com.example.sastabazar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sastabazar.activities.HomeActivity
import com.example.sastabazar.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if(FirebaseAuth.getInstance().currentUser == null)
            {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        },1000)

    }
}