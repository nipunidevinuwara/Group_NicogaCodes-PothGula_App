package com.example.pothgulaauthentiation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pothgulaauthentiation.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignOut.setOnClickListener {
            //use to sign out account
            auth.signOut()
            //start another activity (signIn/SignUp)
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)

            //use to destroy current activity
            finish()
        }

        binding.btnUpdate.setOnClickListener{
            val intent = Intent(this,UpdatePasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}