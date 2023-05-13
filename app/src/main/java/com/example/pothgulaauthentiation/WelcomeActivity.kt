package com.example.pothgulaauthentiation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pothgulaauthentiation.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //connect with Main Activity
        val actionBar = supportActionBar
        actionBar!!.title = "Welcome Activity"

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}