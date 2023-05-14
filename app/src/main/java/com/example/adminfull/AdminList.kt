package com.example.adminfull

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminList : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private  lateinit var adminRecyclerView: RecyclerView
    private lateinit var adminArrayList: ArrayList<Admin>
    private val nodeList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_list)
        adminRecyclerView=findViewById(R.id.admin_list)
        adminRecyclerView.layoutManager= LinearLayoutManager(this)
        adminRecyclerView.hasFixedSize()
        adminArrayList= arrayListOf<Admin>()
        getAdminData()
    }
    private fun getAdminData() {
        db= FirebaseDatabase.getInstance().getReference("items")//referring the items table created in database
        db.addValueEventListener(object : ValueEventListener {// used to receive events about data changes at a location
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(adminsnapshot in snapshot.children){
                    val item=adminsnapshot.getValue(Admin::class.java)
                    adminArrayList.add(item!!)
                    nodeList.add(adminsnapshot.key.toString())
                }
                var adapter=adminAdapter(adminArrayList)
                adminRecyclerView.adapter=adapter
                adapter.setOnItemClickListener(object :adminAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val nodePath:String=nodeList[position]
                        val intent= Intent()
                        intent.putExtra("adminID",nodePath)//sending activity to main activity through a node path where result code=2
                        setResult(2,intent)
                        finish()//finish child activity
                    }

                })
            }
        }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}