package com.example.adminfull

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class BookList : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private  lateinit var bookRecyclerView: RecyclerView
    private lateinit var bookArrayList: ArrayList<Books>
    private val nodeList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)
        bookRecyclerView=findViewById(R.id.book_list)
        bookRecyclerView.layoutManager= LinearLayoutManager(this)
        bookRecyclerView.hasFixedSize()
        bookArrayList= arrayListOf<Books>()
        getBookData()
    }
    private fun getBookData() {
        db= FirebaseDatabase.getInstance().getReference("books")//referring the items table created in database
        db.addValueEventListener(object : ValueEventListener {// used to receive events about data changes at a location
        override fun onDataChange(snapshot: DataSnapshot) {
            if(snapshot.exists()){
                for(booksnapshot in snapshot.children){
                    val item=booksnapshot.getValue(Books::class.java)
                    bookArrayList.add(item!!)
                    nodeList.add(booksnapshot.key.toString())
                }
                var adapter=bookAdapter(bookArrayList)
                bookRecyclerView.adapter=adapter
                adapter.setOnItemClickListener(object :bookAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val nodePath:String=nodeList[position]
                        val intent= Intent()
                        intent.putExtra("bookID",nodePath)//sending activity to main activity through a node path where result code=2
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