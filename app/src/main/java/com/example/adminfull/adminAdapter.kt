package com.example.adminfull

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// The AdapterView where the click happened
class adminAdapter(private val admnlst:ArrayList<Admin>): RecyclerView.Adapter<adminAdapter.adminHolder>() {//passing the data from an array list
private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){//implementing a callback method
        mListener=listener
    }
    // to fetch the data from the database and display it in the recycler view
    class adminHolder(itmView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itmView) {
        val adminname: TextView =itmView.findViewById(R.id.tv_displayadminname)
        val adminemail: TextView =itmView.findViewById(R.id.tv_displayadminstream)
        val adminphone: TextView =itmView.findViewById(R.id.tv_displayadminsubject)
        val adminimg: ImageView =itmView.findViewById(R.id.img_displayviewAdmin)
        init {
            itmView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }


    }
    //member classes of the object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adminHolder {
        val adminView =
            LayoutInflater.from(parent.context).inflate(R.layout.admin, parent, false)//admin.xml
        return adminHolder(adminView,mListener)
    }

    override fun onBindViewHolder(holder: adminHolder, position: Int) {
        val currentadmin=admnlst[position]
        holder.adminname.setText(currentadmin.fullName.toString())
        holder.adminemail.setText(currentadmin.email.toString())
        holder.adminphone.setText(currentadmin.phone.toString())
        val bytes=android.util.Base64.decode(currentadmin.adminImg,android.util.Base64.DEFAULT)
        val bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.adminimg.setImageBitmap(bitmap)



    }


    override fun getItemCount(): Int {
        return admnlst.size
    }
}