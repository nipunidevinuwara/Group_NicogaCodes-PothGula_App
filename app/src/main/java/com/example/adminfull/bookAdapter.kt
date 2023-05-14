package com.example.adminfull

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class bookAdapter(private val booklst:ArrayList<Books>): RecyclerView.Adapter<bookAdapter.bookHolder>() {//passing the data from an array list
private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){//implementing a callback method
        mListener=listener
    }
    // to fetch the data from the database and display it in the recycler view
    class bookHolder(itmView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itmView) {
        val stream: TextView =itmView.findViewById(R.id.sp_stream)
        val subject: TextView =itmView.findViewById(R.id.sp_subject)
        val date: TextView =itmView.findViewById(R.id.book_date)
        val bookimg: ImageView =itmView.findViewById(R.id.img_displayviewBook)
        init {
            itmView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }


    }
    //member classes of the object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bookHolder {
        val bookView =
            LayoutInflater.from(parent.context).inflate(R.layout.book, parent, false)//book.xml
        return bookHolder(bookView,mListener)
    }

    override fun onBindViewHolder(holder: bookHolder, position: Int) {
        val currentbook=booklst[position]
        holder.stream.setText(currentbook.stream.toString())
        holder.subject.setText(currentbook.subject.toString())
        holder.date.setText(currentbook.year.toString())
        val bytes=android.util.Base64.decode(currentbook.BookImg,android.util.Base64.DEFAULT)
        val bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.bookimg.setImageBitmap(bitmap)



    }


    override fun getItemCount(): Int {
        return booklst.size
    }
}