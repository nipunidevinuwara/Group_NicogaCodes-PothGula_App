package com.example.adminfull

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class requestAdapter (private val requestlst:ArrayList<Request>): RecyclerView.Adapter<requestAdapter.requestHolder>() {
    //passing the data from an array list
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {//implementing a callback method
        mListener = listener
    }

    // to fetch the data from the database and display it in the recycler view
    class requestHolder(itmView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itmView) {
        val stream1: TextView = itmView.findViewById(R.id.et_streamname)
        val subject1: TextView = itmView.findViewById(R.id.et_subjectname)
        val itmdt:TextView=itmView.findViewById(R.id.et_dt)

        init {
            itmView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }


    }

    //member classes of the object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): requestHolder {
        val requestView =
            LayoutInflater.from(parent.context).inflate(R.layout.requests, parent, false)//admin.xml
        return requestHolder(requestView, mListener)
    }

    override fun onBindViewHolder(holder: requestHolder, position: Int) {
        val currentrequest = requestlst[position]
        val dateFormat=SimpleDateFormat("yyyy-MM-dd hh:mm a")
        holder.stream1.setText(currentrequest.stream2.toString())
        holder.subject1.setText(currentrequest.subject2.toString())
        holder.itmdt.text=milisecondstoDate(currentrequest.mfgDt.toString(),dateFormat)


    }
    private fun milisecondstoDate(milliseconds:String,dateFormat: SimpleDateFormat):String{
        val millis:Long=milliseconds.toLong()
        return dateFormat.format(millis)

    }


    override fun getItemCount(): Int {
        return requestlst.size
    }

}

