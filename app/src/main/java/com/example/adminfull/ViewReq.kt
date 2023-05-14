package com.example.adminfull

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfull.databinding.ActivityViewReqBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ViewReq : AppCompatActivity() {
    var nodeId=""
    val calander=Calendar.getInstance()
    val dateFormat=SimpleDateFormat("yyyy-MM-dd hh:mm a")

    private lateinit var db: DatabaseReference//pointing out the location in the database

    private lateinit var binding: ActivityViewReqBinding // holds all the bindings from the layout properties to the layout's views and knows how to assign values for the binding expressions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewReqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var dt=dateFormat.format(calander.time)
        binding.etDt.setText(dt).toString()
    }
    fun insert_data(view: View){
        val stream=binding.etStreamname.text.toString()
        val subject=binding.etSubjectname.text.toString()
        val dt=datetoMiliseconds(binding.etDt.text.toString(),dateFormat)

        db= FirebaseDatabase.getInstance().getReference("request")
        val item=Request(stream,subject,dt)
        val databaseReference= FirebaseDatabase.getInstance().reference
        val id=databaseReference.push().key
        db.child(id.toString()).setValue(item).addOnSuccessListener {
            binding.etStreamname.text.clear()
            binding.etSubjectname.text.clear()


            Toast.makeText(this,"Data Inserted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Data Not Inserted", Toast.LENGTH_SHORT).show()

        }


    }//function for inserting admin image







    private val ActivityResultLauncher=registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode== RESULT_OK){
            val uri=result.data!!.data


        }

    }
    private val itemResultLauncher=registerForActivityResult<Intent, ActivityResult>(//getting results from AdminList
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode== 2){
            val intent=result.data
            if(intent!=null){
                nodeId=intent.getStringExtra("requestID").toString()
            }
            db= FirebaseDatabase.getInstance().getReference("items")
            db.child(nodeId).get().addOnSuccessListener {
                if (it.exists()){
                    binding.etStreamname.setText(it.child("stream2").value.toString())
                    binding.etSubjectname.setText(it.child("subject2").value.toString())



                }else{
                    Toast.makeText(this,"Request not found", Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener {
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()

            }

        }

    }//showing the results
    fun showList(view: View){
        var i: Intent
        i= Intent(this,RequestList::class.java)
        itemResultLauncher.launch(i)
    }

    private fun datetoMiliseconds(date:String,dateFormat: SimpleDateFormat):Long{
        val mDate=dateFormat.parse(date)
        return mDate.time

    }
    private fun milisecondstoDate(milliseconds:String,dateFormat: SimpleDateFormat):String{
        val millis:Long=milliseconds.toLong()
        return dateFormat.format(millis)

    }



}