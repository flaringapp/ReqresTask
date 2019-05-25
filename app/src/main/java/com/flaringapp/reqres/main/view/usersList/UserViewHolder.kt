package com.flaringapp.reqres.main.view.usersList

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.reqres.R

class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val container: View = view.findViewById(R.id.container)
    val imageView: ImageView = view.findViewById(R.id.image)
    val nameTextView: TextView = view.findViewById(R.id.full_name)
}