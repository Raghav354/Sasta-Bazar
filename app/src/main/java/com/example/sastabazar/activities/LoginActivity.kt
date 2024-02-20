package com.example.sastabazar.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sastabazar.R
import com.example.sastabazar.databinding.ActivityLoginBinding
import com.example.sastabazar.databinding.LoginsuccessdialogboxBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textWatcherFun()
        handleBtnClick()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this, gso)

    }

    private fun handleBtnClick() {
        binding.apply {

            login.setOnClickListener { userLogin() }
            signUpText.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                finish()
            }
            forgotPass.setOnClickListener { handleForgotPassword(binding.email.text.toString()) }
            google.setOnClickListener {
                val signInClient = googleSignClient.signInIntent
                launcher.launch(signInClient)
            }
        }
    }


    //launcher to show all the google account in your phone .
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showLoginSuccesDialog()
//                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//                            finish()

                        } else {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()

            }
        }

    private fun handleForgotPassword(email: String) {
        if (binding.email.text.isNotEmpty()) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Password reset email sent to $email.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Error sending password reset email: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please provide the mail...", Toast.LENGTH_SHORT).show()
        }

    }


    private fun userLogin() {
        val email = binding.email.text.toString()
        val pass = binding.password.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            Firebase.auth.signInWithEmailAndPassword(
                email, pass
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val verification = Firebase.auth.currentUser?.isEmailVerified
                    if (verification == true) {
                        Toast.makeText(this@LoginActivity, "login", Toast.LENGTH_SHORT)
                            .show()
                        showLoginSuccesDialog()
                    } else {
                        Toast.makeText(
                            this,
                            "Please verify your email by registering!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun textWatcherFun() {
        //textWatcher on login button
        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    //only enables the button when all the required field are filled.
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.login.isEnabled = binding.email.text.toString().trim().isNotEmpty()
                    && binding.password.text.toString().trim().isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {

        }


    }
    private fun showLoginSuccesDialog() {

        val loginsuccessdialog= Dialog(this@LoginActivity)
        val bind: LoginsuccessdialogboxBinding =  LoginsuccessdialogboxBinding.inflate(layoutInflater)
        loginsuccessdialog.setContentView(bind.root)

        val donebtn = bind.donebtn

        bind.logincongrats.text="Congratulations, you have \ncompleted your Login !"

        donebtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
            Toast.makeText(this, "Logged in Successfully...", Toast.LENGTH_SHORT)
                                .show()
        }

        loginsuccessdialog.show()
    }
}