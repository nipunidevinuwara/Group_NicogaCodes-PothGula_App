package com.example.adminfull

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class RequestList : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private  lateinit var requestRecyclerView: RecyclerView
    private lateinit var requestArrayList: ArrayList<Request>
    private val nodeList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_list)
        requestRecyclerView=findViewById(R.id.request_list)
        requestRecyclerView.layoutManager= LinearLayoutManager(this)
        requestRecyclerView.hasFixedSize()
        requestArrayList= arrayListOf<Request>()
        getRequestData()
    }
    private fun getRequestData() {
        db= FirebaseDatabase.getInstance().getReference("request")//referring the items table created in database
        var query:Query=db.orderByKey()
        query=db.orderByChild("mfgDt")
        query.addValueEventListener(object : ValueEventListener {// used to receive events about data changes at a location,default is ascending order
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(requestsnapshot in snapshot.children){
                    val item=requestsnapshot.getValue(Request::class.java)
                    requestArrayList.add(item!!)
                    nodeList.add(requestsnapshot.key.toString())
                }
                var adapter=requestAdapter(requestArrayList)
                requestRecyclerView.adapter=adapter
                adapter.setOnItemClickListener(object :requestAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val nodePath:String=nodeList[position]
                        val intent= Intent()
                        intent.putExtra("requestID",nodePath)//sending activity to main activity through a node path where result code=2
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