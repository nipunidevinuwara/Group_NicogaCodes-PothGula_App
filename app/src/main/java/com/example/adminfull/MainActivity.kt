package com.example.adminfull

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAddAdmin.setOnClickListener(View.OnClickListener{startActivity(Intent(this,AddAdmin::class.java))})
        buttonAddPDF.setOnClickListener(View.OnClickListener{startActivity(Intent(this,AddPP::class.java))})
        buttonAddBooks.setOnClickListener(View.OnClickListener{startActivity(Intent(this,AddBooks::class.java))})
        buttonViewRequests.setOnClickListener(View.OnClickListener{startActivity(Intent(this,ViewReq::class.java))})
        buttonViewDonations.setOnClickListener(View.OnClickListener{startActivity(Intent(this,ViewDonations::class.java))})




    }
}