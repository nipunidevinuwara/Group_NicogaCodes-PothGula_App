package com.example.adminfull

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminfull.databinding.ActivityAddBooksBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class AddBooks : AppCompatActivity() {
    var sImage:String?=""
    var nodeId=""

    private lateinit var db: DatabaseReference//pointing out the location in the database

    private lateinit var binding: ActivityAddBooksBinding // holds all the bindings from the layout properties to the layout's views and knows how to assign values for the binding expressions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBooksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun insert_bookdata(view: View){
        val stream=binding.spStream.text.toString()
        val subject=binding.spSubject.text.toString()
        val year=binding.bookDate.text.toString()
        db= FirebaseDatabase.getInstance().getReference("books")
        val item=Books(stream,subject, year)
        val databaseReference= FirebaseDatabase.getInstance().reference
        val id=databaseReference.push().key
        db.child(id.toString()).setValue(item).addOnSuccessListener {
            binding.spStream.text.clear()
            binding.spSubject.text.clear()
            binding.bookDate.text.clear()

            Toast.makeText(this,"Data Inserted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Data Not Inserted", Toast.LENGTH_SHORT).show()

        }


    }//function for inserting admin image
    fun insert_Imgbook(view: View){
        var myfileintent= Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)

    }






    private val ActivityResultLauncher=registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode== RESULT_OK){
            val uri=result.data!!.data
            try{
                val inputStream=contentResolver.openInputStream(uri!!)
                val myBitmap= BitmapFactory.decodeStream(inputStream)
                val stream= ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bytes=stream.toByteArray()
                sImage= Base64.encodeToString(bytes, Base64.DEFAULT)
                binding.imageView4.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this,"Image Selected", Toast.LENGTH_SHORT).show()


            }catch (ex:java.lang.Exception){
                Toast.makeText(this,ex.message.toString(), Toast.LENGTH_SHORT).show()

            }

        }

    }
    private val itemResultLauncher=registerForActivityResult<Intent, ActivityResult>(//getting results from AdminList
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode== 2){
            val intent=result.data
            if(intent!=null){
                nodeId=intent.getStringExtra("bookID").toString()
            }
            db= FirebaseDatabase.getInstance().getReference("books")
            db.child(nodeId).get().addOnSuccessListener {
                if (it.exists()){
                    binding.spStream.setText(it.child("stream").value.toString())
                    binding.spSubject.setText(it.child("subject").value.toString())
                    binding.bookDate.setText(it.child("year").value.toString())
                    sImage=it.child("BookImg").value.toString()
                    val bytes= Base64.decode(sImage, Base64.DEFAULT)
                    val bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                    binding.imageView4.setImageBitmap(bitmap)
                    binding.btnUpdatebook.visibility= View.VISIBLE
                    binding.btnDeletebook.visibility= View.VISIBLE
                    binding.btnEditbook.visibility= View.INVISIBLE



                }else{
                    Toast.makeText(this,"Admin not found", Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener {
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()

            }

        }

    }//showing the results
    fun showList(view: View){
        var i: Intent
        i= Intent(this,BookList::class.java)
        itemResultLauncher.launch(i)
    }
    fun update_data(view: View){//update admin data
        val fullName=binding.spStream.text.toString()
        val email=binding.spSubject.text.toString()
        val phone=binding.bookDate.text.toString()
        db= FirebaseDatabase.getInstance().getReference("books")
        val item=Admin(fullName,email,phone,sImage)

        db.child(nodeId).setValue(item).addOnSuccessListener {
            binding.spStream.text.clear()
            binding.spSubject.text.clear()
            binding.bookDate.text.clear()
            sImage=""
            binding.btnUpdatebook.visibility= View.INVISIBLE
            binding.btnDeletebook.visibility= View.INVISIBLE
            binding.btnEditbook.visibility= View.VISIBLE

            Toast.makeText(this,"Data Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Data Not Updated", Toast.LENGTH_SHORT).show()

        }

    }//delete admin data
    fun delete_data(view: View){
        db= FirebaseDatabase.getInstance().getReference("books")
        db.child(nodeId).removeValue()

    }

}