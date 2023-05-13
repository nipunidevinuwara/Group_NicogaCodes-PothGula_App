package com.example.pothgulaauthentiation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.pothgulaauthentiation.databinding.ActivityUpdatePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set view binding

        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnUpdatePassword.setOnClickListener{
            val user = auth.currentUser
            val password = binding.etPassword.text.toString()
            if (checkPasswordField()){
                user?.updatePassword(password)?.addOnCompleteListener{
                    //if successful updated successfully
                    if (it.isSuccessful){
                        Toast.makeText(this, "Password Updated Successfully" , Toast.LENGTH_SHORT).show()
                    }
                    else{
                        //catch error
                        Log.e("error:", it.exception.toString())
                    }
                }
            }
        }

        binding.btnUpdateEmail.setOnClickListener{
            val user = auth.currentUser
            val email = binding.etEmail.text.toString()
            if (checkEmailField()){
                user?.updateEmail(email)?.addOnCompleteListener {
                    //if successful updated successfully
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Email Updated Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        //catch error
                        Log.e("error:", it.exception.toString())
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val user = auth.currentUser
            user?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    //account already deleted
                    Toast.makeText(this, "Account Successfully deleted", Toast.LENGTH_SHORT).show()
                    //since account is already been deleted we cannot sign out
                    //so we just start an activity
                    val intent = Intent(this,SignInActivity::class.java)
                    startActivity(intent)
                    //destroy this activity
                    finish()
                } else {
                    //catch error
                    Log.e("error:", it.exception.toString())
                }
            }
        }
    }

    private fun checkPasswordField(): Boolean {
        //also note password should be at least 6 characters
        if (binding.etPassword.text.toString() == "") {
            binding.textInputLayoutPassword.error = "This is required field"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        if (binding.etPassword.length() <= 6) {
            binding.textInputLayoutPassword.error = "Password should at least 7 characters long"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }

    private fun checkEmailField(): Boolean {
        val email = binding.etEmail.text.toString()
        if (binding.etEmail.text.toString() == "") {
            binding.textInputLayoutEmail.error = "This is required field"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        return true
    }
}