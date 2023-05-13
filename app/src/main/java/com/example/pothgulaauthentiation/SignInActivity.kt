package com.example.pothgulaauthentiation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.pothgulaauthentiation.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set view binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //use to remove / hide action bar
        supportActionBar?.hide()

        auth = Firebase.auth

        binding.btnSignIn.setOnClickListener {
            //if true
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (checkAllField()){
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //if successful already sign in
                        Toast.makeText(this, "Successfully sign in", Toast.LENGTH_SHORT).show()
                        //go to Home activity
                        //use to start an activity
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        //use to destroy activity
                        finish()
                    } else {
                        //not sign in
                        Log.e("error:", it.exception.toString())
                    }

                }
            }
        }
        binding.tvHaventAccount.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.tvForgotPw.setOnClickListener{
            val intent = Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun checkAllField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        //also note password should be at least 6 characters
        if (binding.etPassword.text.toString() == "") {
            binding.textInputLayoutPassword.error = "This is required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Password should at least 8 characters long"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }
}