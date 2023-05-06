package com.example.admincontrol

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.admincontrol.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    //variables for uploaded image and node path

    var sImage:String?=""
    var nodeId=""


    private lateinit var db:DatabaseReference//pointing out the location in the database

    private lateinit var binding: ActivityMainBinding // holds all the bindings from the layout properties to the layout's views and knows how to assign values for the binding expressions

    lateinit var toggle:ActionBarDrawerToggle//layout for the side navbar
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        drawerLayout=findViewById(R.id.drawerLayout)
        val navView:NavigationView=findViewById(R.id.nav_view)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                //replacing the fragments when the navigation bar element is clicked

                R.id.nav_home-> replaceFragment(HomeFragment(),it.title.toString())
                R.id.nav_download->replaceFragment(DownloadFragment(),it.title.toString())
                R.id.nav_upload->replaceFragment(UploadFragment(),it.title.toString())


                R.id.nav_viewrequest->replaceFragment(ViewrequestsFragment(),it.title.toString())
                R.id.nav_donations->replaceFragment(ViewdonationsFragment(),it.title.toString())

            }
            true
        }


    }//function for replacing the fragments
    private fun replaceFragment(fragment: Fragment,title:String){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun insert_data(view: View){
        val fullName=binding.etPersonName.text.toString()
        val email=binding.etSampleAdminEmail.text.toString()
        val phone=binding.etPhone.text.toString()
        db=FirebaseDatabase.getInstance().getReference("items")
        val item=Admin(fullName,email,phone)
        val databaseReference=FirebaseDatabase.getInstance().reference
        val id=databaseReference.push().key
        db.child(id.toString()).setValue(item).addOnSuccessListener {
            binding.etPersonName.text.clear()
            binding.etSampleAdminEmail.text.clear()
            binding.etPhone.text.clear()

            Toast.makeText(this,"Data Inserted",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Data Not Inserted",Toast.LENGTH_SHORT).show()

        }


    }//function for inserting admin image
    fun insert_Img(view: View){
        var myfileintent=Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)

    }






    private val ActivityResultLauncher=registerForActivityResult<Intent,ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){result:ActivityResult->
        if(result.resultCode== RESULT_OK){
            val uri=result.data!!.data
            try{
                val inputStream=contentResolver.openInputStream(uri!!)
                val myBitmap=BitmapFactory.decodeStream(inputStream)
                val stream=ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val bytes=stream.toByteArray()
                sImage=Base64.encodeToString(bytes,Base64.DEFAULT)
                binding.imageView3.setImageBitmap(myBitmap)
                inputStream!!.close()
                Toast.makeText(this,"Image Selected",Toast.LENGTH_SHORT).show()


            }catch (ex:java.lang.Exception){
                Toast.makeText(this,ex.message.toString(),Toast.LENGTH_SHORT).show()

            }

        }

    }
    private val itemResultLauncher=registerForActivityResult<Intent,ActivityResult>(//getting results from AdminList
        ActivityResultContracts.StartActivityForResult()
    ){result:ActivityResult->
        if(result.resultCode== 2){
            val intent=result.data
            if(intent!=null){
                nodeId=intent.getStringExtra("adminID").toString()
            }
            db=FirebaseDatabase.getInstance().getReference("items")
            db.child(nodeId).get().addOnSuccessListener {
                if (it.exists()){
                    binding.etPersonName.setText(it.child("fullName").value.toString())
                    binding.etSampleAdminEmail.setText(it.child("email").value.toString())
                    binding.etPhone.setText(it.child("phone").value.toString())
                    sImage=it.child("adminImg").value.toString()
                    val bytes=Base64.decode(sImage,Base64.DEFAULT)
                    val bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                    binding.imageView3.setImageBitmap(bitmap)
                    binding.btnUpdate.visibility=View.VISIBLE
                    binding.btnDelete.visibility=View.VISIBLE
                    binding.btnEdit.visibility=View.INVISIBLE



                }else{
                    Toast.makeText(this,"Admin not found",Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener {
                Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()

            }

        }

    }//showing the results
    fun showList(view: View){
        var i:Intent
        i=Intent(this,AdminList::class.java)
        itemResultLauncher.launch(i)
    }
    fun update_data(view: View){//update admin data
        val fullName=binding.etPersonName.text.toString()
        val email=binding.etSampleAdminEmail.text.toString()
        val phone=binding.etPhone.text.toString()
        db=FirebaseDatabase.getInstance().getReference("items")
        val item=Admin(fullName,email,phone,sImage)

        db.child(nodeId).setValue(item).addOnSuccessListener {
            binding.etPersonName.text.clear()
            binding.etSampleAdminEmail.text.clear()
            binding.etPhone.text.clear()
            sImage=""
            binding.btnUpdate.visibility=View.INVISIBLE
            binding.btnDelete.visibility=View.INVISIBLE
            binding.btnEdit.visibility=View.VISIBLE

            Toast.makeText(this,"Data Updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Data Not Updated",Toast.LENGTH_SHORT).show()

        }

    }//delete admin data
    fun delete_data(view: View){
        db=FirebaseDatabase.getInstance().getReference("items")
        db.child(nodeId).removeValue()

    }



}